package net.basilcam.core.tiles;

import com.google.common.collect.Multimap;
import net.basilcam.core.Board;
import org.jetbrains.annotations.TestOnly;

import java.util.Optional;

public class TestTileManager {
    private TileManager tileManager;

    public TestTileManager(Board board) {
        this.tileManager = TileManager.create(board);
    }

    public TileManager getTileManager() {
        return this.tileManager;
    }

    public Tile drawTileById(int id) {
        Tile tile = TestTileManager.createTileById(id);
        for (TileSection tileSection : tile.getSections()) {
            this.tileManager.addTileSectionMapping(tileSection, tile);
        }
        return tile;
    }

    @TestOnly
    private static Tile createTileById(int id) {
        Multimap<Integer, Tile> tileMap = TileStackFactory.createTileMap();
        Optional<Tile> tile = tileMap.get(id).stream().findFirst();
        assert tile.isPresent() : "tile does not exist with specified id " + id;
        return tile.get();
    }
}
