import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
// Default implementation of ContentHandler
import org.xml.sax.helpers.DefaultHandler;

// SUN JAXP package:
import javax.xml.parsers.*;


public class SAX extends DefaultHandler {
    private static long start = 0;
    private static long initialized = 0;
    private static long end = 0;
    private long count = 0;
    private String currentElement = "";
    private boolean root = true;


    public static void main(String args[])
            throws Exception {
        if (args.length != 1) {
            System.out.println("usage: java SAX XMLfile");
            return;
        }
        String docFile = args[0];
        start = System.currentTimeMillis();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        ContentHandler handler = new SAX();
        xmlReader.setContentHandler(handler);
        xmlReader.parse(docFile);

    } // main

    public void startDocument() {
        initialized = System.currentTimeMillis();
    }

    public void endDocument() {
        end = System.currentTimeMillis();
        System.out.print("elements:" + count + " Initialization time:" + (initialized-start) + " counting time:" + (end - initialized) + " total time:" + (end-start));
    }

    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes atts) {

        if(root){
            root = false;
            return;
        }
        if(currentElement.equals("")){
            ++count;
            currentElement = rawName;
        }


    } // startElement
    public void endElement (String namespaceURI, String localName,
                            String qName) {
        if(qName.equals(currentElement)){
            currentElement = "";
        }
    } // endElement
} // public class SAXDBApp
