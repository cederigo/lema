package ch.unibe.lema.provider.filter;

/**
 * Represents a criteria by which lectures may be filtered.
 * 
 * @author bm
 * 
 */

public class FilterCriterion {
  private String key;
  private String value;
  private String description;

  public FilterCriterion(String key, String value, String description) {
    this.key = key;
    this.value = value;
    this.description = description;
  }

  public String getKey() {
    return key;
  }
  
  public String getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }
  
  public String toString() {
    return key + "=" + value;
  }
}
