package net.basilcam.core;

import net.basilcam.core.tiles.Tile;
import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public enum PlacementValidator {

    ;

    public static boolean isValid(Board board, int xpos, int ypos, Tile tile) {

        TileSection topSection = tile.getSection(0, 1);
        Optional<Tile> adjacentTile = board.getTile(xpos, ypos - 1);








        // todo
        return false;
    }

    public static boolean isValid(TileSection tileSection, Meeple meeple) {
        // todo
        return false;
    }
}
