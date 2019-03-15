package mil.dtic.sitemaps.submitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mil.dtic.sitemaps.submitter.configuration.SitemapSubmitterConfiguration;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 *
 */
@SpringBootTest(classes = SitemapSubmitterApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class IntegrationTests {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	private static final String crawlerEndpoint = "/crawlers";
	private static final String submitSitemapEndpoint = "/indexrequests";
	@Autowired
	private SitemapSubmitterConfiguration config;
	private static final String errorMessage = "an error has occurred";

	// This requires you to have a sitemap in the test-application.properties with the name "google".
	// Couldn't figure out a more testable way to do this.
	private static final String validCrawlerName = "google";
	private static final String validCrawlerNameAlt = "bing";
	private static final String invalidCrawlerName = "foobar";
	
	public IntegrationTests() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@After
	public void tearDown() {
	}

	private MockHttpServletRequestBuilder standardRequest(HttpMethod method, String endpoint) throws Exception {
		return request(method, endpoint)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);
	}

	/**
	 * Possibly not large enough to justify itself.
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private ResultActions standardValidExpect(ResultActions response) throws Exception {
		return response.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	private ResultActions standardSubmitSitemapExpect(ResultActions response, List<String> names) throws Exception {
		return response.andExpect(jsonPath("$.responses[*]", hasSize(names.size())))
			.andExpect(jsonPath("$.responses[*].success", everyItem(equalTo(true))))
			.andExpect(jsonPath("$.responses[*].message", everyItem(equalTo("Succeeded"))))
		        .andExpect(jsonPath("$.responses[*].name", containsInAnyOrder(names.toArray())));
	}

	@Test
	public void testGetCrawlers() throws Exception {
		List<String> names = new ArrayList<>();
		config.getCrawlers().values().forEach((crawler) -> {
			names.add(crawler.getName());
		});

		ResultActions response = mockMvc.perform(standardRequest(HttpMethod.GET, crawlerEndpoint));
		standardValidExpect(response)
			.andExpect(jsonPath("$.crawlers[*]", containsInAnyOrder(names.toArray())));
	}

	@Test
	public void testGetCrawlerSpecification() throws Exception {
		ResultActions response = mockMvc.perform(standardRequest(HttpMethod.GET, crawlerEndpoint + "/" + validCrawlerName));
		standardValidExpect(response)
			.andExpect(jsonPath("$.name").value(config.getCrawler(validCrawlerName).getName()))
			.andExpect(jsonPath("$.urlTemplate").value(config.getCrawler(validCrawlerName).getUrlTemplate()));
	}

	@Test
	public void testGetCrawlerSpecificationAlt() throws Exception {
		ResultActions response = mockMvc.perform(standardRequest(HttpMethod.GET, crawlerEndpoint + "/" + validCrawlerNameAlt));
		standardValidExpect(response)
			.andExpect(jsonPath("$.name").value(config.getCrawler(validCrawlerNameAlt).getName()))
			.andExpect(jsonPath("$.urlTemplate").value(config.getCrawler(validCrawlerNameAlt).getUrlTemplate()));
	}

	/**
	 * Determine if there's anything else this should check.
	 * Add this back in when the tests are done.
	 * @throws Exception 
	 */
	@Test
	public void testGetInvalidCrawlerSpecification() throws Exception {
		ResultActions response = mockMvc.perform(standardRequest(HttpMethod.GET, crawlerEndpoint + "/" + invalidCrawlerName));
		response.andExpect(status().isBadRequest());
	}

	@Test
	public void testSubmitEmpty() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);
		ResultActions response = mockMvc.perform(request);
		//response.andExpect(status().isBadRequest())
		response.andExpect(status().isInternalServerError())
			.andExpect(MockMvcResultMatchers.content().string(errorMessage));
	}

	@Test
	public void testSubmitEmptyCrawlers() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.example.com");
		JSONArray crawlers = new JSONArray();
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);
		response.andExpect(jsonPath("$.responses[*]", hasSize(0)));
	}

	@Test
	public void testSubmitSingle() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.example.com");
		JSONArray crawlers = new JSONArray();
		crawlers.put(validCrawlerName);
		content.put("crawlers", crawlers);

		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);

		List<String> names = Arrays.asList(validCrawlerName);
		standardSubmitSitemapExpect(response, names);
	}

	@Test
	public void testSubmitSingleAlt() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.battelle.org");
		JSONArray crawlers = new JSONArray();
		crawlers.put(validCrawlerNameAlt);
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);
		List<String> names = Arrays.asList(validCrawlerNameAlt);
		standardSubmitSitemapExpect(response, names);
	}

	@Test
	public void testSubmitMultiple() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.battelle.org");
		JSONArray crawlers = new JSONArray();
		crawlers.put(validCrawlerName);
		crawlers.put(validCrawlerNameAlt);
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);
		List<String> names = Arrays.asList(validCrawlerName, validCrawlerNameAlt);
		standardSubmitSitemapExpect(response, names);
	}

	@Test
	public void testBlankSitemapUrl() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		JSONArray crawlers = new JSONArray();
		crawlers.put(validCrawlerName);
		crawlers.put(validCrawlerNameAlt);
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);

		response.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		List<String> names = Arrays.asList(validCrawlerName, validCrawlerNameAlt);
		response.andExpect(jsonPath("$.responses[*]", hasSize(names.size())))
			.andExpect(jsonPath("$.responses[*].success", everyItem(equalTo(false))))
			.andExpect(jsonPath("$.responses[*].message", everyItem(equalTo("Failure: Invalid URL"))))
		        .andExpect(jsonPath("$.responses[*].name", containsInAnyOrder(names.toArray())));
	}

	@Test
	public void testInvalidSitemapUrl() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "htp:/wwwexamplecom.....");
		JSONArray crawlers = new JSONArray();
		crawlers.put(validCrawlerName);
		crawlers.put(validCrawlerNameAlt);
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);

		response.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		List<String> names = Arrays.asList(validCrawlerName, validCrawlerNameAlt);
		response.andExpect(jsonPath("$.responses[*]", hasSize(names.size())))
			.andExpect(jsonPath("$.responses[*].success", everyItem(equalTo(false))))
			.andExpect(jsonPath("$.responses[*].message", everyItem(equalTo("Failure: Invalid URL"))))
		        .andExpect(jsonPath("$.responses[*].name", containsInAnyOrder(names.toArray())));
	}

	@Test
	public void testInvalidCrawlerName() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.example.com");
		JSONArray crawlers = new JSONArray();
		crawlers.put(invalidCrawlerName);
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);

		response.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		List<String> names = Arrays.asList(invalidCrawlerName);

		response.andExpect(jsonPath("$.responses[*]", hasSize(1)))
			.andExpect(jsonPath("$.responses[*].success", everyItem(equalTo(false))))
			.andExpect(jsonPath("$.responses[*].message", everyItem(equalTo("Failed: Crawler Not Found"))))
		        .andExpect(jsonPath("$.responses[*].name", containsInAnyOrder(names.toArray())));
	}

	@Test
	public void testMultipleInvalidCrawlerName() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.example.com");
		JSONArray crawlers = new JSONArray();
		crawlers.put(invalidCrawlerName);
		crawlers.put("Lorem");
		crawlers.put("Ipsum");
		content.put("crawlers", crawlers);
		request.content(content.toString());

		ResultActions response = mockMvc.perform(request);
		response.andExpect(status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		List<String> names = Arrays.asList(invalidCrawlerName, "Lorem", "Ipsum");

		response.andExpect(jsonPath("$.responses[*]", hasSize(3)))
			.andExpect(jsonPath("$.responses[*].success", everyItem(equalTo(false))))
			.andExpect(jsonPath("$.responses[*].message", everyItem(equalTo("Failed: Crawler Not Found"))))
		        .andExpect(jsonPath("$.responses[*].name", containsInAnyOrder(names.toArray())));
	}

	@Test
	public void testMixedInvalidCrawlerName() throws Exception {
		MockHttpServletRequestBuilder request = standardRequest(HttpMethod.POST, submitSitemapEndpoint);

		JSONObject content = new JSONObject();
		content.put("sitemap", "http://www.example.com");
		JSONArray crawlers = new JSONArray();
		crawlers.put(validCrawlerName);
		crawlers.put(invalidCrawlerName);
		crawlers.put(validCrawlerNameAlt);
		crawlers.put("Lorem");
		content.put("crawlers", crawlers);
		request.content(content.toString());

		List<String> names = Arrays.asList(validCrawlerName, validCrawlerNameAlt, invalidCrawlerName, "Lorem");
		ResultActions response = mockMvc.perform(request);
		response.andDo(print());

		standardValidExpect(response);

		response.andExpect(jsonPath("$.responses[*]", hasSize(4)))
		        .andExpect(jsonPath("$.responses[*].name", containsInAnyOrder(names.toArray())))
			.andExpect(jsonPath("$.responses[?(@.name == '" + validCrawlerName + "')].success").value(true))
			.andExpect(jsonPath("$.responses[?(@.name == '" + validCrawlerName + "')].message").value("Succeeded"))
			.andExpect(jsonPath("$.responses[?(@.name == '" + invalidCrawlerName + "')].success").value(false))
			.andExpect(jsonPath("$.responses[?(@.name == '" + invalidCrawlerName + "')].message").value("Failed: Crawler Not Found"))
			.andExpect(jsonPath("$.responses[?(@.name == '" + validCrawlerNameAlt + "')].success").value(true))
			.andExpect(jsonPath("$.responses[?(@.name == '" + validCrawlerNameAlt + "')].message").value("Succeeded"))
			.andExpect(jsonPath("$.responses[?(@.name == 'Lorem')].success").value(false))
			.andExpect(jsonPath("$.responses[?(@.name == 'Lorem')].message").value("Failed: Crawler Not Found"))
			;
	}
}