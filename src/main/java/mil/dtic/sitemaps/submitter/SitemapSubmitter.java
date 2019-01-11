package mil.dtic.sitemaps.submitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.dtic.sitemaps.submitter.configuration.SitemapSubmitterConfiguration;

@Component
public class SitemapSubmitter {
    @Autowired
    protected SitemapSubmitterConfiguration configuration;
    
    public String[] getEngines() {
    	return configuration.getEngines().keySet().toArray(new String[0]);
    }
    
}
