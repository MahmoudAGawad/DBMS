import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	public static String path = System.getenv().get("TEMP");

	private void update(String DataBase, String TableName,
			ArrayList<Col> fields) throws ParserConfigurationException,
			TransformerException {

		try {
			File file = new File(path + "/" + DataBase + "_" + TableName
					+ ".xml");
			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		createTable(DataBase, TableName, fields);

	}

	public static void createTable(String DataBase, String TableName,
			ArrayList<Col> fields) throws ParserConfigurationException,
			TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("table");
		rootElement.setAttribute("name", TableName);
		doc.appendChild(rootElement);
		// staff elements
		for (int i = 0; i < fields.size(); i++) {
			Element field = doc.createElement("field");
			rootElement.appendChild(field);
			// //

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(fields.get(i).getFieldName()));
			field.appendChild(name);

			Element type = doc.createElement("type");
			type.appendChild(doc.createTextNode(fields.get(i).getFieldType()));
			field.appendChild(type);

			// //////
			for (int j = 0; j < fields.get(i).getElements().size(); j++) {
				Element element = doc.createElement("element");
				element.appendChild(doc.createTextNode(fields.get(i)
						.getElements().get(j)));
				field.appendChild(element);

			}
		}

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path + "\\" + DataBase
				+ "_" + TableName + ".xml"));
		transformer.transform(source, result);

		SchemaParser ob = new SchemaParser();

		ob.addTable(DataBase, TableName, new Tables(fields));

	}

	private ArrayList<Col> returnedarr = new ArrayList<>();

	public  ArrayList<Col> load(String databaseName, String table) {

		DTDValidator ob = new DTDValidator();

		if (ob.validate(databaseName + "_" + table) == false) {
			System.out.println("table format not correct");
			return null;

		} else {

			try {

				File fXmlFile = new File(path + "/" + databaseName + "_"
						+ table + ".xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				doc.getDocumentElement().normalize();

				// System.out.println("Root element :" +
				// doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("field");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Col obj = new Col();
					ArrayList<String> elements = new ArrayList<>();

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						obj.setFieldName(eElement.getElementsByTagName("name")
								.item(0).getTextContent());
						obj.setFieldType(eElement.getElementsByTagName("type")
								.item(0).getTextContent());

						NodeList nList2 = eElement
								.getElementsByTagName("element");

						// System.out.println(nList2.);

						for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {

							Node nNode2 = nList2.item(temp2);

							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement2 = (Element) nNode2;
								elements.add(eElement2.getTextContent());

							}
						}
						// rrr.add(eElement.getElementsByTagName("element").item(1).getTextContent());

						obj.setElements(elements);

						returnedarr.add(obj);
						// rrr.clear();

					}
				}

			} catch (Exception e) {
				return null;
			}

			return returnedarr;
		}
	}

}