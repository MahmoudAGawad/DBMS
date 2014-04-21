import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class DBMSImplementer implements DBMS {

	private String result = "";

	@Override
	public void createDateBase(String name) {
		// TODO Auto-generated method stub
		SchemaParser obj = new SchemaParser();
		obj.addDataBase(name);

	}

	@Override
	public void createTable(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		// TODO Auto-generated method stub
		XMLParser obj = new XMLParser();
		ArrayList<Col> array = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			array.add(new Col(entries.get(i).getName(), entries.get(i)
					.getValue()));
		}
		try {
			obj.createTable(dataBaseName, tableName, array);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void insert(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		 ArrayList<Col>table=obj.load(dataBaseName, tableName);

		//ArrayList<Col> table = intialize();
		if (table == null){
			System.out.println("NULL TABLE");
			return;
		}
		int len = table.get(0).getLen();
		for (int i = 0; i < table.size(); i++) {
			table.get(i).addElemByDefault();
		}
		for (int i = 0; i < entries.size(); i++) {
			// search for the name of the col
			String fieldName = entries.get(i).getName();
			for (int j = 0; j < table.size(); j++) {
				if (fieldName.equals(table.get(j).getFieldName())) {
					table.get(j).setElement(len, entries.get(i).getValue());
				}
			}
		}
		try {
			obj.createTable(dataBaseName, tableName, table);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//print(table);
	}

	@Override
	public ArrayList<Col> selectAll(String dataBaseName, String tableName) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		 ArrayList<Col> array=obj.load(dataBaseName, tableName);
		//ArrayList<Col> array = intialize();
		if (array == null)
			return null;
		else {
			print(array);
		}
		return array;

	}

	@Override
	public void select(String dataBaseName, String tableName, Field entry) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col>table=obj.load(dataBaseName, tableName);
		//ArrayList<Col> table = intialize();
		if (table == null)
			return;

		int indexOfConField = getIndexOfField(table, entry.getName());// to
																		// store
																		// the
																		// field
																		// of
																		// condition
																		// the

		String comparedVal = entry.getValue();// to store the compared value
		char relation = entry.getRelation();// to store the condition relation

		for (int i = 0; i < table.get(0).getLen(); i++) {
			if (compareRelation(table.get(indexOfConField).get(i), comparedVal,
					relation, table.get(indexOfConField).getFieldType())) {
				printRaw(table, i);

			}
		}

	}

	@Override
	 public void update(String dataBaseName, String tableName,
	   ArrayList<Field> entries, Field entry) {
	 
	  // get the array of col from load
	  XMLParser obj = new XMLParser();
	  ArrayList<Col>table=obj.load(dataBaseName, tableName);
	//  ArrayList<Col> table = intialize();
	  if(table==null)return;
	 
	  int indexOfConField = getIndexOfField(table, entry.getName());
	  String comparedVal = entry.getValue();// to store the compared value
	  char relation = entry.getRelation();// to store the condition relation
	 
	  for (int i = 0; i < table.get(0).getLen(); i++) {
	 
	   if (compareRelation(table.get(indexOfConField).get(i), comparedVal,
	     relation, table.get(indexOfConField).getFieldType())) {
	    updateRaw(table, i, entries);
	   }
	 
	  }
	  try {
	   obj.createTable(dataBaseName, tableName, table);
	  } catch (ParserConfigurationException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  } catch (TransformerException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  //print(table);
	  System.out.println("Done :)");
	 }

	@Override
	public void delete(String dataBaseName, String tableName, Field entry) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col>table=obj.load(dataBaseName, tableName);
		//ArrayList<Col> table = intialize();
		if (table == null)
			return;

		int indexOfConField = getIndexOfField(table, entry.getName());
		String comparedVal = entry.getValue();// to store the compared value
		char relation = entry.getRelation();// to store the condition relation

		for (int i = 0; i < table.get(0).getLen(); i++) {

			if (compareRelation(table.get(indexOfConField).get(i), comparedVal,
					relation, table.get(indexOfConField).getFieldType())) {
				deleteRaw(table, i);
				i--;
			}

		}
		try {
			obj.createTable(dataBaseName, tableName, table);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//print(table);
		System.out.println("Done :)");
	}

	public void updateRaw(ArrayList<Col> table, int index,
			ArrayList<Field> entries) {

		for (int i = 0; i < entries.size(); i++) {
			for (int j = 0; j < table.size(); j++) {
				if (table.get(j).getFieldName()
						.equalsIgnoreCase(entries.get(i).getName())) {
					table.get(j).setElement(index, entries.get(i).getValue());
				}
			}
		}

	}

	public int getIndexOfField(ArrayList<Col> table, String fieldName) {

		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getFieldName().equalsIgnoreCase(fieldName)) {
				return i;

			}
		}
		return 0;
	}

	public boolean compareRelation(String first, String second, char relation,
			String type) {
		if (relation == '>') {
			if (type.equalsIgnoreCase("String")) {
				return first.compareTo(second) > 0;
			}
			return Double.parseDouble(first) > Double.parseDouble(second);
		}
		if (relation == '<') {
			if (type.equalsIgnoreCase("String")) {
				return first.compareTo(second) < 0;
			}
			return Double.parseDouble(first) < Double.parseDouble(second);
		}
		if (relation == '=') {
			if (type.equalsIgnoreCase("String")) {
				return first.compareTo(second) == 0;
			}
			return Double.parseDouble(first) == Double.parseDouble(second);

		}
		return false;
	}

	public ArrayList<Col> intialize() {

		ArrayList<Col> array = new ArrayList<>();

		array.add(new Col("First Name", "string"));
		array.add(new Col("Last Name", "string"));
		array.add(new Col("Rank", "Integer"));

		return array;

	}

	public void printRaw(ArrayList<Col> table, int indexOfRaw) {
		for (int i = 0; i < table.size(); i++) {
			System.out.print(table.get(i).get(indexOfRaw));
			result += table.get(i).get(indexOfRaw);
			if (i < table.size() - 1) {
				System.out.print(" / ");
				result += " / ";

			}

		}
		System.out.println();
		result += "\n";

	}

	public String getResult() {
		return result;
	}

	public void deleteRaw(ArrayList<Col> table, int indexOfRaw) {
		for (int i = 0; i < table.size(); i++) {

			table.get(i).removeIndex(indexOfRaw);
		}

	}

	public void print(ArrayList<Col> table) {
		for (int i = 0; i < table.size(); i++) {
			System.out.print(table.get(i).getFieldName());
			if (i < table.size() - 1) {
				System.out.print(" / ");
			}

		}
		System.out.println();
		for (int i = 0; i < table.get(0).getLen(); i++) {
			printRaw(table, i);
		}
	}

}