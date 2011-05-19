package ch.unibe.lema.provider;
/**
 * Represents a criteria by which lectures may be filtered.
 * 
 * @author bm
 * 
 */

public class FilterCriterion {
    private String key;
    private String value;
    private String[] suggestions;
    private String description;

    public FilterCriterion(String key, String value,
            String[] suggestions, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.suggestions = suggestions;
    }
    
    public FilterCriterion(String key) {
        this(key,null,new String[]{},"");
    }
    
    public FilterCriterion(String key, String value) {
        this(key,value,new String[]{},"");
    }
    
    public FilterCriterion(String key, String[] suggestions, String description) {
        this(key,null,suggestions,description);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
    public String[] getSuggestions() {
        return suggestions;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return key + "=" + value;
    }
}
