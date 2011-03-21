package ch.unibe.lema.provider;

import java.util.List;

import ch.unibe.lema.LemaException;
import ch.unibe.lema.model.Lecture;
import ch.unibe.lema.provider.filter.Filter;
import ch.unibe.lema.provider.filter.FilterCriterion;

public interface ILectureDataProvider {

    /**
     * name is used to identify the implementing provider. typically an
     * university.
     * 
     * @return the name of the lecture data provider
     */
    public String getName();

    public List<Lecture> getLectures(Filter filter) throws LemaException;

    public List<FilterCriterion> getCriterias();

}
