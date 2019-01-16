package mil.dtic.sitemaps.submitter.controller;

import mil.dtic.sitemaps.submitter.SitemapCrawlerSpecification;
import mil.dtic.sitemaps.submitter.SitemapSubmitter;
import mil.dtic.sitemaps.submitter.SitemapSubmitterRequest;
import mil.dtic.sitemaps.submitter.SitemapSubmitterResponse;
import mil.dtic.sitemaps.submitter.SitemapSubmitterResponses;
import mil.dtic.sitemaps.submitter.factories.IndexedLocationListFactory;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapSubmitterController {
    @Autowired
    protected SitemapSubmitter sitemapSubmitter;
    
    @Autowired
    protected IndexedLocationListFactory indexedLocationListFactory;
    
    @RequestMapping(value={"/crawlers"}, method=RequestMethod.GET, produces = "application/json")
    public Map<String, String[]> getCrawlers() {
    	return Collections.singletonMap("crawlers", sitemapSubmitter.getCrawlers());
    }
    
    @RequestMapping(value={"/crawlers/{name}"}, method=RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SitemapCrawlerSpecification> getCrawler(@PathVariable("name") String name) {
        SitemapCrawlerSpecification ret = sitemapSubmitter.getCrawler(name);
        if (ret == null) {
            return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @RequestMapping(value={"/indexrequests"}, method=RequestMethod.POST, produces = "application/json")
    public ResponseEntity<SitemapSubmitterResponses> submitIndexRequest(@RequestBody SitemapSubmitterRequest request) {

        SitemapSubmitterResponse[] responses = sitemapSubmitter.submitIndexRequests(request.getSitemap(), request.getCrawlers());
        Boolean atLeastOneSucceeded = false;
        for (SitemapSubmitterResponse response : responses) {
            atLeastOneSucceeded = atLeastOneSucceeded || response.getSuccess();
        }
        SitemapSubmitterResponses ret = new SitemapSubmitterResponses();
        ret.setResponses(responses);
        if (atLeastOneSucceeded) {
        	return new ResponseEntity<>(ret, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    	}
    }
    
}
