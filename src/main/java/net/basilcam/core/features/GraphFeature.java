package net.basilcam.core.features;

import net.basilcam.core.Direction;
import net.basilcam.core.tiles.TileSection;

import java.util.*;

public abstract class GraphFeature implements Feature {
    private final Map<TileSection, GraphFeatureNode> featureNodes;

    public GraphFeature(TileSection section) {
        this.featureNodes = new HashMap<>();

        GraphFeatureNode node = new GraphFeatureNode(section);
        this.featureNodes.put(section, node);
    }

    public GraphFeature() {
        this.featureNodes = new HashMap<>();
    }

    public void addCenterNode(GraphFeatureNode centerNode, TileSection existingSection, Direction directionFromExisting) {
        GraphFeatureNode existingNode = this.featureNodes.get(existingSection);
        featureNodes.put(centerNode.getTileSection(), centerNode);

        existingNode.connectNode(centerNode, directionFromExisting);
        centerNode.connectNode(existingNode, directionFromExisting.oppositeDirection());
    }

    public void addAbuttingNode(TileSection newSection, TileSection existingSection, Direction directionFromExisting) {
        GraphFeatureNode existingNode = this.featureNodes.get(existingSection); // todo: null check assertion
        GraphFeatureNode newNode = new GraphFeatureNode(newSection);
        featureNodes.put(newSection, newNode);

        existingNode.connectNode(newNode, directionFromExisting);
        newNode.connectNode(existingNode, directionFromExisting.oppositeDirection());

        for (Direction direction : directionFromExisting.perpendicularDirections()) {
            newNode.closeNode(direction);
        }
    }

    public void merge(Collection<? extends GraphFeature> graphFeatures) {
        for (GraphFeature feature : graphFeatures) {
            this.featureNodes.putAll(feature.featureNodes);
        }
    }

    public Collection<TileSection> getTileSections() {
        return this.featureNodes.keySet();
    }
}
