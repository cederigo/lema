package ch.unibe.ilm.provider;

import java.util.List;

import ch.unibe.ilm.model.Lecture;
import ch.unibe.ilm.provider.filter.Filter;

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
