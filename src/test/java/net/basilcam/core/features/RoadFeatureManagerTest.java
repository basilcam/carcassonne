package net.basilcam.core.features;

import net.basilcam.core.Board;

public class RoadFeatureManagerTest extends GraphFeatureManagerTest<RoadFeature> {

    @Override
    public GraphFeatureManager<RoadFeature> createFeatureManager(Board board) {
        return new RoadFeatureManager(board);
    }
}
