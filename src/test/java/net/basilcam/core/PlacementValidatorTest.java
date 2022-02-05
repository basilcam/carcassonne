package net.basilcam.core;

import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileStackFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlacementValidatorTest {
    private Board board;

    @BeforeEach
    public void beforeEach() {
        this.board = new Board();
    }

    @Test
    public void invalidTilePlacement_placedOnExistingTile() {
        Tile tile = TileStackFactory.getTileById(24);

        assertThat(isValid(tile, 0, 0)).isFalse();
    }

    @Test
    public void invalidTilePlacement_mismatchingSections_placedAboveExistingTile() {
        Tile tile = TileStackFactory.getTileById(2);

        assertThat(isValid(tile, 0, 1)).isFalse();
    }

    @Test
    public void invalidTilePlacement_mismatchingSections_placedLeftOfExistingTile() {
        Tile tile = TileStackFactory.getTileById(22);

        assertThat(isValid(tile, -1, 0)).isFalse();
    }

    @Test
    public void invalidTilePlacement_mismatchingSections_placedBelowExistingTile() {
        Tile tile = TileStackFactory.getTileById(14);

        assertThat(isValid(tile, 0, -1)).isFalse();
    }

    @Test
    public void invalidTilePlacement_mismatchingSections_placedRightOfExistingTile() {
        Tile tile = TileStackFactory.getTileById(14);

        assertThat(isValid(tile, 1, 0)).isFalse();
    }

    @Test
    public void validTilePlacement_matchingSections_placedAboveExistingTile() {
        Tile tile = TileStackFactory.getTileById(1);

        assertThat(isValid(tile, 0, 1)).isTrue();
    }

    @Test
    public void validTilePlacement_matchingSections_placedLeftOfExistingTile() {
        Tile tile = TileStackFactory.getTileById(2);

        assertThat(isValid(tile, -1, 0)).isTrue();
    }

    @Test
    public void validTilePlacement_matchingSections_placedBelowExistingTile() {
        Tile tile = TileStackFactory.getTileById(15);

        assertThat(isValid(tile, 0, -1)).isTrue();
    }

    @Test
    public void validTilePlacement_matchingSections_placedRightOfExistingTile() {
        Tile tile = TileStackFactory.getTileById(22);

        assertThat(isValid(tile, 1, 0)).isTrue();
    }

    @Test
    public void invalidTilePlacement_oneMatchingSection_oneMismatchingSection() {
        Tile validTile = TileStackFactory.getTileById(7);
        assertThat(isValid(validTile, 1, 0)).isTrue();
        this.board.placeTile(validTile, 1, 0);

        Tile anotherValidTile = TileStackFactory.getTileById(1);
        assertThat(isValid(anotherValidTile, 0, 1)).isTrue();
        this.board.placeTile(validTile, 0, 1);

        // the abutting sections between anotherValidTile (city) and this (city) match
        // the abutting sections between validTile (field) and this (road) don't match
        Tile invalidTile = TileStackFactory.getTileById(13);
        assertThat(isValid(invalidTile, 1, 1)).isFalse();
    }


    private boolean isValid(Tile tile, int xPosition, int yPosition) {
        return PlacementValidator.isValid(this.board, xPosition, yPosition, tile);

    }
}