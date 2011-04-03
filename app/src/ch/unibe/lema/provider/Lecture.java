package ch.unibe.lema.provider;

public class Lecture {

    private String title;
    private String number;
    private String persons;
    private String semester;

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return number + ": " + title;
    }

    public String getTitle() {
        return title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }
}
