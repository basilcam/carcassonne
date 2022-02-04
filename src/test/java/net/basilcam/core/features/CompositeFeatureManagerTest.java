package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.PlacementValidator;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileStackFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeFeatureManagerTest {
    private Board board;
    private CompositeFeatureManager featureManager;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = new CompositeFeatureManager(board);
    }

    @Test
    public void shouldUpdateFeaturesForStartTile() {
        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(2); // city, road
        assertThat(features).allMatch(feature -> !feature.isComplete());
        // todo: not done yet
    }

    @Test
    public void test() {
        Tile tile = TileStackFactory.getTile(20);
        tile.rotateClockwise();
        tile.rotateClockwise();

        assertThat(PlacementValidator.isValid(this.board, 0, 1, tile)).isTrue();
        this.board.placeTile(tile, 0, 1);

        this.featureManager.updateFeatures(tile, 0, 1);

        Collection<? extends Feature> features = this.featureManager.getFeatures();
        assertThat(features).hasSize(3);

        // todo: not done yet
    }
}