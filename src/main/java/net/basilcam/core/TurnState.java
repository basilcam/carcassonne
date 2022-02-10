package net.basilcam.core;

import net.basilcam.core.tiles.Tile;

import java.util.Optional;

public class TurnState {
    private boolean hasPlacedMeeple;
    private boolean hasScored;
    private Optional<Tile> lastPlacedTile;

    public TurnState() {
        this.hasPlacedMeeple = false;
        this.hasScored = false;
        this.lastPlacedTile = Optional.empty();
    }

    public boolean hasPlacedTile() {
        return this.lastPlacedTile.isPresent();
    }

    public void setLastPlacedTile(Tile tile) {
        this.lastPlacedTile = Optional.of(tile);
    }

    public Tile getLastPlacedTile() {
        assert this.lastPlacedTile.isPresent();
        return this.lastPlacedTile.get();
    }

    public boolean hasPlacedMeeple() {
        return this.hasPlacedMeeple;
    }

    public void placedMeeple() {
        this.hasPlacedMeeple = true;
    }

    public boolean hasScored() {
        return this.hasScored;
    }

    public void scored() {
        this.hasScored = true;
    }
}
