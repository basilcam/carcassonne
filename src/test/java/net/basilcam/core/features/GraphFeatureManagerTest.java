package net.basilcam.core.features;

import net.basilcam.core.Board;
import org.junit.jupiter.api.BeforeEach;

public abstract class GraphFeatureManagerTest<T extends GraphFeature> {
    private Board board;
    private GraphFeatureManager<T> featureManager;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = createFeatureManager(this.board);
    }

    public abstract GraphFeatureManager<T> createFeatureManager(Board board);
}