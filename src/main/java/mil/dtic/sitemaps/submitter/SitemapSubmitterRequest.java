package mil.dtic.sitemaps.submitter;

/**
 * Container for data for requesting a sitemap be submitted to sitemap crawlers
 * @author Battelle
 */
public class SitemapSubmitterRequest {

    private String sitemap;
    private String[] crawlers;

    /**
     * 
     * @return URL to sitemap
     */
    public String getSitemap() {
        return this.sitemap;
    }

    public void setSitemap(String sitemap) {
        this.sitemap = sitemap;
    }    

    /**
     * 
     * @return Names of crawlers to submit sitemap to
     */
    public String[] getCrawlers() {
        return this.crawlers;
    }

    public void setCrawlers(String[] crawlers) {
        this.crawlers = crawlers;
    }    
}
