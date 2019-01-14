package mil.dtic.sitemaps.submitter;

public class SitemapCrawlerSpecification {

    private String name;
    private String urltemplate;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlTemplate() {
        return this.urltemplate;
    }

    public void setUrlTemplate(String urltemplate) {
        this.urltemplate = urltemplate;
    }    
}
