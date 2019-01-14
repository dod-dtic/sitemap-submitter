package mil.dtic.sitemaps.submitter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SitemapSubmitterTest {

    @Autowired
    protected SitemapSubmitter sitemapSubmitter;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void before() {
	}
	
	@Test
	public void testGetCrawlers() {
		String[] crawlers = sitemapSubmitter.getCrawlers();
		assertEquals("Expected number of crawlers to be 2", 2, crawlers.length);
	}
	
	@Test
	public void testGetCrawler() {
		SitemapCrawlerSpecification crawler = sitemapSubmitter.getCrawler("google");
		assertNotNull(crawler);
		assertEquals("Expected crawler name to be 'google'", "google", crawler.getName());
	}
	
	@Test
	public void testsubmitIndexRequests() {
		SitemapSubmitterResponse[] responses = sitemapSubmitter.submitIndexRequests("http://www.sitemaps.org/sitemap.xml", new String[]{"google", "bing"});
		assertNotNull(responses);
		assertEquals("Expected number of repsonses to be 2", 2, responses.length);
		assertEquals("Expected first response to be for google", "google", responses[0].getName());
		assertTrue("Expected first response to indicate success", responses[0].getSuccess());
		assertEquals("Expected second response to be for bing", "bing", responses[1].getName());
		assertTrue("Expected second response to indicate success", responses[1].getSuccess());
	}
	
}
