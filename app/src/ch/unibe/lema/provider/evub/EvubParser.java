package ch.unibe.lema.provider.evub;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import ch.unibe.lema.provider.Lecture;

/**
 * @author bm
 * 
 */
public class EvubParser extends DefaultHandler {

    private static final String LOG_TAG = "EvubParser";

    private List<Lecture> lectures;
    private Lecture currentLecture;
    private String currentElement;
    private boolean inElement;

    public EvubParser(List<Lecture> lectures) {
        this.lectures = lectures;
        this.currentLecture = new Lecture();
        inElement = false;
    }

    /**
     * Java needs switch-case with Strings NOW
     */
    public void characters(char[] ch, int start, int length) {
        if (inElement) {
            if (currentElement.equals("lecture_title")) {
                String title = new String(ch, start, length);
                currentLecture.setTitle(title);
                inElement = false;
            } else if (currentElement.equals("number")) {
                String number = new String(ch, start, length);
                currentLecture.setNumber(number);
                inElement = false;
            } else if (currentElement.equals("persons")) {
                String persons = new String(ch, start, length);
                currentLecture.setPersons(persons);
                inElement = false;
            } else if (currentElement.equals("semester")) {
                String semester = new String(ch, start, length);
                currentLecture.setSemester(semester);
                inElement = false;
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (localName.equals("lecture")) {
            lectures.add(currentLecture);
            currentLecture = new Lecture();
            inElement = false;
            currentElement = "";
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        inElement = true;
        currentElement = localName;
    }
}
