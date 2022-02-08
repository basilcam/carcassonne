package net.basilcam.core.tiles;

import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TileManager {
    private List<Tile> tiles;
    protected Map<TileSection, Tile> sectionToTile;

    private TileManager() {
    }

    public static TileManager create() {
        TileManager tileManager = new TileManager();
        tileManager.reset();
        return tileManager;
    }

    public void reset() {
        this.tiles = TileStackFactory.createTileStack();
        this.sectionToTile = new HashMap<>();
        for (Tile tile : this.tiles) {
            for (TileSection tileSection : tile.getSections()) {
                this.sectionToTile.put(tileSection, tile);
            }
        }
    }

    public Optional<Tile> drawTile() {
        if (this.tiles.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.tiles.remove(0));
    }

    public boolean hasMoreTiles() {
        return this.tiles.isEmpty();
    }

    public Tile getTileFromSection(TileSection tileSection) {
        return this.sectionToTile.get(tileSection);
    }

    @TestOnly
    public void addTileSectionMapping(TileSection tileSection, Tile tile) {
        this.sectionToTile.put(tileSection, tile);
    }
}
