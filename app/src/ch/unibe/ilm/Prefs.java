package ch.unibe.ilm;

import ch.unibe.ilm.model.Department;
import ch.unibe.ilm.model.Faculty;
import ch.unibe.ilm.model.University;

import android.content.SharedPreferences;

public class Prefs {

  public final static String KEY_UNIVERSITY = "university";
  public final static String KEY_DEPARTMENT = "department";
  public final static String KEY_FACULTY = "faculty";

  private University myUni;
  private Department myDep;
  private Faculty myFac;

  public Prefs(SharedPreferences inPrefs) {
    // set members from inPrefs
  }

  public University myUniversity() {
    return myUni;
  }

  public Department myDepartment() {
    return myDep;
  }

  public Faculty myFaculty() {
    return myFac;
  }

}
