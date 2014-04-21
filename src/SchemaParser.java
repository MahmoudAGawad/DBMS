
import java.io.File;
import java.util.ArrayList;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SchemaParser {

	public static void main(String[] args) {
		SchemaParser ob = new SchemaParser();

		// testing load database
		// ArrayList<Tables> arr = ob.loadDataBase("test1");
		// for(Tables tb : arr){
		// System.out.println("table name :"+tb.getTableName());
		// ArrayList<Col> fields = tb.getFields();
		// for(Col field : fields){
		// System.out.println("field name : "+field.getFieldName()+" and type :"+field.getFieldType());
		// }
		//
		// }
		//
		// ob.CreateDataBase("test2", arr);

		// testing add table
		// Tables tbb = new Tables();
		// tbb.setTableName("tb1");
		// Col x = new Col();
		// x.setFieldName("students");
		// x.setFieldType("Seq. of characters");
		//
		// Col y = new Col();
		// y.setFieldName("teachers");
		// y.setFieldType("interger");
		//
		// ArrayList<Col > arrr = new ArrayList<Col>();
		// arrr.add(x);
		// arrr.add(y);
		//
		// tbb.setFields(arrr);
		//
		// ob.addTable("test1", "tb1", tbb);
		//
		//
		// ArrayList<Tables> arr = ob.loadDataBase("test1");
		// for(Tables tb : arr){
		// System.out.println("table name :"+tb.getTableName());
		// ArrayList<Col> fields = tb.getFields();
		// for(Col field : fields){
		// System.out.println("field name : "+field.getFieldName()+" and type :"+field.getFieldType());
		// }
		//
		// }

		// testing add database
		// ob.addDataBase("database2");

	}

	public static String rootPath = XMLParser.path;

	public void CreateDataBase(String DataBase, ArrayList<Tables> tables) { 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("database");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", DataBase);

			if (tables != null)
				for (Tables tb : tables) {
					Element table = doc.createElement("table");

					Element name = doc.createElement("name");
					name.setTextContent(tb.getTableName());
					table.appendChild(name);

					ArrayList<Col> fields = tb.getFields();
					for (Col field : fields) {
						Element e = doc.createElement("field");

						Element nm = doc.createElement("name");
						nm.setTextContent(field.getFieldName());
						e.appendChild(nm);

						Element type = doc.createElement("type");
						type.setTextContent(field.getFieldType());
						e.appendChild(type);

						table.appendChild(e);
					}
					rootElement.appendChild(table);

				}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rootPath +"/"+DataBase + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		}
	}

	public ArrayList<Tables> loadDataBase(String databaseName) { // abanub
		//System.out.println("Successfully");
		ArrayList<Tables> returnDataBases = null;
		try {
			
			
			File fXmlFile = new File(rootPath +"/"+ databaseName+".xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			returnDataBases = new ArrayList<Tables>();
			NodeList database = doc.getElementsByTagName("table");
			for (int i = 0; i < database.getLength(); i++) {
				Tables tb = new Tables();

				Node node = database.item(i);
				Element e = (Element) node;
				tb.setTableName(e.getElementsByTagName("name").item(0)
						.getTextContent());

				NodeList fields = e.getElementsByTagName("field");

				ArrayList<Col> fieldsCol = new ArrayList<Col>();
				for (int j = 0; j < fields.getLength(); j++) {
					Col field = new Col();
					Node node2 = fields.item(j);
					Element e2 = (Element) node2;

					field.setFieldName(e2.getElementsByTagName("name").item(0)
							.getTextContent());
					field.setFieldType(e2.getElementsByTagName("type").item(0)
							.getTextContent());

					fieldsCol.add(field);
				}

				tb.setFields(fieldsCol);
				returnDataBases.add(tb);
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No DataBase \""+databaseName+"\" Found");
		}
		return returnDataBases;
	}

	public boolean addTable(String DataBase, String TableName, Tables tb) { // waleed
		
		tb.setTableName(TableName);
		ArrayList<Tables> tables = loadDataBase(DataBase);
		
		boolean f=true;
		for(Tables tab : tables){
			if(tab.getTableName().equalsIgnoreCase(TableName)){
				f=false;
				break;
			}
		}
		if(f){
		tables.add(tb);
		
		CreateDataBase(DataBase, tables);
		
		new DTDGenerator(rootPath+"/"+DataBase+"_"+TableName);
		}
		return true;
	}

	public void addDataBase(String DataBase) { // mostafa-diaa

		ArrayList<String> databases = loadRoot();
		databases.add(DataBase);
		updateRoot(databases);
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("database");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", DataBase);

			
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rootPath +"/"+DataBase + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		}

	}

	private void updateRoot(ArrayList<String> databases) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("databases");
			doc.appendChild(rootElement);

			for (String db : databases) {
				Element e = doc.createElement("database");
				e.setTextContent(db);
				rootElement.appendChild(e);
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rootPath
					+ "/root.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		}
	}

	public ArrayList<String> loadRoot() { // abanub
		ArrayList<String> returnDataBases = null;
		try {

			File fXmlFile = new File(rootPath + "/root.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			returnDataBases = new ArrayList<String>();
			NodeList database = doc.getElementsByTagName("database");
			for (int i = 0; i < database.getLength(); i++) {
				Node node = database.item(i);
				Element e = (Element) node;
				returnDataBases.add(e.getTextContent());
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No Root-DataBase Found");
		}
		return returnDataBases;
	}

}
