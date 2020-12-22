package cool.solr.search.component.solr.query;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.search.SolrIndexSearcher;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This request can be used inside a solr component to fire internal subrequest.
 * Those are aware of any cache-warming. If cache-warming is in progress, the
 * searcher to warm will be used instead of the to be replaced searcher.
 */
public class SearcherAwareLocalSolrQueryRequest extends LocalSolrQueryRequest {
    /**
     * Searcher.
     */
    private SolrIndexSearcher searcher;

    /**
     * {@inheritDoc}
     * <p>
     * If <code>args</code> contains a <code>event</code> parameter, a latest
     * cold searcher with it's connected caches will be used for requesting.
     */
    public SearcherAwareLocalSolrQueryRequest(SolrIndexSearcher searcher, NamedList<?> args) {
        super(checkNotNull(searcher).getCore(), args);

        this.searcher = searcher;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If <code>args</code> contains a <code>event</code> parameter, a latest
     * cold searcher with it's connected caches will be used for requesting.
     */
    public SearcherAwareLocalSolrQueryRequest(SolrIndexSearcher searcher, SolrParams args) {
        super(checkNotNull(searcher).getCore(), args);

        this.searcher = searcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SolrIndexSearcher getSearcher() {
        return searcher;
    }

    @Override
    public void close() {
        super.close();

        searcher = null;
    }
}