package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.tiles.TileSection;

public class RoadFeatureManager extends GraphFeatureManager<RoadFeature> {
    RoadFeatureManager(Board board) {
        super(board);
    }

    @Override
    public RoadFeature createFeature(TileSection tileSection) {
        return new RoadFeature(tileSection);
    }

    @Override
    public RoadFeature createEmptyFeature() {
        return new RoadFeature();
    }
}
