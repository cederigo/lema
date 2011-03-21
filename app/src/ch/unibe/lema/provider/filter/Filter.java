package ch.unibe.lema.provider.filter;

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
}
