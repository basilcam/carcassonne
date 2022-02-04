package net.basilcam.core.features;

import net.basilcam.core.Board;
import org.junit.jupiter.api.BeforeEach;

public class GraphFeatureManagerTest {
    private Board board;
    private GraphFeatureManager featureManager;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = new GraphFeatureManager(this.board);
    }
}