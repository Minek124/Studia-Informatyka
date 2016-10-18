import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class DOM {

    public static void main(String args[]) throws Exception {

        if (args.length != 1) {
            System.out.println("usage: java DOM XMLfile");
            return;
        }
        String docFile = args[0];
        long start = System.currentTimeMillis();
        DocumentBuilderFactory dbf;
        DocumentBuilder builder;

        dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);

        builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }

        Document doc = builder.parse(docFile);
        long initialized = System.currentTimeMillis();

        Element root = doc.getDocumentElement();
        NodeList list = root.getChildNodes();
        int count = 0;
        for (int i = 0; i < list.getLength(); i++) {
            ++count;
        }
        double end = System.currentTimeMillis();
        System.out.print("elements:" + count + " Initialization time:" + (initialized-start) + " counting time:" + (end - initialized) + " total time:" + (end-start));
    }
}

