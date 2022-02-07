package net.basilcam.core.features;

import net.basilcam.core.Board;
import net.basilcam.core.Meeple;
import net.basilcam.core.PlacementValidator;
import net.basilcam.core.Player;
import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;
import net.basilcam.core.tiles.TileSectionType;
import net.basilcam.core.tiles.TileStackFactory;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphFeatureManagerTest {
    private Board board;
    private GraphFeatureManager featureManager;
    private Player player;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
        this.featureManager = new GraphFeatureManager(this.board);
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
        Tile tile = TileStackFactory.getTileById(20);
        tile.rotateClockwise();
        tile.rotateClockwise();

        placeTileAndUpdate(tile, 0, 1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, true);
        assertFeature(TileSectionType.ROAD, 1, false);
    }

    @Test
    public void shouldUpdateFeatures_tilePlacedBesideTwoTiles_t() {
        Tile tile15 = TileStackFactory.getTileById(15);
        placeTileAndUpdate(tile15, 1, 0);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile11 = TileStackFactory.getTileById(11);
        tile11.rotateClockwise();
        tile11.rotateClockwise();
        placeTileAndUpdate(tile11, 0, 1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile20 = TileStackFactory.getTileById(20);
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
        Tile tile15 = TileStackFactory.getTileById(15);
        placeTileAndUpdate(tile15, 1, 0);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile2 = TileStackFactory.getTileById(2);
        placeTileAndUpdate(tile2, 1, -1);
        assertFeatureCount(5);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 4, false);

        Tile tile10 = TileStackFactory.getTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 0, -1);
        assertFeatureCount(5);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 4, false);

        Tile tile7 = TileStackFactory.getTileById(7);
        tile7.rotateClockwise();
        tile7.rotateClockwise();
        placeTileAndUpdate(tile7, -1, -1);
        assertFeatureCount(7);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 5, false);
        assertFeature(TileSectionType.ROAD, 1, true);

        Tile tile14 = TileStackFactory.getTileById(14);
        placeTileAndUpdate(tile14, -1, 0);
        assertFeatureCount(7);
        assertFeature(TileSectionType.CITY, 2, false);
        assertFeature(TileSectionType.ROAD, 3, false);
        assertFeature(TileSectionType.ROAD, 2, true);
    }

    @Test
    public void shouldUpdateFeatures_continuousCircleRoad() {
        Tile tile15 = TileStackFactory.getTileById(15);
        placeTileAndUpdate(tile15, 1, 0);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile15_2 = TileStackFactory.getTileById(15);
        tile15_2.rotateClockwise();
        placeTileAndUpdate(tile15_2, 1, -1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile10 = TileStackFactory.getTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 0, -1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile15_3 = TileStackFactory.getTileById(15);
        tile15_3.rotateClockwise();
        tile15_3.rotateClockwise();
        placeTileAndUpdate(tile15_3, -1, -1);
        assertFeatureCount(2);
        assertFeature(TileSectionType.CITY, 1, false);
        assertFeature(TileSectionType.ROAD, 1, false);

        Tile tile14 = TileStackFactory.getTileById(14);
        placeTileAndUpdate(tile14, -1, 0);
        assertFeatureCount(3);
        assertFeature(TileSectionType.CITY, 2, false);
        assertFeature(TileSectionType.ROAD, 1, true);
    }

    @Test
    public void shouldNotAllowPlacingMeepleOnRoadWithExistingMeeple() {
        Tile tile10 = TileStackFactory.getTileById(10);
        tile10.rotateClockwise();
        placeTileAndUpdate(tile10, 1, 0);

        TileSection roadSection = tile10.getLeftSection();
        assertThat(this.featureManager.canPlaceMeeple(tile10, roadSection)).isTrue();
        placeMeeple(roadSection);

        Tile tile15 = TileStackFactory.getTileById(15);
        placeTileAndUpdate(tile15, 2, 0);

        TileSection anotherRoadSection = tile15.getBottomSection();
        assertThat(this.featureManager.canPlaceMeeple(tile15, anotherRoadSection)).isFalse();
    }

    @Test
    public void shouldNotAllowPlacingMeepleOnCityWithExistingMeeple() {

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