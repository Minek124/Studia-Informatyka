import java.util.Calendar;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 * Simple practice of JAXP interfaces
 * <p>
 * Pekka Kilpelainen, Univ. of Eastern Finland 2015
 */
public class DOMHello {

    public static void main(String args[])
            throws ParserConfigurationException,
            TransformerConfigurationException,
            TransformerException {

        Calendar today = Calendar.getInstance();
        int month_int = today.get(Calendar.MONTH) + 1,
                day_int = today.get(Calendar.DAY_OF_MONTH);

        String monthStr = ((month_int < 10) ? "0" : "") + month_int;
        String dayStr = ((day_int < 10) ? "0" : "") + day_int;
        String currDateStr =
                today.get(Calendar.YEAR) + "-" + monthStr + "-" + dayStr;

        System.out.println("Today is " + currDateStr);

        // First initialize: DocumentBuilderFactory -> DocumentBuilder,
        // and create a new empty DOM Document:
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        // Then create nodes for the new Document, and connect them
        // to the document as its componets:
        Element root = doc.createElement("greeting");
        doc.appendChild(root);
        root.setAttribute("date",currDateStr);
        root.appendChild(doc.createTextNode("Hello, world!"));

        // Create a Transformer, and use it to write
        // the Document to System.out:
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.transform(new DOMSource((doc)),new StreamResult(System.out));
    } // main()
} // class DOMHello

