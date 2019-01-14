package mil.dtic.sitemaps.submitter;

public class SitemapSubmitterResponse {

    private String name;
    private Boolean success;
    private String message;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
}
