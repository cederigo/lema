package ch.unibe.lema.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a list of criteria by which to filter lectures.
 * 
 * @author bm
 * 
 */
public class Filter implements Iterable<FilterCriterion> {
    private List<FilterCriterion> criteria;

    public Filter() {
        criteria = new ArrayList<FilterCriterion>();
    }
    
    public Filter(List<FilterCriterion> criteria) {
        this.criteria = criteria;
    }

    public void addCriteria(FilterCriterion criterion) {
        criteria.add(criterion);
    }

    public Iterator<FilterCriterion> iterator() {
        return criteria.iterator();
    }
    
    /*match filter objects based on their filtercriteria key=value&key2=value2..*/
    
    public int hashCode() {
        return toString().hashCode();
    }
    
    public boolean equals(Object o) {
        return toString().equals(o.toString());
    }
    
    public String toString() {
        String result = "";
        for(FilterCriterion f : criteria) {
            result += f.toString() + "&";
        }
        return result;
        
    }
    
}
