package cool.solr.search.solr.util.analyzing;

import com.google.common.base.Objects;
import org.apache.lucene.util.Attribute;

import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Representation of a token in the analyzing phase with additional information.
 */
public class Token {

    /**
     * Name of the token.
     */
    private final String name;

    /**
     * Type of the token (e.g. {@link Attribute}).
     */
    private final String type;

    /**
     * Position in the input.
     */
    private final Position position;

    /**
     * Of how many token does this token exists.
     */
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
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("position=" + position)
                .add("tokenCount=" + tokenCount)
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
