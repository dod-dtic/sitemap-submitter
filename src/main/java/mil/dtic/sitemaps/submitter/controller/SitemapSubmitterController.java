package mil.dtic.sitemaps.submitter.controller;

import mil.dtic.sitemaps.submitter.SitemapSubmitter;
import mil.dtic.sitemaps.submitter.factories.IndexedLocationListFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapSubmitterController {
    @Autowired
    protected SitemapSubmitter sitemapSubmitter;
    
    @Autowired
    protected IndexedLocationListFactory indexedLocationListFactory;
    
    @RequestMapping(value={"/sitemap-submitter"}, method=RequestMethod.GET, produces = "application/json")
    public String[] listEngines() {
    	return sitemapSubmitter.getEngines();
    }
    
}
