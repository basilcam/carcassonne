package net.basilcam.core;

public class TurnState {
    private boolean hasPlacedTile;
    private boolean hasPlacedMeeple;
    private boolean hasScored;

    public TurnState() {
        this.hasPlacedTile = false;
        this.hasPlacedMeeple = false;
        this.hasScored = false;
    }

    public void nextTurn() {
        this.hasPlacedTile = false;
        this.hasPlacedMeeple = false;
        this.hasScored = false;
    }

    public boolean hasPlacedTile() {
        return this.hasPlacedTile;
    }

    public void placedTile() {
        this.hasPlacedTile = true;
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
