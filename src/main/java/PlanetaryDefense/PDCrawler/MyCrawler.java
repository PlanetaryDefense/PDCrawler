package PlanetaryDefense.PDCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

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
  public static BufferedWriter bw = pre();

  public MyCrawler() {
  }

  public static BufferedWriter pre()
  {
    File file = new File("C:/cralwer_PDPages/crawlerPages.csv");
    if (file.exists()) {
      file.delete();
    }

    BufferedWriter bwl = null;
    try {
      file.createNewFile();
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      bwl = new BufferedWriter(fw); 
      bwl.write("URL, Title, Content, fileType, Collected time" + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bwl;
  }

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    return !FILTERS.matcher(href).matches()
        && !href.contains("http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=")
        && href.startsWith("http://neo.jpl.nasa.gov/") && !href.contains("http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=");
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
      try {
        bw.write(url + "," + StringEscapeUtils.escapeCsv(htmlParseData.getTitle()) + "," + StringEscapeUtils.escapeCsv(text) + "," + "Web page" + "," + new Date() + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
