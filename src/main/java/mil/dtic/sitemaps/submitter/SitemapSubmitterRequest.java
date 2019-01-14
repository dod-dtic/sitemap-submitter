package mil.dtic.sitemaps.submitter;

public class SitemapSubmitterRequest {

    private String sitemap;
    private String[] crawlers;

    public String getSitemap() {
        return this.sitemap;
    }

    public void setSitemap(String sitemap) {
        this.sitemap = sitemap;
    }    

    public String[] getCrawlers() {
        return this.crawlers;
    }

    public void setCrawlers(String[] crawlers) {
        this.crawlers = crawlers;
    }    
}
