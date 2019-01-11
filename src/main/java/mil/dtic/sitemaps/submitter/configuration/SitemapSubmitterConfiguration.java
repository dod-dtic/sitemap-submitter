package mil.dtic.sitemaps.submitter.configuration;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sitemap.submitter")
public class SitemapSubmitterConfiguration {
    private Map<String, String> engines;

    public Map<String, String> getEngines() {
        return engines;
    }

    public void setEngines(Map<String, String> engines) {
        this.engines = engines;
    }

}