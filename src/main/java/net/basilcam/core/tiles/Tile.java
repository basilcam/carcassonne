package net.basilcam.core.tiles;

public class Tile {
    private static final int NUMBER_ROWS = 3;
    private static final int NUMBER_COLUMNS = 3;
    private TileSection[][] tileSections;

    // [0][1] is top section
    // [2][1] is bottom section
    // [1][0] is left section
    // [1][2] is right section

    Tile(TileSection[][] tileSections) {
        this.tileSections = tileSections;
    }

    public TileSection getSection(int x, int y) {
        assert x >= 0 && x <= 2;
        assert y >= 0 && y <= 2;

        return tileSections[x][y];
    }

    public void rotateClockwise() {
        // todo
    }
}
