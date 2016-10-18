import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/* A StAX example application 
 * - to list the values of 'no' attributes in the given file
 *
 * P. Kilpelainen, Univ. of Eastern Finland, 2013-11-12 * */

public class mergeChildren {
    static final String appName = "mergeChildren";
    static final String options = " ";

    private static void usage() {
        System.err.println("Usage: " + appName + options + "boys.xml girls.xml");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) usage();
        String file1 = args[0];
        String file2 = args[1];

        // Initialize an event reader:
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLEventWriter xew = XMLOutputFactory.newInstance().createXMLEventWriter(System.out);
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, new Boolean(false));
        InputStream is1 = new FileInputStream(file1);
        InputStream is2 = new FileInputStream(file2);
        XMLEventReader xer1 = xif.createXMLEventReader(is1);
        XMLEventReader xer2 = xif.createXMLEventReader(is2);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("children");
        doc.appendChild(root);
        NodeList list = root.getChildNodes();

        QName no = new QName("no");
        QName name = new QName("name");
        QName age = new QName("age");

        while (xer1.hasNext()) {
            XMLEvent inEvent = xer1.nextEvent();
            if (inEvent.isStartElement()) {
                StartElement se = (StartElement) inEvent;
                if(se.getName().getLocalPart().equals("boy") || se.getName().getLocalPart().equals("girl")){
                    Element el = doc.createElement("child");
                    el.setAttribute("no",se.getAttributeByName(no).getValue());
                    el.setAttribute("name",se.getAttributeByName(name).getValue());
                    el.setAttribute("age",se.getAttributeByName(age).getValue());
                    root.appendChild(el);
                }
            }
        } // while
        xer1.close();
        is1.close();

        while (xer2.hasNext()) {
            XMLEvent inEvent = xer2.nextEvent();
            if (inEvent.isStartElement()) {
                StartElement se = (StartElement) inEvent;
                if(se.getName().getLocalPart().equals("boy") || se.getName().getLocalPart().equals("girl")){
                    Element el = doc.createElement("child");
                    int newChildNumber = Integer.parseInt(se.getAttributeByName(no).getValue());
                    el.setAttribute("no",se.getAttributeByName(no).getValue());
                    el.setAttribute("name",se.getAttributeByName(name).getValue());
                    el.setAttribute("age",se.getAttributeByName(age).getValue());
                    boolean found = false;
                    for(int i=0;i<list.getLength();i++){
                        int childNumber = Integer.parseInt(((Element)list.item(i)).getAttribute("no"));
                        if(newChildNumber < childNumber){
                            found = true;
                            root.insertBefore(el, list.item(i));

                            break;
                        }
                    }
                    if(!found){
                        root.appendChild(el);
                    }
                }
            }
        } // while
        xer2.close();
        is2.close();

        for(int i=0;i<list.getLength();i++){
            int childNumber = Integer.parseInt(((Element)list.item(i)).getAttribute("no"));
            System.out.print(childNumber + " ");
        }
        System.out.println();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.transform(new DOMSource((doc)),new StreamResult(System.out));
    }


} // class mergeChildren
