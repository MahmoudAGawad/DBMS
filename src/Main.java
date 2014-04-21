import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// String str=System.getenv().get("Path");
		// int sge=str.indexOf(';');
		// System.out.println(str.substring(0, sge));
		BufferedReader k = new BufferedReader(new InputStreamReader(System.in));
		Parser x = new Parser();
		// Scanner k = new Scanner(System.in);
		// String y = k.readLine();
		Main ob = new Main();
		ob.CreateRoot();

		System.out.println(XMLParser.path);
		while (true) {
			String y = k.readLine();
			if(y.equalsIgnoreCase("exit")) break;
			x.parseStatment(y);
		}

	}

	private void CreateRoot() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		File b = new File(XMLParser.path + "/root.xml");
		if (b.exists())
			return;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("databases");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", "root");

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(b);

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Root Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Root Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Root Not Created");
		}
	}

}