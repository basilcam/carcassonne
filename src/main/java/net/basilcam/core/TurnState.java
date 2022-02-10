package net.basilcam.core;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class TurnState {
    private final ImmutableList<Player> players;
    private int currentPlayerIndex;

    private boolean hasPlacedTile;
    private boolean hasPlacedMeeple;
    private boolean hasScored;

    public TurnState(List<Player> players) {
        assert !players.isEmpty();

        this.players = ImmutableList.copyOf(players);
        this.currentPlayerIndex = 0;

        this.hasPlacedTile = false;
        this.hasPlacedMeeple = false;
        this.hasScored = false;
    }

    public void nextTurn() {
        this.hasPlacedTile = false;
        this.hasPlacedMeeple = false;
        this.hasScored = false;

        this.currentPlayerIndex++;
        if (this.currentPlayerIndex >= this.players.size()) {
            this.currentPlayerIndex = 0;
        }
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

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }
}
