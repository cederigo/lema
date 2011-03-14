package ch.unibe.ilm.provider;

import java.util.List;

import ch.unibe.ilm.model.Department;
import ch.unibe.ilm.model.Faculty;
import ch.unibe.ilm.model.Lecture;

public interface ILectureDataProvider {

  /**
   * name is used to identify the implementing provider. typically an
   * university.
   * 
   * @return the name of the lecture data provider
   */
  public String getName();

  public List<Faculty> getFaculties();

  public List<Department> getDepartments();

  public List<Lecture> getLectures(Department department);

  public List<Lecture> getLectures(Faculty faculty);

}
