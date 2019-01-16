package mil.dtic.sitemaps.submitter;

/**
 * Container for sitemap crawler specifications
 * @author Battelle
 */
public class SitemapCrawlerSpecification {

    private String name;
    private String urltemplate;

    /**
     * 
     * @return Name of crawler
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return URL template for submitting sitemap to crawler
     */
    public String getUrlTemplate() {
        return this.urltemplate;
    }

    public void setUrlTemplate(String urltemplate) {
        this.urltemplate = urltemplate;
    }    
}
