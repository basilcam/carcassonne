package net.basilcam.core.features;

import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileSectionType;

import java.util.*;

public class GraphFeature implements Feature {
    private final Map<TileSection, GraphFeatureNode> featureNodes;
    private final TileSectionType type;

    public GraphFeature(TileSection section) {
        this.featureNodes = new HashMap<>();
        this.type = section.getType();

        GraphFeatureNode node = new GraphFeatureNode(section);
        this.featureNodes.put(section, node);
    }

    public GraphFeature(TileSectionType type) {
        this.featureNodes = new HashMap<>();
        this.type = type;
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

    public void addNode(GraphFeatureNode node) {
        this.featureNodes.put(node.getTileSection(), node);
    }

    public GraphFeatureNode getNode(TileSection tileSection) {
        return this.featureNodes.get(tileSection);
    }

    public Collection<TileSection> getTileSections() {
        return this.featureNodes.keySet();
    }

    public void merge(Collection<? extends GraphFeature> graphFeatures) {
        for (GraphFeature feature : graphFeatures) {
            this.featureNodes.putAll(feature.featureNodes);
        }
    }
}
