import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.*;

public class DTDValidator {
//	public static void main(String[] args) throws Exception {
//
//		parseFile("db1_tb1.xml", "db1_tb1.dtd");
//	}
//	
	
	
	
	
	public boolean validate(String tableName){
		try {
			parseFile(tableName, tableName);
			return true;
		} catch (IOException e) {
			System.out.println("File Not Valid");
			return false;
		}
	}
	
	private static String path = SchemaParser.rootPath+"/";

	public  void parseFile(String xmlFile, String dtdFile)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path + xmlFile+".xml"));
		BufferedReader br1 = new BufferedReader(new FileReader(path + dtdFile+".dtd"));
		FileWriter fw = new FileWriter(path + "temp111.xml");
		BufferedWriter bw = new BufferedWriter(fw);

		String s="";
		
		String dtd="";
		String once;
		dtd+="<!DOCTYPE table [";
		while ((once = br1.readLine()) != null) {
			dtd =dtd+"\n"+ once;
		}
		dtd += "]>";
		
		
		while ((once = br.readLine()) != null) {
			s=s+once+"\n";
		}
		s=s.substring(54);
		
		s="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> \n "+dtd+" \n "+s;
		s=s.replaceAll("><", "> <");
		bw.write(s);
		bw.close();
		boolean valid = true;
		try {
			validateXMLFile(path + "temp111.xml");
		} catch (ParserConfigurationException e) {
			//System.out.println(1);
			valid = false;
		} catch (FileNotFoundException e) {
			//System.out.println(2);
			valid = false;
		} catch (SAXException e) {
			valid = false;
			//System.out.println(3);
		} catch (IOException e) {
			valid = false;
			//System.out.println(4);
		}

		File delete = new File(path + "temp111.xml");
		delete.delete();
		//System.out.println("Validation is " + valid);
	}

	public static Document validateXMLFile(String xmlFilePath)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setValidating(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {
			@Override
			public void error(SAXParseException exception) throws SAXException {
				// do something more useful in each of these handlers
				// System.out.println("A");
				throw new SAXException();
			}

			@Override
			public void fatalError(SAXParseException exception)
					throws SAXException {
				// System.out.println("B");
				throw new SAXException();
			}

			@Override
			public void warning(SAXParseException exception)
					throws SAXException {
				// System.out.println("C");
				throw new SAXException();
			}
		});
		builder.parse(xmlFilePath);
		return null;
	}
}