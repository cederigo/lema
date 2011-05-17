package ch.unibe.lema.provider.evub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.text.format.Time;
import android.util.Log;
import ch.unibe.lema.provider.Lecture;

/**
 * @author bm
 * 
 */
public class EvubParser extends DefaultHandler {

    private static final String LOG_TAG = "EvubParser";

    private List<Lecture> lectures;
    private Lecture currentLecture;    
    private String currentElement, timeStart, timeEnd, dateStart, dateEnd, weekDay;
    private boolean inElement;
    private int eventsRecorded;
    private Attributes currentAttributes;

    public EvubParser(List<Lecture> lectures) {
        this.lectures = lectures;
        this.currentLecture = new Lecture();
        inElement = false;
        eventsRecorded = 0;
    }

    /**
     * Java needs switch-case with Strings NOW
     */
    public void characters(char[] ch, int start, int length) {

        if (inElement) {
            if (currentElement.equals("lecture_title")) {
                String title = new String(ch, start, length);
                currentLecture.setTitle(title);
            } else if (currentElement.equals("number")) {
                String number = new String(ch, start, length);
                currentLecture.setNumber(number);
            } else if (currentElement.equals("persons")) {
                String persons = new String(ch, start, length);
                currentLecture.setStaff(persons);
            } else if (currentElement.equals("semester")) {
                String semester = new String(ch, start, length);
                currentLecture.setSemester(semester);
            } else if (currentElement.equals("comment")) {
                String desc = new String(ch, start, length);
                currentLecture.setDescription(desc);
            } else if (currentElement.equals("ects")) {
                float ects = 0;
                try {
                    ects = Float.parseFloat(new String(ch, start, length));
                } catch (NumberFormatException e) {
                    Log.d(LOG_TAG, "no ECTS provided");
                }
                currentLecture.setEcts(ects);
            } else if (currentElement.equals("date_start")) {
                dateStart = new String(ch, start, length).trim();
            } else if (currentElement.equals("date_end")) {
                dateEnd = new String(ch, start, length).trim();
            } else if (currentElement.equals("time_start")) {
                timeStart = new String(ch, start, length).trim();
            } else if (currentElement.equals("time_end")) {
                timeEnd = new String(ch, start, length).trim();
            } else if (currentElement.equals("day")) {
                //weekDay = currentAttributes.getValue(0);
            }

            inElement = false;
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (localName.equals("lecture")) {
            lectures.add(currentLecture);
            currentLecture = new Lecture();
            inElement = false;
            currentElement = "";
            eventsRecorded = 0;
        } else if (localName.equals("event")) {        
            
            if (eventsRecorded == 0) {
                currentLecture.setTimeStart(dateFromString(dateStart));
                currentLecture.setTimeEnd(dateFromString(dateEnd));                
            }
            
            currentLecture.addEvent("some location", 
                    timeFromString(timeStart,weekDay), timeFromString(timeStart,weekDay));
            

            dateStart = null;
            dateEnd = null;
            timeStart = null;
            timeEnd = null;

            eventsRecorded++;
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        inElement = true;
        currentElement = localName;
        currentAttributes = attributes;
    }

    /**
     * Return a Time object which hopefully is equal to what is specified by
     * parameters. Format assumptions: date: DD.MM.YYYY
     * 
     * TODO parse if only date or time is given
     * 
     * @param date
     * @param time
     * @return
     */
    private Time dateFromString(String date) {
        ;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        Time t = new Time();

        try {
            t.set(sdf.parse(date).getTime());
        } catch (ParseException e) {
            Log.d(LOG_TAG, "failed to parse '" + date + "'");
        }

        return t;
    }
    
    /**
     * Return a Time object which hopefully is equal to what is specified by
     * parameters. Format assumptions:HH:mm
     * 
     * TODO parse if only date or time is given
     * 
     * @param date
     * @param time
     * @return
     */
    private Time timeFromString(String time, String weekDay) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        int dayOfWeek = 0;

        Time t = new Time();        

        try {
            t.set(sdf.parse(time).getTime());
            dayOfWeek = Integer.parseInt(weekDay);
            t.weekDay = dayOfWeek;
        } catch (Exception e) {
            Log.d(LOG_TAG, "failed to parse '" + time + "'");
        }

        return t;
    }
    
}
