package ch.unibe.lema.provider;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class Lecture implements Parcelable {

    private String title;
    private String number;
    private String staff;
    private String semester;
    private String description;
    private int ects;
    private Time start, end;
    private Long id;

    public Lecture() {
        id = -1L;
    }

    public Lecture(long id) {
        this.id = id;
    }

    public Lecture(Parcel in) {
        id = in.readLong();
        title = in.readString();
        number = in.readString();
        staff = in.readString();
        semester = in.readString();
        description = in.readString();
    }

    public boolean isSubscription() {
        return id != -1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getEcts() {
        return ects;
    }

    public void setTimeStart(Time timeStart) {
        this.start = timeStart;
    }

    public Time getTimeStart() {
        return start;
    }

    public void setTimeEnd(Time timeEnd) {
        this.end = timeEnd;
    }

    public Time getTimeEnd() {
        return end;
    }

    /**
     * match a Lecture based on the number
     */
    public boolean equals(Lecture l) {
        return l != null && l.getNumber().equalsIgnoreCase(number);
    }

    /**
     * implement Parcelable interface so that a Lecture can easely be from
     * Activity to Activity via Intent
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(number);
        dest.writeString(staff);
        dest.writeString(semester);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<Lecture> CREATOR = new Parcelable.Creator<Lecture>() {
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }

    };

}
