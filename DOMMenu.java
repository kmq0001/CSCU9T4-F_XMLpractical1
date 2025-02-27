import java.io.*;               // import input-output

import javax.xml.XMLConstants;
import javax.xml.parsers.*;         // import parsers
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;           // import XPath
import javax.xml.validation.*;      // import validators
import javax.xml.transform.*;       // import DOM source classes

//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import org.w3c.dom.*;               // import DOM

/**
  DOM handler to read XML information, to create this, and to print it.

  @author   CSCU9T4, University of Stirling
  @version  11/03/20
*/
public class DOMMenu {

  /** Document builder */
  private static DocumentBuilder builder = null;

  /** XML document */
  private static Document document = null;

  /** XPath expression */
  private static XPath path = null;

  /** XML Schema for validation */
  private static Schema schema = null;

  /*----------------------------- General Methods ----------------------------*/

  /**
    Main program to call DOM parser.

    @param args         command-line arguments
  */
  public static void main(String[] args)  {
    // load XML file into "document"
    loadDocument(args[0]);
    // print staff.xml using DOM methods and XPath queries
    if (validateDocument(args[1])){
      printNodes();
    }
  }

  /**
    Set global document by reading the given file.

    @param filename     XML file to read
  */
  private static void loadDocument(String filename) {
    try {
      // create a document builder
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builder = builderFactory.newDocumentBuilder();

      // create an XPath expression
      XPathFactory xpathFactory = XPathFactory.newInstance();
      path = xpathFactory.newXPath();

      // parse the document for later searching
      document = builder.parse(new File(filename));
    }
    catch (Exception exception) {
      System.err.println("could not load document " + exception);
    }
  }

  /*-------------------------- DOM and XPath Methods -------------------------*/
  /**
   Validate the document given a schema file
   @param filename XSD file to read
  */
  private static Boolean validateDocument(String filename)  {
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File(filename));
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
      return true;
    } catch (Exception e){
      System.err.println("Could not load schema or validate. Location of error and specification given:\n");
      System.err.println(e);
      return false;
    }
  }
  /**
    Print nodes using DOM methods and XPath queries.
  */
  private static void printNodes() {
    NodeList res = query("//item/name");
    NodeList res1 = query("//item/price");
    NodeList res2 = query("//item/description");

    for(int i = 0; i < res.getLength(); i++) {
      Node name = res.item(i);
      Node price = res1.item(i);
      Node description = res2.item(i);
      if (name.getTextContent().length() < 9) {
        System.out.print(name.getTextContent() + "\t\t");
      }
      else{
        System.out.print(name.getTextContent() + "\t");
      }
      System.out.print(price.getTextContent() + "\t");
      System.out.println(description.getTextContent());
    }
  }

  /**
    Get result of XPath query.

    @param query        XPath query
    @return         result of query
  */
  private static NodeList query(String query) {
    NodeList result = null;
    try {
      result = (NodeList) path.evaluate(query, document, XPathConstants.NODESET);
    }
    catch (Exception exception) {
      System.err.println("could not perform query - " + exception);
    }
    return(result);
  }
}
