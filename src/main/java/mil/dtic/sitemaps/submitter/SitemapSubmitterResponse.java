package mil.dtic.sitemaps.submitter;

/**
 * Container for response data, for sitemap submission requests
 * @author Battelle
 */
public class SitemapSubmitterResponse {

    private String name;
    private Boolean success;
    private String message;

    /**
     * 
     * @return Name of crawler submitted to
     */
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }    
    
    /**
     * 
     * @return True iff successfully submitted sitemap to crawler
     */
    public Boolean getSuccess() {
        return this.success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 
     * @return Details relating to response, particularly regarding failed submissions
     */
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }    
}
