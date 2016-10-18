// Used JAXP packages:
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;

/**
 * An example program that displays SAX ContentHandler events that result from parsing 
 * the input file. Also shows one way to turn validation on, and how to use a
 * SAX ErrorHandler.
 *
 * @author Pekka Kilpelainen <Pekka "dot" Kilpelainen "at" cs "dot" uku "dot" fi>
 *
 * (borrows some code from the SAXTagCount example by Edwin Goei <edwingo@apache.org>)
 */

public class XMLTest implements ContentHandler {

/**************************************************
 * Implementations of ContentHandler methods
 * display the callbacks and their parameters:
 **************************************************/

  public void  startDocument() throws SAXException {
         System.out.println("startDocument()");  }

  public void endDocument() throws SAXException {
         System.out.println("endDocument()");    }

  public void setDocumentLocator(Locator loc){
        System.out.println("setDocumentLocator(" + loc.toString() + ")" ); }

  public void processingInstruction(String target, String data) throws SAXException { 
       System.out.println("processingInstruction(\"" + target + "\", \"" + data + "\")" ); }

  public void skippedEntity(String name) throws SAXException {
         System.out.println("skippedEntity(\"" + name +  "\")" ); }


  public void startPrefixMapping(String prefix, String uri) throws SAXException {
      System.out.println("startPrefixMapping(\"" + prefix + "\", \"" + uri + "\")" ); }

  public void endPrefixMapping(String prefix) throws SAXException {
      System.out.println("endPrefixMapping(\"" + prefix + "\")" ); }


  public void startElement(String nsURI, String localName, String rawName, Attributes atts)
     throws SAXException {
        System.out.print("startElement(\"" + nsURI + "\", \"" + localName + "\", \"" + rawName + "\", [" );
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.print(atts.getQName(i) + "=\"" + atts.getValue(i) + "\"" );
            if (i < atts.getLength() - 1) System.out.print(", ");
        }
        System.out.println("])");
  }

  public void endElement(String nsURI, String localName, String rawName)
      throws SAXException {
        System.out.println("endElement(\"" + nsURI + "\", \"" + localName + "\", \"" + rawName + "\")" );
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
      System.out.println("characters(\"" + new String(ch, start, length) + "\")" );
  }

  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      System.out.println("ignorableWhiteSpace(\"" + new String(ch, start, length) + "\")" );
  }

 /* End of ContentHandler callback methods */


  private static void usage() {
    System.err.println("Usage: XMLTest [-v][-n][-s] <filename>");
    System.err.println("       -n = namespace support off");
    System.err.println("       -v = validate");
    System.err.println("       -s = show SAX ContentHandler events");
    System.exit(1);
  }

/*********************/
/* Class main method: */
/*********************/

  static public void main(String[] args) {
    String filename = null;
    boolean validation = false;
    boolean namespaces = true;
    boolean showEvents = false;

    // Parse command line arguments:
    for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-n")) { 

	  namespaces = false;
        
        } else if (args[i].equals("-v")) { 

	  validation = true; 

        } else if (args[i].equals("-s")) { 

	  showEvents = true; 

	} else {
          
	  filename = args[i];

          // Must be the last arg:
          if (i != args.length - 1) {usage();}
        }
    }
    if (filename == null) {usage(); }

    // Create a JAXP SAXParserFactory and configure it:

    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setValidating(validation);
    spf.setNamespaceAware(namespaces);

    XMLReader xmlReader = null;
    try { // to create a JAXP SAXParser
        SAXParser saxParser = spf.newSAXParser();

        // and get the encapsulated SAX XMLReader
        xmlReader = saxParser.getXMLReader();
    } catch (Exception ex) {
        System.err.println(ex);
        System.exit(1);
    }

    // Set the appropriate Handlers before parsing:
    if (showEvents) 

	xmlReader.setContentHandler(new XMLTest() );

    else

	xmlReader.setContentHandler(new DefaultHandler() );

    xmlReader.setErrorHandler(new MyErrorHandler(System.err));

    try { // to parse the input file:

        xmlReader.parse(filename);
        System.out.println("OK");

    } catch (SAXException se) {
        System.err.println(se.getMessage());
        System.exit(1);
    } catch (IOException ioe) {
        System.err.println(ioe);
        System.exit(1);
    }
} // XMLTest.main()


/************************************************/
/* Error handler to report errors and warnings: */
/************************************************/

private static class MyErrorHandler implements ErrorHandler {
    
    private PrintStream out; // Error handler output goes here

    MyErrorHandler(PrintStream out) { this.out = out; }

    /**
     * Gives info from a SAXParseException as a String:
     */
    private String getParseExceptionInfo(SAXParseException spe) {

        String systemId = spe.getSystemId();

        if (systemId == null) {  systemId = "null"; }

        String info = "URI=" + systemId + " Line=" + spe.getLineNumber() +
                ": " + spe.getMessage();

        return info;
    }

    // Implementations of standard SAX ErrorHandler methods:
   
    public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
    }
        
    public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
    }

} // private class MyErrorHandler

} // public class XMLTest
