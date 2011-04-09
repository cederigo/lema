package ch.unibe.lema.provider;

import android.os.Parcel;
import android.os.Parcelable;

public class Lecture implements Parcelable{

    private String title;
    private String number;
    private String staff;
    private String semester;
    
    public Lecture() {}
    
    public Lecture(Parcel in) {
        title = in.readString();
        number = in.readString();
        staff = in.readString();
        semester = in.readString();
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
    
    /**
     * match a Lecture based on the number
     */
    public boolean equals(Lecture l) {
        return l != null && l.getNumber().equalsIgnoreCase(number);
    }
    
    
    /**
     * implement Parcelable interface so that a Lecture can easely be from Activity to Activity
     * via Intent
     */
    public int describeContents() {
        
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        
        dest.writeString(title);
        dest.writeString(number);
        dest.writeString(staff);
        dest.writeString(semester);
        
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
