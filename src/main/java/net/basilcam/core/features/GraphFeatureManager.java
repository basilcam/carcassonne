package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.Direction;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileSectionType;

import java.util.*;

public abstract class GraphFeatureManager<T extends GraphFeature> implements FeatureManager {
    private final Board board;
    private final Map<TileSection, T> tileSectionToFeature;

    // todo: consider refactoring this so we pass in TileSections rather than Tiles
    // todo: that way we can rely on generics instead of casing on TileSection types
    // todo: we're currently diverging from OOP dev too much

    GraphFeatureManager(Board board) {
        this.board = board;
        this.tileSectionToFeature = new HashMap<>();

        // todo: i don't like this explicity replies on start tile location / existence
        Optional<Tile> startTile = this.board.getTile(0, 0);
        assert startTile.isPresent();
        updateFeatures(startTile.get(), 0, 0);
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
        this.tileSectionToFeature.clear();
    }

    @Override
    public Collection<T> getFeatures() {
        return Set.copyOf(this.tileSectionToFeature.values());
    }

    public abstract T createFeature(TileSection tileSection);

    public abstract T createEmptyFeature();

    public abstract TileSectionType getTileSectionType();

    private void updateFeaturesForEdges(Tile tile, int xPosition, int yPosition, Direction direction) {
        TileSection tileSection = tile.getSection(direction);

        if (tileSection.getType() != getTileSectionType()) {
            return;
            // todo: refactor, i really don't like this
        }

        Optional<Tile> abuttingTile = this.board.getAbuttingTile(xPosition, yPosition, direction);
        if (abuttingTile.isPresent()) {
            TileSection abuttingSection = abuttingTile.get().getSection(direction.oppositeDirection());
            assert abuttingSection.getType() != tileSection.getType() : "tile placement is invalid";

            T abuttingFeature = this.tileSectionToFeature.get(abuttingSection);
            assert abuttingFeature != null : "no feature found for section";

            addAbuttingNode(abuttingFeature, tileSection, abuttingSection, direction.oppositeDirection());
            this.tileSectionToFeature.put(tileSection, abuttingFeature);
        } else {
            T feature = createFeature(tileSection);
            this.tileSectionToFeature.put(tileSection, feature);
        }
    }

    private void updateFeaturesForCenter(Tile tile) {
        for (TileSection centerSection : tile.getCenterSections()) {
            GraphFeatureNode centerNode = new GraphFeatureNode(centerSection);

            if (centerSection.getType() != getTileSectionType()) {
                continue;
                // todo: refactor, i really don't like this
            }

            List<T> connectedFeatures = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                TileSection tileSection = tile.getSection(direction);
                if (tileSection.getType() != centerSection.getType()) {
                    centerNode.closeNode(direction);
                    continue;
                }

                T feature = this.tileSectionToFeature.get(tileSection);
                assert feature != null : "unexpected missing feature";

                addCenterNode(feature, centerNode, tileSection, direction.oppositeDirection());

                connectedFeatures.add(feature);
            }

            if (connectedFeatures.size() == 0) {
                continue;
            }

            if (connectedFeatures.size() == 1) {
                this.tileSectionToFeature.put(centerSection, connectedFeatures.get(0));
                continue;
            }

            T mergedFeature = createEmptyFeature();
            mergedFeature.merge(connectedFeatures);
            for (TileSection tileSection : mergedFeature.getTileSections()) {
                this.tileSectionToFeature.put(tileSection, mergedFeature);
            }
        }
    }

    private void addAbuttingNode(GraphFeature feature,
                                 TileSection newSection,
                                 TileSection existingSection,
                                 Direction directionFromExisting) {
        GraphFeatureNode existingNode = feature.getNode(existingSection);
        GraphFeatureNode newNode = new GraphFeatureNode(newSection);
        feature.addNode(newNode);

        existingNode.connectNode(newNode, directionFromExisting);
        newNode.connectNode(existingNode, directionFromExisting.oppositeDirection());

        for (Direction direction : directionFromExisting.perpendicularDirections()) {
            newNode.closeNode(direction);
        }
    }

    private void addCenterNode(GraphFeature feature,
                               GraphFeatureNode centerNode,
                               TileSection existingSection,
                               Direction directionFromExisting) {
        GraphFeatureNode existingNode = feature.getNode(existingSection);

        existingNode.connectNode(centerNode, directionFromExisting);
        centerNode.connectNode(existingNode, directionFromExisting.oppositeDirection());

        feature.addNode(centerNode);
    }
}