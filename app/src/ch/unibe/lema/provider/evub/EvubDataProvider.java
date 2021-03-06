package ch.unibe.lema.provider.evub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.res.Resources;
import android.util.Log;
import ch.unibe.lema.LemaException;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Filter;
import ch.unibe.lema.provider.FilterCriterion;
import ch.unibe.lema.provider.ILectureDataProvider;
import ch.unibe.lema.provider.Lecture;

public class EvubDataProvider implements ILectureDataProvider {

    private static final String LOG_TAG = "EvubDataProvider";
    private static final String BASE_URL = "https://webapps.unibe.ch/evubs/evubs?";
    private List<FilterCriterion> filterCriteria;
    private HttpClient client;

    public EvubDataProvider() throws LemaException {
        filterCriteria = new LinkedList<FilterCriterion>();
        client = new DefaultHttpClient();
    }
    
    public void init(Resources res) throws LemaException {

        filterCriteria.add(new FilterCriterion("institution"
                ,res.getStringArray(R.array.institutions),""));
        filterCriteria.add(new FilterCriterion("faculty"
                ,res.getStringArray(R.array.faculies), ""));
        // TODO automated semester string generation
        filterCriteria.add(new FilterCriterion("semester",
                new String[] { "S2011" }, ""));

    }

    public String getName() {
        return "unibe";
    }

    /**
     * 
     * @param filter
     * @return
     * @throws LemaException
     */
    public List<Lecture> getLectures(Filter filter) throws LemaException {
        return askEvub(filter);
    }

    /**
     * Return a list of possible filter criteria for this DataProvider.
     */
    public List<FilterCriterion> getCriteria() {
        return filterCriteria;
    }

    /**
     * 
     * @param filter
     * @return
     * @throws LemaException
     */
    private List<Lecture> askEvub(Filter filter) throws LemaException {
        HttpGet request = buildRequest(filter);
        List<Lecture> lectures = new ArrayList<Lecture>();
        SAXParser parser;

        try {
            HttpResponse response = client.execute(request);
            parser = SAXParserFactory.newInstance().newSAXParser();
            XMLReader xmlReader = parser.getXMLReader();
            xmlReader.setContentHandler(new EvubParser(lectures));

            InputSource is = new InputSource(response.getEntity().getContent());
            is.setEncoding("ISO-8859-1");
            xmlReader.parse(is);

        } catch (ParserConfigurationException e) {
            throw new LemaException(e);
        } catch (SAXException e) {
            throw new LemaException(e);
        } catch (FactoryConfigurationError e) {
            throw new LemaException(e.getMessage());
        } catch (IOException e) {
            throw new LemaException(e);
        }

        Log.d(LOG_TAG, "got " + lectures.size() + " lectures.");

        return lectures;
    }

    /**
     * 
     * @param filter
     * @return
     */
    private HttpGet buildRequest(Filter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);

        for (FilterCriterion crit : filter) {
            sb.append(crit.getKey());
            sb.append("=");
            sb.append(crit.getValue());
            sb.append("&");
        }
        Log.d(LOG_TAG, sb.toString());

        return new HttpGet(sb.toString());
    }
}
