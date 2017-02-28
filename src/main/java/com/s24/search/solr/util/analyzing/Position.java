package com.s24.search.solr.util.analyzing;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Pojo to store advanced information of a entity.
 * E.g. where the position of a entity is in the original query.
 */
public class Position {
    private final Integer start;
    private final Integer end;
    private final Integer wordPosition;

    public Position(Integer start, Integer end, Integer wordPosition) {
        this.start = start;
        this.end = end;
        this.wordPosition = wordPosition;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public Integer getWordPosition() {
        return wordPosition;
    }

    public boolean matches(Position other) {
        checkNotNull(other, "Pre-condition violated: other must not be null.");

        return getStart().equals(other.getStart()) && getEnd().equals(other.getEnd());
    }

    public boolean contains(Position other) {
        checkNotNull(other, "Pre-condition violated: other must not be null.");

        return getStart() <= other.getStart() && getEnd() >= other.getEnd();
    }

    @Override
    public int hashCode() {
        return (start != null ? start.hashCode() : 0) + (end != null ? end.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Position) {
            Position other = (Position) obj;
            return other.getStart().equals(getStart()) && other.getEnd().equals(getEnd());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("start: %d end: %d", start, end);
    }
}
