package net.basilcam.core.features;

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

    public void addNode(GraphFeatureNode node) {
        this.featureNodes.put(node.getTileSection(), node);
    }

    public void merge(Collection<? extends GraphFeature> graphFeatures) {
        for (GraphFeature feature : graphFeatures) {
            this.featureNodes.putAll(feature.featureNodes);
        }
    }

    public Collection<TileSection> getTileSections() {
        return this.featureNodes.keySet();
    }

    public GraphFeatureNode getNode(TileSection tileSection) {
        return this.featureNodes.get(tileSection);
    }

    @Override
    public boolean isComplete() {
        for (GraphFeatureNode node : this.featureNodes.values()) {
            if (node.hasOpenConnection()) {
                return false;
            }
        }
        return true;
    }
}
