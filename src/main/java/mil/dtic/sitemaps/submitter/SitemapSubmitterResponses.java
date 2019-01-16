package mil.dtic.sitemaps.submitter;

/**
 * Container for responses from requesting sitemaps be submitted to sitemap crawlers
 * @author Battelle
 */
public class SitemapSubmitterResponses {

    private SitemapSubmitterResponse[] responses;

    /**
     * 
     * @return List of responses from sitemap submission requests
     */
    public SitemapSubmitterResponse[] getResponses() {
        return this.responses;
    }

    public void setResponses(SitemapSubmitterResponse[] responses) {
        this.responses = responses;
    }    
}
