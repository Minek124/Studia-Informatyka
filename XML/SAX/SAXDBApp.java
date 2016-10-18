import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
// Default implementation of ContentHandler
import org.xml.sax.helpers.DefaultHandler; 

// SUN JAXP package:
import javax.xml.parsers.*;


public class SAXDBApp extends DefaultHandler {
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
          ContentHandler handler = new SAXDBApp();
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

  private boolean InFirst = false, 
		InLast = false;	

  // Storage for element contents and attribute values:

      private String FirstName, LastName;
      private String IdNum;

  // Callback methods:
  
  public void  startDocument() {
         System.out.println("<HTML><HEAD><TITLE>A sample SAX result page</TITLE>");  
		 System.out.println("</HEAD><BODY>");  
		 System.out.println("<H1>Contents of the DB</H1>");  
		 System.out.println("<TABLE>");  
		 System.out.println("<TR><TH>Last name</TH><TH>number</TH></TR>");  
		 }

  public void endDocument() {
         System.out.println("</TABLE>");  
		 System.out.println("</BODY>");  
		 System.out.println("</HTML>");  		 
		 }

  public void startElement (String namespaceURI, String localName,
                             String rawName, Attributes atts) {
   
	if (rawName.equals("last")) 
		InLast = true;
	if (rawName.equals("person")) 
		IdNum = atts.getValue("idnum");
  } // startElement

 public void characters (char ch[], int start, int length) {
	if (InLast) 
		LastName = new String(ch, start, length);
	 
 } // characters  

public void endElement (String namespaceURI, String localName,
			String qName) {
	if (qName.equals("last")) 
		InLast = false;
	if (qName.equals("person")) 
          	System.out.println("<TR><TD>" + LastName + "</TD><TD>(" 
		+ IdNum + ")</TD></TR>" );
	
} // endElement

} // public class SAXDBApp
