/* RegListMgr: A command line application to manipulate the contents
   of a course registration list represented as an XML document.

   Version: A REDUCED DEMO VERSION 0.7

   FILES: 
	- name of the registration list file is given as a command line argument
	- reglist.dtd: DTD for the registration list

   P. Kilpelainen, University of Kuopio, 2003 - 2010

*/

import java.io.*;

// JAXP packages:
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import java.util.Calendar;


public class RegListMgr {

    static final String appName = "RegListMgr";
    static final String version = "Reduced DEMO version 0.7";

    static BufferedReader terminalReader;

    public RegListMgr() throws UnsupportedEncodingException {

        terminalReader = new BufferedReader(
                new InputStreamReader(System.in, System.getProperty("file.encoding")));
    }

    public void process(Document doc, String filename) {

        String cmd;   // current command string
        boolean cont = true;

        try { // to read command characters from terminal
      
    /* MAIN COMMAND READING LOOP: */
            while (cont) {
                System.out.print("> ");

                cmd = terminalReader.readLine();
                if (cmd == null)
                    cont = false;
                else if (cmd.length() > 0) {
                    cmd = cmd.trim();
                    if (cmd.startsWith("ac")) {
                        makeActivityList(doc);

                    } else if (cmd.startsWith("ad")) {
                        addRecords(doc);

                    } else if (cmd.startsWith("d")) {
                        deleteRecords(doc);

                    } else if (cmd.startsWith("e")) {
                        editExercs(doc);

                    } else if (cmd.startsWith("f")) {
                        findStudent(doc);

                    } else if (cmd.startsWith("l")) {
                        listRecords(doc);

                    } else if (cmd.startsWith("q")) {
                        cont = false;

                    } else if (cmd.startsWith("s")) {
                        saveInFile(doc, filename);

                    } else if (cmd.startsWith("u")) {
                        update(doc);

                    } else
                        showCommands();

                }
                ; // if (cmd.length() > 0)
            } // while (cont)
        } // of try (to read commands)
        catch (Exception e) {
            e.printStackTrace();
        }
    } // public void process(Document doc)

/* AUXILIARY METHODS: */

    private void usage() {
        System.err.println("Usage: " + appName + " [options] <filename>");
        System.err.println("       -v = validation");
        System.exit(1);
    }

    private void showCommands() {

        System.out.println("?: \t show commands (this message) ");
        System.out.println("ac[tivity]: \t produce exercise activity list ");
        System.out.println("ad[d]: \t add records ");
        System.out.println("d[el]: \t delete records ");
        System.out.println("e[x]: \t edit exercise data ");
        System.out.println("f[ind]: \t find student ");
        System.out.println("h[elp]: \t show commands (this message) ");
        System.out.println("l[ist]: \t list records ");
        System.out.println("s[ave]: \t save in file ");
        System.out.println("q[quit]: \t quit ");
        System.out.println("u[pdate]: \t update ");
        System.out.println(" ");
    } // showCommands()


/* COMMAND IMPLEMENTATION METHODS: */

    private Element newStudent(Document doc, String ID,
                               String fName, String lName,
                               String branchAndYear, String email, String group)
// Create and return a new 'student' element 
// with the given values for its 'id' attribute and its sub-elements
    {
        Element newStudent = doc.createElement("student");
        newStudent.setAttribute("id", ID);
        newStudent.setIdAttribute("id", true);

        Element newName = doc.createElement("name");

        Element newGiven = doc.createElement("given");
        newGiven.appendChild(doc.createTextNode(fName));
        Element newFamily = doc.createElement("family");
        newFamily.appendChild(doc.createTextNode(lName));
        newName.appendChild(newGiven);
        newName.appendChild(newFamily);

        newStudent.appendChild(newName);

        Element newBranch = doc.createElement("branchAndYear");
        newBranch.appendChild(doc.createTextNode(branchAndYear));
        newStudent.appendChild(newBranch);

        Element newEmail = doc.createElement("email");
        newEmail.appendChild(doc.createTextNode(email));
        newStudent.appendChild(newEmail);

        Element newGroup = doc.createElement("group");
        newGroup.appendChild(doc.createTextNode(group));
        newStudent.appendChild(newGroup);

        return newStudent;

    } // newStudent

