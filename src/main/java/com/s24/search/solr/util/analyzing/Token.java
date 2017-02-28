package com.s24.search.solr.util.analyzing;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Token {
    public static final String MULTIWORD_TOKEN = "multiword";

    private final String name;
    private final String type;
    private final Position position;
    private final Integer tokenCount;

    public Token(String name, Position positionInOriginal, Integer tokenCount) {
        this(name, null, positionInOriginal, tokenCount);
    }

    public Token(String name, String type, Position positionInOriginal, Integer tokenCount) {
        checkNotNull(name, "Pre-condition violated: name must not be null.");
        checkNotNull(positionInOriginal, "Pre-condition violated: positionInOriginal must not be null.");
        checkNotNull(tokenCount, "Pre-condition violated: tokenCount must not be null.");

        this.type = type;
        this.position = positionInOriginal;
        this.tokenCount = tokenCount;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("token", name)
                .add("position", position)
                .add("tokenCount", tokenCount)
                .add("type", type)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, position, tokenCount, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token other = (Token) obj;
            return Objects.equal(other.getName(), getName()) && Objects.equal(other.getPosition(), getPosition()) && Objects.equal(other.getType(), getType());
        }
        return false;
    }
}
