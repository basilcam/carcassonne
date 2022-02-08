package net.basilcam.core.tiles;

import net.basilcam.core.Meeple;

import java.util.Optional;

public class TileSection {
    private final Tile tile;
    private Optional<Meeple> meeple;
    private final TileSectionType type;

    public TileSection(Tile tile, TileSectionType type) {
        this.tile = tile;
        this.meeple = Optional.empty();
        this.type = type;
    }

    public Tile getTile() {
        return this.tile;
    }

    public Optional<Meeple> getMeeple() {
        return meeple;
    }

    public void placeMeeple(Meeple meeple) {
        this.meeple = Optional.of(meeple);
    }

    public void removeMeeple() {
        this.meeple = Optional.empty();
    }

    public TileSectionType getType() {
        return type;
    }
}