    private void addRecords(Document doc) throws Exception {

        System.out.println("Starting to add records");

        Element rootElem = doc.getDocumentElement();
        String lastID = rootElem.getAttribute("lastID");
        String courseID = rootElem.getAttribute("courseID");

        int lastIDnum = java.lang.Integer.parseInt(lastID);

        String ID, firstName, lastName, branchAndYear, email, group;

        System.out.print("First name (or <return> to finish): ");
        firstName = terminalReader.readLine().trim();

        while (firstName.length() > 0) {

            ID = courseID + "_" + new Integer(++lastIDnum).toString();

            System.out.print("Last name: ");
            lastName = terminalReader.readLine().trim();

            System.out.print("Branch&year: ");
            branchAndYear = terminalReader.readLine().trim();

            System.out.print("email: ");
            email = terminalReader.readLine().trim();

            System.out.print("group: ");
            group = terminalReader.readLine().trim();

            // Create and append a new student element:
            Element newStudent =
                    newStudent(doc, ID, firstName, lastName, branchAndYear, email, group);
            rootElem.appendChild(newStudent);

            System.out.print("First name (or <return> to finish): ");
            firstName = terminalReader.readLine().trim();

        } // while firstName.length() > 0

        String newLastID = java.lang.Integer.toString(lastIDnum);
        rootElem.setAttribute("lastID", newLastID);
        System.out.println("Finished adding records");
    }

    private void deleteRecords(Document doc) throws Exception {

        System.out.println("Starting to delete records");

        Element rootElem = doc.getDocumentElement();
        String lastID = rootElem.getAttribute("lastID");
        String courseID = rootElem.getAttribute("courseID");
        String ID, IDnum;

        System.out.print("Give ID (1 .. " + lastID + ") or <return> to finish): ");
        IDnum = terminalReader.readLine().trim();

        while (IDnum.length() > 0) {

            ID = courseID + "_" + IDnum;

            System.out.println("Deleting student " + ID);

/* Insert the implementation of deletion here: */

            System.out.println("Deletion disabled in " + version);

            System.out.print("Give ID (1 .. " + lastID + ") or <return> to finish): ");
            IDnum = terminalReader.readLine().trim();
        } // while IDnum.length() > 0

        System.out.println("Finished deleting records");

    } // deleteRecords


    private void makeActivityList(Document doc) throws Exception {

        System.out.println("Not implemented in " + version);

    } // editExercs

    private void editExercs(Document doc) throws Exception {

        System.out.println("Not implemented in " + version);

    } // editExercs

    private void findStudent(Document doc) throws Exception {

        System.out.print("Give family name (or <return>): ");

        String fName = terminalReader.readLine().trim();

        if (fName.length() > 0) {

/* Insert the implementation of look-up by family name here: */

            System.out.println("Not implemented in " + version);

        }
        ; // if (fName.getLength() > 0)

    } // findStudent

    private void showStudent(Element student) throws Exception {

        // Collect relevant sub-elements:
        Node given = student.getElementsByTagName("given").item(0);
        Node family = given.getNextSibling();
        Node bAndY = student.
                getElementsByTagName("branchAndYear").item(0);
        Node email = bAndY.getNextSibling();
        Node group = email.getNextSibling();

//    System.out.print(student.getAttribute("id").substring(3));
        System.out.print(student.getAttribute("id"));
        System.out.print(": " + given.getTextContent());
        // or simply given.getFirstChild().getNodeValue()
        System.out.print(" " + family.getTextContent());
        System.out.print(", " + bAndY.getTextContent());
        System.out.print(", " + email.getTextContent());
        System.out.println(", " + group.getTextContent());

    } // showStudent

    private void listRecords(Document doc) throws Exception {
        System.out.println("listing records:");

        NodeList students = doc.getElementsByTagName("student");

        for (int i = 0; i < students.getLength(); i++) {

            showStudent((Element) students.item(i));

        }
    }

    private void updateField(Document doc, Element student,
                             String fieldName) throws Exception
/* An auxiliary method of 'update'.
   Reads a new value from 'terminalReader', and if it is non-empty, uses it 
   to update the value of sub-element 'fieldName' of student
   Element 'student' */ {

    } //  updateField


