package mil.dtic.sitemaps.submitter.configuration;

import mil.dtic.sitemaps.submitter.SitemapCrawlerSpecification;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sitemap.submitter")
public class SitemapSubmitterConfiguration {
    private Map<String, SitemapCrawlerSpecification> crawlers;

    public Map<String, SitemapCrawlerSpecification> getCrawlers() {
        return crawlers;
    }

    public void setCrawlers(Map<String, SitemapCrawlerSpecification> crawlers) {
        this.crawlers = crawlers;
    }

    public SitemapCrawlerSpecification getCrawler(String name) {
        SitemapCrawlerSpecification ret = null;
        if (crawlers.containsKey(name)) {
            ret = crawlers.get(name);
        }
        return ret;
    }

}