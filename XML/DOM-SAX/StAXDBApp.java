import org.w3c.dom.events.Event;

import java.io.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.namespace.QName; 

/* A StAX-based application to display the contents of a simple XML DB file  */

public class StAXDBApp {
  static final String appName = "StAXDBApp";

  private static void usage() {
    System.err.println("Usage: " + appName + "XML_file");
    System.exit(1); }

public static void main(String[] args) throws FactoryConfigurationError, 
					XMLStreamException, IOException {

    final String notFound = "not-found";
    // Values of data fields we are looking for:
    String 	idNumValue = notFound, 
		firstName = notFound, 
		lastName = notFound;
    // Flags for element context:
    boolean 	inFirst = false, inLast = false;
    QName idNumQName = new QName("idnum"); // name of the idNum attribute

    if (args.length != 1) usage(); 
    String docFile = args[0]; 

    // Initialize an XMLEventReader for docFile:
    XMLInputFactory xif = XMLInputFactory.newInstance();
    XMLEventReader xer = xif.createXMLEventReader(new FileInputStream(docFile));
    boolean InLast = false;
    String LastName = "";
    String IdNum = "";

    while(xer.hasNext()) {
        XMLEvent e = xer.nextEvent();
        switch (e.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                String tagName = e.asStartElement().getName().getLocalPart();
                if (tagName.equals("last"))
                    InLast = true;
                if (tagName.equals("person")) {
                    IdNum = ((Attribute) e.asStartElement().getAttributes().next()).getValue();
                }
                break;
            case XMLStreamConstants.CHARACTERS:
                if (InLast)
                    LastName = e.asCharacters().toString().trim();
                break;
            case XMLStreamConstants.END_ELEMENT:
                if (e.asEndElement().getName().getLocalPart().equals("last"))
                    InLast = false;
                if (e.asEndElement().getName().getLocalPart().equals("person"))
                    System.out.println("<TR><TD>" + LastName + "</TD><TD>("
                            + IdNum + ")</TD></TR>" );
                break;
            case XMLStreamConstants.START_DOCUMENT:
                System.out.println("<HTML><HEAD><TITLE>A sample SAX result page</TITLE>");
                System.out.println("</HEAD><BODY>");
                System.out.println("<H1>Contents of the DB</H1>");
                System.out.println("<TABLE>");
                System.out.println("<TR><TH>Last name</TH><TH>number</TH></TR>");
                break;
            case XMLStreamConstants.END_DOCUMENT:
                System.out.println("</TABLE>");
                System.out.println("</BODY>");
                System.out.println("</HTML>");
                break;
        }

    }

  } // main()
} // class StAXDBApp
