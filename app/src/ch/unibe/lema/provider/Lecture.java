package ch.unibe.lema.provider;

public class Lecture {

    private String title;
    private String number;
    private String staff;
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

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }

    public String getStaff() {
        return staff;
    }
}
