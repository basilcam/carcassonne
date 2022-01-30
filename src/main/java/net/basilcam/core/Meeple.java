package net.basilcam.core;

import net.basilcam.core.tiles.TileSection;

import java.util.Optional;

public class Meeple {
    private final Player owner;
    private Optional<TileSection> tile;

    public Meeple(Player owner) {
        this.owner = owner;
        this.tile = Optional.empty();
    }
}
