package net.basilcam.core;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class TurnState {
    private final ImmutableList<Player> players;
    private int currentPlayerIndex;

    private boolean hasPlacedTile;
    private boolean hasPlacedMeeple;

    public TurnState(List<Player> players) {
        assert !players.isEmpty();

        this.players = ImmutableList.copyOf(players);
        this.currentPlayerIndex = 0;

        this.hasPlacedTile = false;
        this.hasPlacedMeeple = false;
    }

    public void nextTurn() {
        this.hasPlacedTile = false;
        this.hasPlacedMeeple = false;

        this.currentPlayerIndex++;
        if (this.currentPlayerIndex >= this.players.size()) {
            this.currentPlayerIndex = 0;
        }
    }

    public boolean hasPlacedTile() {
        return this.hasPlacedTile;
    }

    public boolean hasPlacedMeeple() {
        return this.hasPlacedMeeple;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }
}
