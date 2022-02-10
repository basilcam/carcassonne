package net.basilcam.core;

import com.google.common.collect.Lists;

import java.util.*;

public class Player {
    private static final int MEEPLE_PER_PLAYER = 7;

    private final String name;
    private int score;
    private final Meeple[] meeples;

    // todo: i don't like the player owns meeple, and meeple owns player

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.meeples = Player.createMeeples();
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

    public Collection<Meeple> getMeeples() {
        return Arrays.asList(this.meeples);
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

    private static Meeple[] createMeeples() {
        Meeple[] meeples = new Meeple[MEEPLE_PER_PLAYER];
        for (int i = 0; i < MEEPLE_PER_PLAYER; i++) {
            meeples[i] = new Meeple();
        }
        return meeples;
    }
}
