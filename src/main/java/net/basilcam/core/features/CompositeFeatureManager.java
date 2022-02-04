package net.basilcam.core.features;

import com.google.common.collect.Lists;
import net.basilcam.core.Board;
import net.basilcam.core.tiles.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompositeFeatureManager implements FeatureManager {
    private final List<FeatureManager> featureManagers;

    public CompositeFeatureManager(Board board) {
        this.featureManagers = Lists.newArrayList(
                new GraphFeatureManager(board),
                new MonasteryFeatureManager(board));
    }

    @Override
    public void updateFeatures(Tile tile, int xPosition, int yPosition) {
        for (FeatureManager featureManager : featureManagers) {
            featureManager.updateFeatures(tile, xPosition, yPosition);
        }
    }

    @Override
    public void clear() {
        for (FeatureManager featureManager : featureManagers) {
            featureManager.clear();
        }
    }

    @Override
    public Collection<? extends Feature> getFeatures() {
        List<Feature> features = new ArrayList<>();
        for (FeatureManager featureManager : featureManagers) {
            features.addAll(featureManager.getFeatures());
        }
        return features;
    }
}
