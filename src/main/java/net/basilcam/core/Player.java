package net.basilcam.core;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Player {
    private static final int MEEPLE_PER_PLAYER = 7;

    private final String name;
    private int score;
    private final Meeple[] meeples;

    // todo: i don't like the player owns meeple, and meeple owns player

    public static Player createPlayer(String name) {
        Player player = new Player(name);
        player.createMeeples();
        return player;
    }

    public String getName() {
        return this.name;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    public Optional<Meeple> getMeeple() {
        for (Meeple meeple : this.meeples) {
            if (meeple.getTileSection().isEmpty()) {
                return Optional.of(meeple);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return this.score == player.score
                && Objects.equals(this.name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.score);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", meeples=" + Arrays.toString(meeples) +
                '}';
    }

    private Player(String name) {
        this.name = name;
        this.score = 0;
        this.meeples = new Meeple[MEEPLE_PER_PLAYER];
    }

    private void createMeeples() {
        for (int i = 0; i < MEEPLE_PER_PLAYER; i++) {
            this.meeples[i] = new Meeple(this);
        }
    }
}
