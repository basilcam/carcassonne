package net.basilcam.core.tiles;

import com.google.common.collect.ImmutableList;
import net.basilcam.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private TileSection topSection;
    private TileSection leftSection;
    private TileSection bottomSection;
    private TileSection rightSection;
    private final ImmutableList<TileSection> centerSections;

    private Tile(TileSection topSection,
                TileSection leftSection,
                TileSection bottomSection,
                TileSection rightSection,
                ImmutableList<TileSection> centerSections) {
        this.topSection = topSection;
        this.leftSection = leftSection;
        this.bottomSection = bottomSection;
        this.rightSection = rightSection;
        this.centerSections = centerSections;
    }

    public TileSection getSection(Direction direction) {
        switch (direction) {
            case UP:
                return getTopSection();
            case LEFT:
                return getLeftSection();
            case DOWN:
                return getBottomSection();
            case RIGHT:
                return getRightSection();
            default:
                throw new IllegalStateException();

        }
    }

    public TileSection getTopSection() {
        return topSection;
    }

    public TileSection getLeftSection() {
        return leftSection;
    }

    public TileSection getBottomSection() {
        return bottomSection;
    }

    public TileSection getRightSection() {
        return rightSection;
    }

    public ImmutableList<TileSection> getCenterSections() {
        return this.centerSections;
    }

    public void rotateClockwise() {
        TileSection tempTop = this.topSection;
        TileSection tempLeft = this.leftSection;
        TileSection tempBottom = this.bottomSection;
        TileSection tempRight = this.rightSection;

        this.rightSection = tempTop;
        this.bottomSection = tempRight;
        this.leftSection = tempBottom;
        this.topSection = tempLeft;
    }

    public static class Builder {
        private TileSection topSection;
        private TileSection leftSection;
        private TileSection bottomSection;
        private TileSection rightSection;
        private final List<TileSection> centerSections;

        Builder() {
            this.centerSections = new ArrayList<>();
        }

        Builder withTop(TileSection section) {
            this.topSection = section;
            return this;
        }

        Builder withLeft(TileSection section) {
            this.leftSection = section;
            return this;
        }

        Builder withBottom(TileSection section) {
            this.bottomSection = section;
            return this;
        }

        Builder withRight(TileSection section) {
            this.rightSection = section;
            return this;
        }

        Builder addCenter(TileSection section) {
            this.centerSections.add(section);
            return this;
        }

        Tile build() {
            return new Tile(this.topSection,
                    this.leftSection,
                    this.bottomSection,
                    this.rightSection,
                    ImmutableList.copyOf(this.centerSections));
        }

    }
}
