/* domEval: Evaluate expressions given as XML documents like

	<sum>
  	    <sum><int val="1" />
       		 <int val="2" />
       		 <int val="3" />
  	    </sum>
	    <sum><int val="4" /> <int val="5" /></sum>
  	  <int val="-6" />
	</sum>

 FILES: 
  - names of the input documents given as command line arguments

  P. Kilpelainen, 2004 - 2009

*/

import java.io.*;

// JAXP packages:
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.w3c.dom.*;

public class domEval {

    static final String appName = "domEval";

    private static void usage(String appName) {
        System.err.println("Usage: " + appName + " ('-v' | '-n' | filename)*");
        System.err.println("Options: -v for validation");
        System.err.println("         -n for no validation (default)");
        System.exit(1);
    }


    private static void evalDoc(Document doc) throws Exception {
        System.out.print("Value of ");
        int val = evalNode(doc.getDocumentElement());
        System.out.print(" is ");
        System.out.println(val);
    } // evalDoc

    private static int evalNode(Element node) throws Exception {
        int result = 0;

        if (node.getNodeName().equals("int")) {
            result = Integer.parseInt(node.getAttribute("val"));
            System.out.print(result);
        } else if (node.getNodeName().equals("sum")) {
            NodeList l = node.getChildNodes();
            System.out.print("(");
            for (int i = 0; i < l.getLength(); i++) {
                result += evalNode((Element) l.item(i));
                if (i != (l.getLength() - 1))
                    System.out.print("+");
            }
            System.out.print(")");
        } else if (node.getNodeName().equals("prod")) {
            NodeList l = node.getChildNodes();
            result = 1;
            System.out.print("(");
            for (int i = 0; i < l.getLength(); i++) {
                result *= evalNode((Element) l.item(i));
                if (i != (l.getLength() - 1))
                    System.out.print("*");
            }
            System.out.print(")");
        }

        return result;
    }// evalNode

    public static Document loadDoc(File file,
                                   ErrorHandler errHandler,
                                   boolean validate) throws Exception {
        DocumentBuilderFactory dbf;
        DocumentBuilder builder;
        Document doc;

        dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validate);
        dbf.setIgnoringElementContentWhitespace(true);

        // Step 2: create a DocumentBuilder using the DocumentBuilderFactory:
        builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }

        // Set the ErrorHandler:
        builder.setErrorHandler(errHandler);

        // Step 3: parse the input file
        try {
            doc = builder.parse(file);
            return doc;
        } catch (SAXException se) {
            System.err.println(se.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
        return null;
    }

    public static void main(String args[]) throws Exception {

        String filename = null;
        // Parsing flags:
        boolean validate = false;
        boolean noDocs = true;

        if (args.length < 1) usage(appName);

        // Create errHandler:
        OutputStreamWriter errorWriter =
                new OutputStreamWriter(System.err);
        ErrorHandler errHandler =
                new MyErrorHandler(new PrintWriter(errorWriter, true));

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v"))
                validate = true;
            else if (args[i].equals("-n"))
                validate = false;
            else {
                noDocs = false;
                File docFile = new File(args[i]);
                Document doc = loadDoc(docFile, errHandler, validate);

                evalDoc(doc);

            }
        } // for
        if (noDocs) usage(appName);


    } // main() method

// Error handler to report errors and warnings:
private static class MyErrorHandler implements ErrorHandler {
    /**
     * Error handler output goes here
     */
    private PrintWriter out;

    MyErrorHandler(PrintWriter out) {
        this.out = out;
    }

    /**
     * Returns a string describing parse exception details
     */
    private String getParseExceptionInfo(SAXParseException spe) {
        String systemId = spe.getSystemId();
        if (systemId == null) {
            systemId = "null";
        }
        String info = "URI=" + systemId +
                " Line=" + spe.getLineNumber() +
                ": " + spe.getMessage();
        return info;
    }

    // Standard SAX ErrorHandler methods:
    // See SAX documentation for more info.

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
} // class MyErrorHandler

} // public class domEval 
