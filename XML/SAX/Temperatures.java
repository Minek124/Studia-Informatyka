import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
// Default implementation of ContentHandler
import org.xml.sax.helpers.DefaultHandler; 

// SUN JAXP package:
import javax.xml.parsers.*;


public class Temperatures extends DefaultHandler {
// Extends the default behaviour of the ContentHandler interface (implemented
// by DefaultHandler) to handle the content of a simple db file


public static void main (String args[]) 
	throws Exception {

// Create a JAXP SAXParserFactory and configure it:

  SAXParserFactory spf = SAXParserFactory.newInstance();
  try {
	  SAXParser saxParser = spf.newSAXParser();
          XMLReader xmlReader = saxParser.getXMLReader();
// Instantiate a ContentHandler and pass it to xmlReader:
          ContentHandler handler = new Temperatures();
          xmlReader.setContentHandler(handler);
          for (int i = 0; i < args.length; i++) {
            xmlReader.parse(args[i]);
          }
	} catch (Exception e) {
		System.err.println(e.getMessage());
                System.exit(1);
	};
} // main

  // Flags to remember relevant element context:

	  private double max = -9999;
	  private double min = 9999;
	  private String maxYear;
	  private String minYear;
	  private String maxCity;
	  private String minCity;
	  private String currentCity;

  // Storage for element contents and attribute values:

      private String FirstName, LastName;
      private String IdNum;

  // Callback methods:
  
  public void  startDocument() {
		 }

  public void endDocument() {
         	 System.out.println("min: " + minCity + "(" + minYear + ") " + min + " C");
			 System.out.println("max: " + maxCity + "(" + maxYear + ") " + max + " C");
		 }

  public void startElement (String namespaceURI, String localName, String rawName, Attributes atts) {
    if (rawName.equals("city")) {
		currentCity = atts.getValue(0);
	}
	if (rawName.equals("obs")) {
		double val = Double.parseDouble(atts.getValue(1));
		if(max < val){
			max = val;
			maxYear = atts.getValue(0);
			maxCity = currentCity;
		}
		if(min > val){
			min = val;
			minYear = atts.getValue(0);
			minCity = currentCity;
		}
	}
	
  } // startElement

 public void characters (char ch[], int start, int length) {
 } // characters  

public void endElement (String namespaceURI, String localName, String qName) {

	
} // endElement

} // public class Temperatures
