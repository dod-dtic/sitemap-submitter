# Sitemap Submitter
This application was built to ping web crawlers to inform them of new or updated sitemaps.

## Build
1. navigate to project root directory, execute:
    ```
    build-dev.sh
    ```

## Debug
1. Install VS Code Extension: Microsoft - Debugger for Java
2. Run tomcat 8.5.* in debug mode with exposed port 8000 to attach
    ```
    $CATALINA_HOME/bin/catalina.sh jpda start
    ```
3. Deploy sitemap-manager.war to tomcat
4. Run VS Code debug

## Configuration
All configuration is stored in the application.properties file, utilized by the SitemapSubmitterConfiguration component

The configuration properties are listed and described here:

- `sitemap.submitter.crawlers.[crawlername].name`
    - `string`: the name of the crawler; probably the same as [crawlername]

- `sitemap.submitter.crawlers.[crawlername].urlTemplate`
    - `string`: the URL template to use for submitting index requests for this crawler; this should contain a %s format specifier which will be replaced with the URL for the sitemap

These two properties should occur together and should be repeated for each [crawlername].

## Usage
### Get Crawlers (detailed)
To list the crawlers defined for the service, issue a `GET` request against `/crawlers`. The response will be in JSON format and will contain an string array of crawler names.

### Get Crawler Specification
To retrive the specification for a crawler, issue a `GET` request against `/crawlers/[crawlername]`. The response will be in JSON format and will contain an object containing the name (string) and urlTemplate (string).

### Submit a Sitemap for Indexing to One or More Crawlers
To submit a sitemap for indexing to one or more crawlers, issue a `POST` request against `/indexrequests` with an object structure as described:
```json
{
    "sitemap" : "URL to the sitemap",
    "crawlers": [ "crawlername1", "crawlername2", ... ]
}
```

Example:
```json
{
    "sitemap":"http://www.sitemap.org/sitemap.xml",
    "crawlers": [ "google", "bing" ]
}
```

The response will be in JSON format and will contain an array of response objects (one for each crawler). Each repsonse object will contain the fields name (string, the name of the crawler), 
success (Boolean, whether the request for that crawler succeeded), and message (string, additional information about the request). 

The HTTP status code for the response will be 200 is any of the 
crawlers requests succeeded (you will still need to inspect the responses to see if all requests succeeded), or 500 if all failed.