package net.basilcam.core.features;

import net.basilcam.core.Board;

public class CityFeatureManagerTest extends GraphFeatureManagerTest<CityFeature> {

    @Override
    public GraphFeatureManager<CityFeature> createFeatureManager(Board board) {
        return new CityFeatureManager(board);
    }
}
