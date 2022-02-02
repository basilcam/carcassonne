package net.basilcam.core;

import com.google.common.collect.Table;
import net.basilcam.core.tiles.Tile;

import java.util.Optional;

public class Board {
    private Table<Integer, Integer, Tile> tiles;

    // up is +y
    // down is -y
    // left is -x
    // right is +x

    public Optional<Tile> getTile(int x, int y) {
        return Optional.ofNullable(tiles.get(x, y));
    }

    public Optional<Tile> getTileRelativeTo(int x, int y, Direction direction) {
        switch (direction) {
            case UP:
                return getTile(x, y + 1);
            case LEFT:
                return getTile(x - 1, y);
            case DOWN:
                return getTile(x, y - 1);
            case RIGHT:
                return getTile(x + 1, y);
            default:
                return Optional.empty();
        }
    }

    public void clear() {
        tiles.clear();

    }
}
