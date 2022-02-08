package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.Meeple;
import net.basilcam.core.PlacementValidator;
import net.basilcam.core.Player;
import net.basilcam.core.tiles.*;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeFeatureManagerTest {
    private Board board;
    private TestTileManager tileManager;
    private CompositeFeatureManager featureManager;
    private Player player;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.tileManager = new TestTileManager();
        this.featureManager = new CompositeFeatureManager(this.board, this.tileManager.getTileManager());
        this.player = Player.createPlayer("cam");
    }

    @Test
    public void shouldCompleteFeatureOfEachType() {
        Tile tile11 = tileManager.drawTileById(11);
        tile11.rotateClockwise();
        tile11.rotateClockwise();
        placeTileAndUpdate(tile11, 0, 1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile20 = tileManager.drawTileById(20);
        tile20.rotateClockwise();
        tile20.rotateClockwise();
        tile20.rotateClockwise();
        placeTileAndUpdate(tile20, 1, 1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile7 = tileManager.drawTileById(7);
        tile7.rotateClockwise();
        tile7.rotateClockwise();
        tile7.rotateClockwise();
        placeTileAndUpdate(tile7, 2, 1);
        assertFeatureCount(5);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 4, false);

        Tile tile18 = tileManager.drawTileById(18);
        tile18.rotateClockwise();
        placeTileAndUpdate(tile18, 1, 0);
        assertFeatureCount(6);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 4, false);
        assertFeature(TileSectionType.MONASTERY, 1, false);

        Tile tile10_1 = tileManager.drawTileById(10);
        placeTileAndUpdate(tile10_1, 2, 0);
        assertFeatureCount(6);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 4, false);
        assertFeature(TileSectionType.MONASTERY, 1, false);

        Tile tile7_2 = tileManager.drawTileById(7);
        placeTileAndUpdate(tile7_2, 0, -1);
        assertFeatureCount(9);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 7, false);
        assertFeature(TileSectionType.MONASTERY, 1, false);

        Tile tile10_3 = tileManager.drawTileById(10);
        tile10_3.rotateClockwise();
        placeTileAndUpdate(tile10_3, 1, -1);
        assertFeatureCount(9);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 7, false);
        assertFeature(TileSectionType.MONASTERY, 1, false);

        Tile tile15 = tileManager.drawTileById(15);
        tile15.rotateClockwise();
        placeTileAndUpdate(tile15, 2, -1);
        assertFeatureCount(8);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 5, false);
        assertFeature(TileSectionType.ROAD, 1, true);
        assertFeature(TileSectionType.MONASTERY, 1, true);
    }

    @Test
    public void shouldNotAllowPlacingMeepleOnFieldSection() {
        Tile tile10 = tileManager.drawTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 1, 0);

        TileSection section = tile10.getBottomSection();
        assertThat(section.getType()).isEqualTo(TileSectionType.FIELD);
        assertThat(this.featureManager.canPlaceMeeple(tile10, section)).isFalse();
    }

    @Test
    public void shouldNotAllowPlacingMeepleOnTileAlreadyContainingMeeple() {
        Tile tile10 = tileManager.drawTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 1, 0);

        TileSection section = tile10.getLeftSection();
        assertThat(section.getType()).isEqualTo(TileSectionType.ROAD);
        assertThat(this.featureManager.canPlaceMeeple(tile10, section)).isTrue();
        placeMeeple(section);

        section = tile10.getRightSection();
        assertThat(section.getType()).isEqualTo(TileSectionType.ROAD);
        assertThat(this.featureManager.canPlaceMeeple(tile10, section)).isFalse();
    }

    private void placeMeeple(TileSection tileSection) {
        Optional<Meeple> meeple = this.player.getMeeple();
        assertThat(meeple).isPresent();
        tileSection.placeMeeple(meeple.get());
    }

    private void assertFeatureCount(int count) {
        assertThat(this.featureManager.getFeatures()).hasSize(count);
    }

    private void assertFeature(TileSectionType type, int count, boolean isComplete) {
        Collection<? extends Feature> features = this.featureManager.getFeatures();

        assertThat(features).areExactly(count, new Condition<>(
                feature -> feature.getType() == type
                        && feature.isComplete() == isComplete,
                ""));

    }

    private void placeTileAndUpdate(Tile tile, int xPosition, int yPosition) {
        assertThat(PlacementValidator.isValid(this.board, xPosition, yPosition, tile)).isTrue();
        this.board.placeTile(tile, xPosition, yPosition);
        this.featureManager.updateFeatures(tile, xPosition, yPosition);
    }
}