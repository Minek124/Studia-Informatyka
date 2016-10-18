
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;

public class SAX extends DefaultHandler {
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        System.out.print("<" + qName + "> ");
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.print(atts.getValue(i) + ", ");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        System.out.println("</" + qName + "> ");
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (length > 5) {
            System.out.print(new String(ch, start, length));
        }
    }

    public static void main(String[] args) {
        SAX f = new SAX();
        SAXParser p = new SAXParser();
        p.setContentHandler(f);

        try {
            p.parse("recipes.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
