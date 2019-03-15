package mil.dtic.sitemaps.submitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.dtic.sitemaps.submitter.configuration.SitemapSubmitterConfiguration;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Utility for getting crawler names and crawler specifications, and for
 * submitting sitemaps to crawlers.
 * @author Battelle
 */
@Component
public class SitemapSubmitter {
    @Autowired
    protected SitemapSubmitterConfiguration configuration;
    
    /**
     * 
     * @return Names of all sitemap crawlers
     */
    public String[] getCrawlers() {
    	return configuration.getCrawlers().keySet().toArray(new String[0]);
    }

    /**
     * 
     * @param name Name of sitemap crawler
     * @return Specification for sitemap crawler with given name, else null if not found
     */
    public SitemapCrawlerSpecification getCrawler(String name) {
        SitemapCrawlerSpecification ret = configuration.getCrawler(name);
        return ret;
    }
    
    /**
     * Submit sitemap URL to sitemap crawler
     * @param sitemap URL to sitemap to submit
     * @param crawlers List of crawler names to submit to
     * @return 
     */
    public SitemapSubmitterResponse[] submitIndexRequests(String sitemap, String[] crawlers) {
        List<SitemapSubmitterResponse> responses = new ArrayList<>();
        if (crawlers != null) {
            for (String crawler : crawlers) {
                SitemapCrawlerSpecification spec = configuration.getCrawler(crawler);
                if (spec == null) {
                    SitemapSubmitterResponse response = new SitemapSubmitterResponse();
                    response.setName(crawler);
                    response.setSuccess(false);
                    response.setName(crawler);
                    response.setMessage("Failed: Crawler Not Found");
                    responses.add(response);
                } else {
                    SitemapSubmitterResponse response = sendGetRequest(spec, sitemap);
                    responses.add(response);
                }
            }
        }
        return responses.toArray(new SitemapSubmitterResponse[0]);
    }

    /**
     * Submit sitemap URL to sitemap crawler
     * @param spec Sitemap crawler specification to submit sitemap to
     * @param sitemap URL to sitemap to submit
     * @return Result from request
     */
    private SitemapSubmitterResponse sendGetRequest(SitemapCrawlerSpecification spec, String sitemap) {
        String urlTemplate = spec.getUrlTemplate();
        String urlString = String.format(urlTemplate, sitemap);
        URL url = null;
        SitemapSubmitterResponse ret = new SitemapSubmitterResponse();
        try {
            URL sitemapUrl = new URL(sitemap);
            url = new URL(urlString);
        }
        catch (MalformedURLException ex) {
            ret.setName(spec.getName());
            ret.setSuccess(false);
            ret.setMessage("Failure: Invalid URL");
            return ret;
        }
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.getInputStream();
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode == 200) {
                ret.setName(spec.getName());
                ret.setSuccess(true);
                ret.setMessage("Succeeded");
            } else {
                ret.setName(spec.getName());
                ret.setSuccess(false);
                ret.setMessage(String.format("Failed: HTTP Request Failed with response %d", httpStatusCode));
            }
        }
        catch (IOException ex) {
            ret.setName(spec.getName());
            ret.setSuccess(false);
            ret.setMessage("Failure: IO Exception");
            return ret;
        }
        return ret;
    }
    
}
