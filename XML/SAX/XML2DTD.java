import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
// Default implementation of ContentHandler
import org.xml.sax.helpers.DefaultHandler; 

// SUN JAXP package:
import javax.xml.parsers.*;
import java.util.ArrayList;

public class XML2DTD extends DefaultHandler {
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
          ContentHandler handler = new XML2DTD();
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

	  private ArrayList<ArrayList<String>> tab = new ArrayList<ArrayList<String>>();

  // Storage for element contents and attribute values:

      private String FirstName, LastName;
      private String IdNum;

  // Callback methods:
  
  public void  startDocument() {
		 }

  public void endDocument() {
			for (ArrayList<String> it : tab) {	
				System.out.println("<!ELEMENT " + it.get(0) + " ANY >");
				if(it.size() > 1){
					System.out.print("<!ATTLIST " + it.get(0));
					for (int i = 1; i < it.size(); i++) {
						System.out.println();
						System.out.print("	" + it.get(i) + " CDATA #IMPLIED");
					}
					System.out.println(" >");
				}
			}
		 }

  public void startElement (String namespaceURI, String localName, String rawName, Attributes atts) {
    int ind = 0;
	for (int i = 0; i < tab.size(); i++) {
		if (rawName.equals(tab.get(i).get(0))) {
			ind = i;
		}
	}

	if(ind == 0){
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(rawName);
		tab.add(tmp);
		ind = tab.size() - 1;
	}
		
	
	if (atts.getLength() > 0){
		for (int i = 0; i < atts.getLength(); i++) {
			boolean found = false;
			for (String it : tab.get(ind)) {
				if ( it.equals(atts.getQName(i))){
					found = true;
				}
			}
			if(!found){
				tab.get(ind).add(atts.getQName(i));
			}
        }
	}
	
	
  } // startElement

 public void characters (char ch[], int start, int length) {
 } // characters  

public void endElement (String namespaceURI, String localName, String qName) {

	
} // endElement

} // public class Temperatures
