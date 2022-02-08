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

public class GraphFeatureManagerTest {
    private Board board;
    private TestTileManager tileManager;
    private GraphFeatureManager featureManager;
    private Player player;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.tileManager = new TestTileManager();
        this.featureManager = new GraphFeatureManager(this.board, this.tileManager.getTileManager());
        this.player = Player.createPlayer("cam");
    }

    @Test
    public void shouldUpdateFeaturesForStartTile() {
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);
    }

    @Test
    public void shouldUpdateFeatures_tilePlacedAboveStartTile() {
        Tile tile = tileManager.drawTileById(20);
        tile.rotateClockwise();
        tile.rotateClockwise();

        placeTileAndUpdate(tile, 0, 1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 1, false);
    }

    @Test
    public void shouldUpdateFeatures_tilePlacedBesideTwoTiles_t() {
        Tile tile15 = tileManager.drawTileById(15);
        placeTileAndUpdate(tile15, 1, 0);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

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
    }

    @Test
    public void shouldUpdateFeatures_splitCircleRoad() {
        Tile tile15 = tileManager.drawTileById(15);
        placeTileAndUpdate(tile15, 1, 0);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile2 = tileManager.drawTileById(2);
        placeTileAndUpdate(tile2, 1, -1);
        assertFeatureCount(5);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 4, false);

        Tile tile10 = tileManager.drawTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 0, -1);
        assertFeatureCount(5);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 4, false);

        Tile tile7 = tileManager.drawTileById(7);
        tile7.rotateClockwise();
        tile7.rotateClockwise();
        placeTileAndUpdate(tile7, -1, -1);
        assertFeatureCount(7);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 5, false);
        assertFeature(TileSectionType.ROAD, 1, true);

        Tile tile14 = tileManager.drawTileById(14);
        placeTileAndUpdate(tile14, -1, 0);
        assertFeatureCount(7);
        assertFeature(TileSectionType.CITY, 2, false);
        assertFeature(TileSectionType.ROAD, 3, false);
        assertFeature(TileSectionType.ROAD, 2, true);
    }

    @Test
    public void shouldUpdateFeatures_continuousCircleRoad() {
        Tile tile15 = tileManager.drawTileById(15);
        placeTileAndUpdate(tile15, 1, 0);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile15_2 = tileManager.drawTileById(15);
        tile15_2.rotateClockwise();
        placeTileAndUpdate(tile15_2, 1, -1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile10 = tileManager.drawTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 0, -1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile15_3 = tileManager.drawTileById(15);
        tile15_3.rotateClockwise();
        tile15_3.rotateClockwise();
        placeTileAndUpdate(tile15_3, -1, -1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile14 = tileManager.drawTileById(14);
        placeTileAndUpdate(tile14, -1, 0);
        assertFeatureCount(3);
        assertFeature(TileSectionType.CITY, 2, false);
        assertFeature(TileSectionType.ROAD, 1, true);
    }

    @Test
    public void shouldNotAllowPlacingMeepleOnRoadWithExistingMeeple() {
        Tile tile10 = tileManager.drawTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 1, 0);

        TileSection roadSection = tile10.getLeftSection();
        assertThat(this.featureManager.canPlaceMeeple(tile10, roadSection)).isTrue();
        placeMeeple(roadSection);

        Tile tile15 = tileManager.drawTileById(15);
        placeTileAndUpdate(tile15, 2, 0);

        TileSection anotherRoadSection = tile15.getBottomSection();
        assertThat(this.featureManager.canPlaceMeeple(tile15, anotherRoadSection)).isFalse();
    }

    @Test
    public void shouldNotAllowPlacingMeepleOnCityWithExistingMeeple() {
        Tile tile8 = tileManager.drawTileById(8);
        tile8.rotateClockwise();
        placeTileAndUpdate(tile8, 0, 1);

        TileSection citySection = tile8.getBottomSection();
        assertThat(citySection.getType()).isEqualTo(TileSectionType.CITY);
        assertThat(this.featureManager.canPlaceMeeple(tile8, citySection)).isTrue();
        placeMeeple(citySection);

        Tile tile20 = tileManager.drawTileById(20);
        tile20.rotateClockwise();
        tile20.rotateClockwise();
        placeTileAndUpdate(tile20, 0, 2);

        citySection = tile20.getBottomSection();
        assertThat(citySection.getType()).isEqualTo(TileSectionType.CITY);
        assertThat(this.featureManager.canPlaceMeeple(tile20, citySection)).isFalse();
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