    private void update(Document doc) throws Exception {

        Element rootElem = doc.getDocumentElement();
        String lastID = rootElem.getAttribute("lastID");

        System.out.print("Give student number (1 .. " + lastID + "): ");

        int num = Integer.parseInt(terminalReader.readLine().trim());
        num -= 1;
        NodeList students = doc.getElementsByTagName("student");
        if (num < students.getLength() && num >= 0) {
            Element e = (Element) students.item(num);
            showStudent(e);

            System.out.print("New given (or <return>): ");
            String given = terminalReader.readLine().trim();
            System.out.print("New family (or <return>): ");
            String family = terminalReader.readLine().trim();
            System.out.print("New branchAndYear (or <return>): ");
            String branchAndYear = terminalReader.readLine().trim();
            System.out.print("New email (or <return>): ");
            String email = terminalReader.readLine().trim();
            System.out.print("New group (or <return>): ");
            String group = terminalReader.readLine().trim();
            if(!given.equals("")){
                Element tmp = (Element) e.getElementsByTagName("given").item(0);
                tmp.setTextContent(given);
            }
            if(!family.equals("")){
                Element tmp = (Element) e.getElementsByTagName("family").item(0);
                tmp.setTextContent(family);
            }
            if(!branchAndYear.equals("")){
                Element tmp = (Element) e.getElementsByTagName("branchAndYear").item(0);
                tmp.setTextContent(branchAndYear);
            }
            if(!email.equals("")){
                Element tmp = (Element) e.getElementsByTagName("email").item(0);
                tmp.setTextContent(email);
            }
            if(!group.equals("")){
                Element tmp = (Element) e.getElementsByTagName("group").item(0);
                tmp.setTextContent(group);
            }
			showStudent((Element) students.item(num));
        }else{
            System.out.println("No such student number");
        }
        
    } // update

    private static void saveInFile(Document doc, String oldFileName)
            throws Exception {
        System.out.println("saving records");

        String outFileName;
        File outFile;
        String answ;

        System.out.print("Give output file name: ");
        outFileName = terminalReader.readLine().trim();

        if (outFileName.length() == 0) outFileName = oldFileName;

        outFile = new File(outFileName);

        boolean OKtoSave = true;

        if (outFile.exists()) {

            do {
                System.out.print("OK to overwrite existing file "
                        + outFileName + "?  (y/n): ");
                answ = terminalReader.readLine().trim();
            } while (!(answ.equals("y") || answ.equals("yes") ||
                    answ.equals("n") || answ.equals("no")));

            if (answ.equals("n") || answ.equals("no")) {
                OKtoSave = false;
                System.out.println("Save canceled");
            }
        }
        if (OKtoSave) {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            // transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "reglist.dtd");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource src = new DOMSource(doc);
            StreamResult result = new StreamResult(outFile);
            transformer.transform(src, result);

            System.out.println("Saved records in file " + outFileName);
        }
    } // saveInFile()


    public static void main(String args[]) throws Exception {

        String filename = null;
        // Parsing options:
        boolean validation = false;
        boolean ignoreWhitespace = true;
        boolean ignoreComments = true;
        boolean putCDATAIntoText = true;
        boolean expandEntities = true;

        RegListMgr mgr = new RegListMgr();

        System.out.println(appName + " (" + version + ")");


        final String[] MM = // two-digit month identifiers
                new String[]{"01", "02", "03", "04", "05", "06",
                        "07", "08", "09", "10", "11", "12"};

        Calendar currentDate = Calendar.getInstance();
        String currentMM = MM[currentDate.get(Calendar.MONTH)];
        int currDayNum = currentDate.get(Calendar.DAY_OF_MONTH);
        String currentDD;
        if (currDayNum > 9) currentDD = "" + currDayNum;
        else currentDD = "0" + currDayNum;

        System.out.println(currentDate.get(Calendar.YEAR) + "-" +
                currentMM + "-" + currentDD);

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) {
                validation = true;
            } else {
                filename = args[i];

                // Must be last arg
                if (i != args.length - 1) {
                    mgr.usage();
                }
            }
        } // for
        if (filename == null) {
            mgr.usage();
        }

        // Step 1: create a DocumentBuilderFactory:
        DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();

        // Optional: set various configuration options:
        dbf.setValidating(validation);
        dbf.setIgnoringComments(ignoreComments);
        dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
        dbf.setCoalescing(putCDATAIntoText);
        dbf.setExpandEntityReferences(expandEntities);

        // Step 2: create a DocumentBuilder using the DocumentBuilderFactory:
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }

        // Set an ErrorHandler before parsing:
        OutputStreamWriter errorWriter =
                new OutputStreamWriter(System.err);
        db.setErrorHandler(
                new MyErrorHandler(new PrintWriter(errorWriter, true)));

        // Step 3: parse the input file
        Document doc = null;
        try {
            doc = db.parse(new File(filename));
        } catch (SAXException se) {
            System.err.println(se.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }

        System.err.println("Document loaded succesfully");

        mgr.process(doc, filename);

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

} // public class RegListMgr
