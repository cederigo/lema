package ch.unibe.ilm.provider;

import java.util.List;

import ch.unibe.ilm.model.Department;
import ch.unibe.ilm.model.Faculty;
import ch.unibe.ilm.model.Lecture;

public class UniBeDataProvider implements ILectureDataProvider {

	public String getName() {
		return "unibe";
	}

	public List<Faculty> getFaculties() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Department> getDepartments() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Lecture> getLectures(Department department) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Lecture> getLectures(Faculty faculty) {
		// TODO Auto-generated method stub
		return null;
	}

}
