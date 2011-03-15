package ch.unibe.lema.provider;

import java.util.List;

import ch.unibe.lema.model.Lecture;
import ch.unibe.lema.provider.filter.Filter;

public interface ILectureDataProvider {

    /**
     * name is used to identify the implementing provider. typically an
     * university.
     * 
     * @return the name of the lecture data provider
     */
    public String getName();

    public List<Lecture> getLectures(Filter filter);

    public Filter getCriterias();

}
