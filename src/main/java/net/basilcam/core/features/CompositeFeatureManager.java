package net.basilcam.core.features;

import com.google.common.collect.Lists;
import net.basilcam.core.Board;
import net.basilcam.core.tiles.Tile;

import java.util.List;

public class CompositeFeatureManager implements FeatureManager {
    private List<FeatureManager> featureManagers;
    private Board board;

    public CompositeFeatureManager(Board board) {
        this.board = board;
        this.featureManagers = Lists.newArrayList(
                new CityFeatureManager(board),
                new RoadFeatureManager(board),
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
}
