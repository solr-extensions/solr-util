package cool.solr.search.component.solr.util;

import com.google.common.collect.Sets;
import cool.solr.search.component.solr.query.SearcherAwareLocalSolrQueryRequest;
import org.apache.solr.common.params.*;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.FacetComponent;
import org.apache.solr.handler.component.QueryComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.response.SolrQueryResponse;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builder for a Solr response using a local Solr request.
 * <p>
 * Always use inside try with resources!
 */
public class SolrLocalResponseBuilder implements Closeable {
    /**
     * Originating request.
     */
    private final ResponseBuilder origin;

    /**
     * Search components to use.
     */
    private final List<SearchComponent> components = new ArrayList<>();

    /**
     * Parameters to use.
     */
    private SolrParams params;

    /**
     * The request.
     */
    private SearcherAwareLocalSolrQueryRequest request;

    /**
     * Create local request builder from originating request.
     * Uses query component only.
     *
     * @param origin Originating request.
     */
    public SolrLocalResponseBuilder(ResponseBuilder origin) {
        this.origin = checkNotNull(origin);
        components.add(extractQueryComponent(origin.components));
    }

    @Override
    public void close() {
        if (request != null) {
            request.close();
        }
        components.clear();
    }

    /**
     * This takes the original incoming solr params and removes all scoring,
     * boosting, grouping, collapsing and expanding params. It furthermore
     * disables all spellchecking and query reductions.
     *
     * @return this for method chaining.
     */
    public SolrLocalResponseBuilder useUnscoredSolrParams() {
        params = computeUnscoredSolrParams(origin.req.getParams());
        return this;
    }

    /**
     * Use the given parameters for the request.
     *
     * @return this for method chaining.
     */
    public SolrLocalResponseBuilder useParams(SolrParams params) {
        this.params = params;
        return this;
    }

    /**
     * Use facet component too.
     *
     * @return this for method chaining.
     */
    public SolrLocalResponseBuilder useFacetComponent() {
        components.add(extractFacetComponent(origin.components));
        return this;
    }

    /**
     * Create a response builder.
     */
    public ResponseBuilder build() {
        checkNotNull(params, "Pre-condition violated: params must not be null.");
        checkArgument(!components.isEmpty(), "Pre-condition violated: expression !components.isEmpty() must be true.");

        request = new SearcherAwareLocalSolrQueryRequest(origin.req.getSearcher(), params);

        SolrQueryResponse checkResponse = new SolrQueryResponse();
        SolrCore.preDecorateResponse(request, checkResponse);

        ResponseBuilder response = new ResponseBuilder(request, checkResponse, components);
        response.doExpand = false;
        response.doHighlights = false;
        response.doStats = false;
        response.doTerms = false;

        return response;
    }

    /**
     * Execute request by calling the search components directly.
     */
    public static void execute(ResponseBuilder response) throws IOException {
        for (SearchComponent component : response.components) {
            component.prepare(response);
        }
        for (SearchComponent component : response.components) {
            component.process(response);
        }
    }

    //
    // Helper
    //

    /**
     * This takes the original incoming solr params and creates new solr query params
     * that contain query and filter query params only. Sort params are replaced in order
     * to reflect unscored document retrieval.
     * <p>
     * Use these params on a subset of search components like the query and facet
     * component only. With this shorter query params all clutter is removed from
     * the query.
     */
    public static ModifiableSolrParams computeUnscoredSolrQueryParams(SolrParams original) {
        checkNotNull(original, "Pre-condition violated: original must not be null.");

        ModifiableSolrParams params = new ModifiableSolrParams();

        // transfer minimum query
        params.set(CommonParams.Q, original.get(CommonParams.Q));
        params.set(DisMaxParams.QF, original.get(DisMaxParams.QF));
        params.set("defType", original.get("defType"));
        params.set(DisMaxParams.MM, original.get(DisMaxParams.MM));

        // add fqs
        if (original.getParams(CommonParams.FQ) != null) {
            for (String fq : original.getParams(CommonParams.FQ)) {
                params.add(CommonParams.FQ, fq);
            }
        }

        // remove sorting
        params.set(CommonParams.ROWS, 0);
        params.set(CommonParams.SORT, "_docid_ asc");
        params.set(CommonParams.FL, "_docid_");

        return params;
    }

    /**
     * This takes the original incoming solr params and removes all scoring,
     * boosting, grouping, collapsing and expanding params. It furthermore
     * disables all spellchecking and query reductions.
     */
    public static ModifiableSolrParams computeUnscoredSolrParams(SolrParams origParams) {
        checkNotNull(origParams, "Pre-condition violated: origParams must not be null.");

        ModifiableSolrParams params = new ModifiableSolrParams(origParams);
        params.remove(CommonParams.START);
        params.set(CommonParams.FL, "id");
        params.set(CommonParams.ROWS, "0");
        params.remove(CommonParams.DEBUG);
        params.remove(CommonParams.HEADER_ECHO_PARAMS);
        params.remove(GroupParams.GROUP);

        // remove boosting
        params.remove(DisMaxParams.BF);
        params.remove(DisMaxParams.BQ);
        params.remove("boost");
        params.remove("rq");
        params.remove("rrq");

        // remove phrase boosting
        params.remove(DisMaxParams.PF);
        params.remove(DisMaxParams.PF2);
        params.remove(DisMaxParams.PF3);
        params.remove(DisMaxParams.PS);
        params.remove(DisMaxParams.PS2);
        params.remove(DisMaxParams.PS3);

        // disable elevation
        params.set("enableElevation", "false");
        params.set("forceElevation", "false");

        // disable spellcheck & facetting
        params.set("spellcheck", "false");
        params.set(FacetParams.FACET, "false");
        params.set("collapse", "false");
        params.set("collapse.enable", "false");
        params.set("expand", "false");
        params.set("expand.append", "false");
        params.set("ranking.barkeeper", "false");
        params.set("facet.cache.static", "false");
        params.set("boost.cache", "false");
        params.set("bq.cache", "false");

        // remove sorting
        params.remove(CommonParams.SORT);
        params.set(CommonParams.SORT, "_docid_ asc");

        // remove waste
        Set<String> waste = Sets.newHashSet(params.getParameterNames());
        for (String param : waste) {
            if (param.startsWith("spellcheck.")) {
                params.remove(param);
            } else if (param.startsWith("bmax.")) {
                params.remove(param);
            } else if (param.startsWith("boost.")) {
                params.remove(param);
            }
        }

        return params;
    }

    /**
     * Returns the queryComponent in components and null if no QueryComponent is
     * present
     */
    public static QueryComponent extractQueryComponent(List<SearchComponent> components) {
        QueryComponent queryComponent = null;
        if (components != null) {
            for (SearchComponent sc : components) {
                if (sc instanceof QueryComponent) {
                    queryComponent = (QueryComponent) sc;
                    break;
                }
            }
        }

        return queryComponent;
    }

    /**
     * Returns the facetComponent in components and null if no FacetComponent is
     * present
     */
    public static FacetComponent extractFacetComponent(List<SearchComponent> components) {
        FacetComponent facetComponent = null;
        if (components != null) {
            for (SearchComponent sc : components) {
                if (sc instanceof FacetComponent) {
                    facetComponent = (FacetComponent) sc;
                    break;
                }
            }
        }

        return facetComponent;
    }

}
