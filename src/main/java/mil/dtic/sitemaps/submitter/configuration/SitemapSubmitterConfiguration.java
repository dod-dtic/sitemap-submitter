package mil.dtic.sitemaps.submitter.configuration;

import mil.dtic.sitemaps.submitter.SitemapCrawlerSpecification;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Utility for getting sitemap crawlers
 * @author Battelle
 */
@Component
@ConfigurationProperties("sitemap.submitter")
public class SitemapSubmitterConfiguration {
    private Map<String, SitemapCrawlerSpecification> crawlers;

    /**
     * 
     * @return Map of crawler names to crawler specifications
     */
    public Map<String, SitemapCrawlerSpecification> getCrawlers() {
        return crawlers;
    }

    public void setCrawlers(Map<String, SitemapCrawlerSpecification> crawlers) {
        this.crawlers = crawlers;
    }

    /**
     * 
     * @param name Name of crawler
     * @return Specification for crawler with given name
     */
    public SitemapCrawlerSpecification getCrawler(String name) {
        SitemapCrawlerSpecification ret = null;
        if (crawlers.containsKey(name)) {
            ret = crawlers.get(name);
        }
        return ret;
    }

}