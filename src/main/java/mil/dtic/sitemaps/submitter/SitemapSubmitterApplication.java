package mil.dtic.sitemaps.submitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SitemapSubmitterApplication extends SpringBootServletInitializer {

	private static Class<SitemapSubmitterApplication> applicationClass = SitemapSubmitterApplication.class;

    /**
     * Entry point into application
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		SpringApplication.run(SitemapSubmitterApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

}

