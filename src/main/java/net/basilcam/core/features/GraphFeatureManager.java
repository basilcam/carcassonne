package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.Direction;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

import java.util.*;

public abstract class GraphFeatureManager<T extends GraphFeature> implements FeatureManager {
    private final Board board;
    private final Map<TileSection, T> features;

    GraphFeatureManager(Board board) {
        this.board = board;
        this.features = new HashMap<>();
    }

    @Override
    public void updateFeatures(Tile tile, int xPosition, int yPosition) {
        updateFeaturesForEdges(tile, xPosition, yPosition, Direction.UP);
        updateFeaturesForEdges(tile, xPosition, yPosition, Direction.LEFT);
        updateFeaturesForEdges(tile, xPosition, yPosition, Direction.DOWN);
        updateFeaturesForEdges(tile, xPosition, yPosition, Direction.RIGHT);
        updateFeaturesForCenter(tile);
    }

    @Override
    public void clear() {
        this.features.clear();
    }

    public abstract T createFeature(TileSection tileSection);

    public abstract T createEmptyFeature();

    private void updateFeaturesForEdges(Tile tile, int xPosition, int yPosition, Direction direction) {
        TileSection tileSection = tile.getSection(direction);
        Optional<Tile> abuttingTile = this.board.getAbuttingTile(xPosition, yPosition, direction);
        if (abuttingTile.isPresent()) {
            TileSection abuttingSection = abuttingTile.get().getSection(direction.oppositeDirection());
            assert abuttingSection.getType() != tileSection.getType() : "tile placement is invalid";

            T abuttingFeature = this.features.get(abuttingSection);
            assert abuttingFeature != null : "no feature found for section";

            addAbuttingNode(abuttingFeature, tileSection, abuttingSection, direction.oppositeDirection());
            this.features.put(tileSection, abuttingFeature);
        } else {
            T feature = createFeature(tileSection);
            this.features.put(tileSection, feature);
        }
    }

    private void updateFeaturesForCenter(Tile tile) {
        for (TileSection centerSection : tile.getCenterSections()) {
            GraphFeatureNode centerNode = new GraphFeatureNode(centerSection);

            List<T> connectedFeatures = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                TileSection tileSection = tile.getSection(direction);
                if (tileSection.getType() != centerSection.getType()) {
                    centerNode.closeNode(direction);
                    continue;
                }

                T feature = this.features.get(tileSection);
                assert feature != null : "unexpected missing feature";

                addCenterNode(feature, centerNode, tileSection, Direction.DOWN);

                connectedFeatures.add(feature);
            }

            if (connectedFeatures.size() == 0) {
                continue;
            }

            if (connectedFeatures.size() == 1) {
                this.features.put(centerSection, connectedFeatures.get(0));
                continue;
            }

            T mergedFeature = createEmptyFeature();
            mergedFeature.merge(connectedFeatures);
            for (TileSection tileSection : mergedFeature.getTileSections()) {
                this.features.put(tileSection, mergedFeature);
            }
        }
    }

    public void addAbuttingNode(GraphFeature feature, TileSection newSection, TileSection existingSection, Direction directionFromExisting) {
        GraphFeatureNode existingNode = feature.getNode(existingSection);
        GraphFeatureNode newNode = new GraphFeatureNode(newSection);
        feature.addNode(newNode);

        existingNode.connectNode(newNode, directionFromExisting);
        newNode.connectNode(existingNode, directionFromExisting.oppositeDirection());

        for (Direction direction : directionFromExisting.perpendicularDirections()) {
            newNode.closeNode(direction);
        }
    }

    public void addCenterNode(GraphFeature feature,
                              GraphFeatureNode centerNode,
                              TileSection existingSection,
                              Direction directionFromExisting) {
        GraphFeatureNode existingNode = feature.getNode(existingSection);

        existingNode.connectNode(centerNode, directionFromExisting);
        centerNode.connectNode(existingNode, directionFromExisting.oppositeDirection());

        feature.addNode(centerNode);
    }
}