package PlanetaryDefense.PDCrawler;

import java.util.UUID;
import java.util.regex.Pattern;
import java.sql.*;

import org.apache.commons.lang.StringEscapeUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yongyao
 *
 */
public class MyCrawler extends WebCrawler{
  private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
      + "|png|mp3|mp3|zip|gz))$");
  //public static Connection connection = DBConnector.getDBconnection("postgres", "admin", "pd");
  public static Connection connection = DBConnector.getDBconnection("postgres", "a7d6m5i4n362J", "drupal");

  public MyCrawler() {
  }

  private static java.sql.Timestamp getCurrentTimeStamp() {
    java.util.Date today = new java.util.Date();
    return new java.sql.Timestamp(today.getTime());
  }

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    return !FILTERS.matcher(href).matches()
        && !href.contains("http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=");
  }

  /**
   * This function is called when a page is fetched and ready
   * to be processed by your program.
   */
  @Override
  public void visit(Page page) {
    String url = page.getWebURL().getURL();
    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      text = text.replaceAll("[^\\S\\r\\n]+", " ").replaceAll("\\n+", " ").replaceAll("\\s+", " ");
      String title = htmlParseData.getTitle();
      if(title!=null)
      {
        try {
          if (connection != null) {               
            String myStatement = " INSERT INTO webpages (id, title, url, content, date) VALUES (?,?,?,?,?)";
            PreparedStatement st= connection.prepareStatement(myStatement);
            String uniqueID = UUID.randomUUID().toString();
            st.setString(1, uniqueID);
            st.setString(2, StringEscapeUtils.escapeCsv(title));
            st.setString(3, url);
            st.setString(4, StringEscapeUtils.escapeCsv(text));
            st.setString(5, getCurrentTimeStamp().toString());
            st.executeUpdate();
            st.close();
          } else {
            System.out.println("Failed to make connection!");
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
