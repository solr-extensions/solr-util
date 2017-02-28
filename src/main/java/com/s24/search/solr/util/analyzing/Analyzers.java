package com.s24.search.solr.util.analyzing;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Analyzer utilities. This class is superseeded by the open source version in solr-analyzers.
 *
 * @see TokenStream
 */
public class Analyzers {

    private Analyzers() {
    }

    /**
     * Runs an input string through the given analyzer.
     *
     * @param analyzer  Analyzer to be used.
     * @param input     Input to analyze.
     * @param separator Split the input by this separator.
     */
    public static String analyze(Analyzer analyzer, String input, String separator) throws IOException {
        checkNotNull(separator);

        return Joiner.on(separator).skipNulls().join(analyze(analyzer, input));
    }

    /**
     * Runs an input string through the given analyzer.
     *
     * @param analyzer Analyzer to be used.
     * @param input    Input to analyze.
     * @return A list of strings
     */
    public static List<String> analyze(Analyzer analyzer, String input) throws IOException {
        checkNotNull(analyzer);
        checkNotNull(input);

        List<String> result = Lists.newArrayList();

        // get analyzed tokens
        StringReader reader = new StringReader(input);
        TokenStream tokenStream = analyzer.tokenStream("analyzer", reader);
        try {
            // reset stream
            tokenStream.reset();

            // get attributes
            CharTermAttribute charAttr = tokenStream.addAttribute(CharTermAttribute.class);

            // read into candidate map
            while (tokenStream.incrementToken()) {
                String value = StringUtils.trimToNull(charAttr.toString());

                if (value != null) {
                    result.add(value);
                }
            }
        } finally {
            TokenStreams.endQuietly(tokenStream);
            TokenStreams.closeQuietly(tokenStream);
            TokenStreams.closeQuietly(reader);
        }

        return result;
    }

    /**
     * Runs an input string through the given analyzer and returns most available
     * attributes extracted.
     *
     * @param analyzer Analyzer to be used.
     * @param input    Input to analyze.
     * @return A list of {@link Token}
     */
    public static List<Token> analyzeTokens(Analyzer analyzer, String input) throws IOException {
        checkNotNull(analyzer);
        checkNotNull(input);

        List<Token> result = Lists.newArrayList();

        // get analyzed tokens
        StringReader reader = new StringReader(input);
        TokenStream tokenStream = analyzer.tokenStream("analyzer", reader);
        try {
            // reset stream
            tokenStream.reset();
            int wordPosition = 0;

            // register attributes
            CharTermAttribute charAttr = tokenStream.addAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            PositionLengthAttribute positionLength = tokenStream.addAttribute(PositionLengthAttribute.class);
            PositionIncrementAttribute positionIncrementAttribute = tokenStream
                    .addAttribute(PositionIncrementAttribute.class);
            TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);

            // read into candidate map
            while (tokenStream.incrementToken()) {
                wordPosition += positionIncrementAttribute.getPositionIncrement();
                result.add(new Token(charAttr.toString(), typeAttribute.type(), new Position(offsetAttribute.startOffset(),
                        offsetAttribute
                                .endOffset(), wordPosition - 1), positionLength.getPositionLength()));
            }
        } finally {
            TokenStreams.endQuietly(tokenStream);
            TokenStreams.closeQuietly(tokenStream);
            TokenStreams.closeQuietly(reader);
        }

        return result;
    }
}
