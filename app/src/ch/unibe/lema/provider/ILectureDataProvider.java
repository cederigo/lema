package ch.unibe.lema.provider;

import java.util.List;

import ch.unibe.lema.LemaException;

public interface ILectureDataProvider {

    /**
     * name is used to identify the implementing provider. typically an
     * university.
     * 
     * @return the name of the lecture data provider
     */
    public String getName();

    public List<Lecture> getLectures(Filter filter) throws LemaException;

    public List<FilterCriterion> getCriteria();

}
