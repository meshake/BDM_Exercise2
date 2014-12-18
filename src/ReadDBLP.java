import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Tomas on 10/7/2014. test
 */
public class ReadDBLP {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        List<String> names = loadNames();
        int totalPublications = 0;
        int noPublications = 0;
        for (String name : names) {
            int publicationCount = getPublicationCount(name);
            System.out.println(name + ": " + publicationCount);
            if (publicationCount == 0 || publicationCount == -1) {
                noPublications++;
            } else {
                totalPublications += publicationCount;
            }
            System.out.println("Total publications by the members of ITU: " + totalPublications + "\n" +
                    "No publications by " + noPublications + " members.");
            Thread.sleep(5000); // Sleep for 5 seconds to not overwhelm the site.
        }
        System.out.println("Total publications by the members of ITU: " + totalPublications + "\n" +
        "No publications by " + noPublications + " members.");
    }

    private static List<String> loadNames() {
        List<String> names = new ArrayList<String>();
        try {
            Scanner in = new Scanner(new FileReader("C:\\Users\\Tomas\\Desktop\\names2.txt"));
            while(in.hasNext()) {
                String name = in.nextLine();
                String[] splitName = name.split(" ");
                String firstNames = "";
                String surname = splitName[splitName.length-1];
                String surnameFirstLetter = surname.substring(0, 1).toLowerCase();
                for (int i = 0; i < splitName.length - 1; i++) {
                    if (firstNames.length() > 0) {
                        firstNames += "_" + splitName[i];
                    } else {
                        firstNames += splitName[i];
                    }
                }
                names.add(surnameFirstLetter + "/" +surname + ":" + firstNames);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return names;
    }

    private static int getPublicationCount(String person) {
        PublConfigHandler publHandler = null;
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", false);
            URL u = new URL("http://dblp.uni-trier.de/rec/pers/" + person + "/xk");
            publHandler = new PublConfigHandler();
            parser.parse(u.openStream(), publHandler);
        } catch (ParserConfigurationException e) {
            //e.printStackTrace();
        } catch (SAXException e) {
            //e.printStackTrace();
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return publHandler.publicationCount();
    }

    static class PublConfigHandler extends DefaultHandler {
        private String Value;
        private boolean insideKey, insideHp;
        private int number;
        public void startElement(String namespaceURI, String localName,
                                 String rawName, Attributes atts) throws SAXException {
            if (insideKey = rawName.equals("dblpkey")) {
                Value = "";
                insideHp = (atts.getValue("type") != null);
            }
        }
        public void endElement(String namespaceURI, String localName,
                               String rawName) throws SAXException {
            if (rawName.equals("dblpkey") && Value.length() > 0) {
                number++;
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (insideKey)
                Value += new String(ch, start, length);
        }

        private void Message(String mode, SAXParseException exception) {
            System.out.println(mode + " Line: " + exception.getLineNumber()
                    + " URI: " + exception.getSystemId() + "\n" + " Message: "
                    + exception.getMessage());
        }

        public void warning(SAXParseException exception) throws SAXException {

            Message("**Parsing Warning**\n", exception);
            throw new SAXException("Warning encountered");
        }

        public void error(SAXParseException exception) throws SAXException {

            Message("**Parsing Error**\n", exception);
            throw new SAXException("Error encountered");
        }

        public void fatalError(SAXParseException exception) throws SAXException {

            Message("**Parsing Fatal Error**\n", exception);
            throw new SAXException("Fatal Error encountered");
        }

        public int publicationCount() {
            return number - 1; // -1, since the 1st entry in the XML is not a publication.
        }
    }
}
