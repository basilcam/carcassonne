package net.basilcam.core;

import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public class Meeple {
    private final Player owner;
    private Optional<TileSection> tileSection;

    public Meeple(Player owner) {
        this.owner = owner;
        this.tileSection = Optional.empty();
    }

    public Player getOwner() {
        return this.owner;
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
