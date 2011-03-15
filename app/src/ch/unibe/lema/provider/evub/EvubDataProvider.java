package ch.unibe.lema.provider.evub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import ch.unibe.lema.model.Lecture;
import ch.unibe.lema.provider.ILectureDataProvider;
import ch.unibe.lema.provider.filter.Filter;
import ch.unibe.lema.provider.filter.FilterCriterion;

public class EvubDataProvider implements ILectureDataProvider {

  private static final String LOG_TAG = "EvubDataProvider";
  private static final String BASE_URL = "https://webapps.unibe.ch/evubs/evubs?";
  private Filter filterCriteria;
  private HttpClient client;

  public EvubDataProvider() {
    filterCriteria = new Filter();
    client = new DefaultHttpClient();
  }

  public String getName() {
    return "unibe";
  }

  /**
   * 
   * @param filter
   * @return
   */
  public List<Lecture> getLectures(Filter filter) {
    return askEvub(filter);
  }

  /**
   * Return a Filter object containing a list of possible filter criteria for
   * this DataProvider.
   */
  public Filter getCriterias() {
    return filterCriteria;
  }

  /**
   * 
   * @param filter
   * @return
   */
  private List<Lecture> askEvub(Filter filter) {
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
      
    } catch (ParserConfigurationException e1) {
      e1.printStackTrace();
    } catch (SAXException e1) {
      e1.printStackTrace();
    } catch (FactoryConfigurationError e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
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
