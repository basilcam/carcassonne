package net.basilcam.core;

import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public class Meeple {
    private Optional<TileSection> tileSection; // todo: we don't actually need this, we just need to know if it's placed or not

    public Meeple() {
        this.tileSection = Optional.empty();
    }

    public Optional<TileSection> getTileSection() {
        return this.tileSection;
    }

    public void setTileSection(TileSection tileSection) {
        this.tileSection = Optional.of(tileSection);
    }

    public void removeFromTileSection() {
        this.tileSection = Optional.empty();
    }
}
