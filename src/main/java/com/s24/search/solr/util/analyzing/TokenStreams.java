package com.s24.search.solr.util.analyzing;

import com.google.common.io.Closeables;
import org.apache.lucene.analysis.TokenStream;

import java.io.Closeable;
import java.io.IOException;

/**
 * This class is superseeded by the open source version in solr-analyzers.
 *
 * @author Shopping24 GmbH, Torsten Bøgh Köster (@tboeghk)
 */
public class TokenStreams {

    public static void endQuietly(TokenStream tokenStream) {
        if (tokenStream != null) {
            try {
                tokenStream.end();
            } catch (IOException e) {
                // ignored
            }
        }
    }

    /**
     * Unconditionally close a <code>Closeable</code>.
     * <p>
     * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored. This is typically used in finally
     * blocks.
     * <p>
     * Example code:
     * <p>
     * <pre>
     * Closeable closeable = null;
     * try {
     *    closeable = new FileReader(&quot;foo.txt&quot;);
     *    // process closeable
     *    closeable.close();
     * } catch (Exception e) {
     *    // error handling
     * } finally {
     *    IOUtils.closeQuietly(closeable);
     * }
     * </pre>
     *
     * @param closeable the object to close, may be null or already closed
     * @since 2.0
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            Closeables.close(closeable, true);
        } catch (IOException e) {
            // ignored
        }
    }

}
