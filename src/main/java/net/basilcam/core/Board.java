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

    public void clear() {
        tiles.clear();

    }
}
