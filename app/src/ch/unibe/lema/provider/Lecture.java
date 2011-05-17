package ch.unibe.lema.provider;

import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class Lecture implements Parcelable {

    private String title;
    private String number;
    private String staff;
    private String semester;
    private String description;
    private float ects;
    private Time startDate, endDate;
    private List<Event> events;
    private Long id;    
    
    
    public class Event implements Parcelable{
        
        public Time startTime;
        public Time endTime;         
        public String location;
        
        
        private Event(String location, Time startTime, Time endTime) {
            this.location = location;
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        private Event(Parcel in) {
            this.location = in.readString();
            this.startTime = new Time();
            this.startTime.set(in.readLong());
            this.endTime = new Time();
            this.endTime.set(in.readLong());
            
        }
        
        public int describeContents() {            
            return 0;
        }
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(location);
            dest.writeLong(startTime.toMillis(false));
            dest.writeLong(endTime.toMillis(false));
        }
        
    }

    public Lecture() {
        id = -1L;
        startDate = new Time();
        endDate = new Time();
        events = new LinkedList<Event>();
    }

    public Lecture(long id) {
        this();
        this.id = id;
    }

    public Lecture(Lecture copy, long id) {
        this.id = id;
        this.number = copy.getNumber();
        this.title = copy.getTitle();
        this.staff = copy.getStaff();
        this.semester = copy.getSemester();
        this.description = copy.getDescription();
        this.ects = copy.getEcts();
        this.startDate = copy.getTimeStart();
        this.endDate = copy.getTimeEnd();
    }

    public Lecture(Parcel in) {
        id = in.readLong();
        title = in.readString();
        number = in.readString();
        staff = in.readString();
        semester = in.readString();
        description = in.readString();
        ects = in.readFloat();
        startDate = new Time();
        startDate.set(in.readLong());
        endDate = new Time();
        endDate.set(in.readLong());
        int eventSize = in.readInt();
        for (int i = 0; i < eventSize; i++) {
            events.add(new Event(in));
        }
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

    public void setEcts(float ects) {
        this.ects = ects;
    }

    public float getEcts() {
        return ects;
    }

    public void setTimeStart(Time timeStart) {
        this.startDate = timeStart;
    }

    public Time getTimeStart() {
        return startDate;
    }

    public void setTimeEnd(Time timeEnd) {
        this.endDate = timeEnd;
    }

    public Time getTimeEnd() {
        return endDate;
    }
    
    public void addEvent(String description, Time startTime, Time endTime) {
        events.add(new Event(description,startTime,endTime));
    }
    
    public List<Event> getEvents() {
        return events;
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
        dest.writeFloat(ects);
        dest.writeLong(startDate.toMillis(false));
        dest.writeLong(endDate.toMillis(false));
        dest.writeInt(events.size());
        for (Event e : events) {
            dest.writeParcelable(e, flags);
        }
        
    }

    public static final Parcelable.Creator<Lecture> CREATOR = new Parcelable.Creator<Lecture>() {
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }

    };

    public long getId() {
        return id;
    }

}
