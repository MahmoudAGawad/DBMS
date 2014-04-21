import java.util.ArrayList;

public class Validator {

	// returns true if the database name exists

	private boolean check_dataBase(String dataBaseName) {

		SchemaParser ob = new SchemaParser();
		return ob.loadRoot().contains(dataBaseName);
	}

	// returns true if the table exists in a certain database

	private boolean check_table(String dataBaseName, String tableName) {
		SchemaParser ob = new SchemaParser();
		ArrayList<Tables> db = ob.loadDataBase(dataBaseName);
		
		if (db != null)
			for (Tables tb : db) {
				if (tb.getTableName().equalsIgnoreCase(tableName))
	 				return true;
			}
		return false;

	}

	// returns true if the table's data type matches the entry's data type

	private boolean check_type(String dataBaseName, String tableName,
			String entryValue, String entryName) {
		ArrayList<Tables> tablesList = new ArrayList<Tables>();
		SchemaParser ob = new SchemaParser();
		tablesList = ob.loadDataBase(dataBaseName);

		if (tablesList != null)
			for (Tables tb : tablesList) {
				if (tb.getTableName().equals(tableName)) {
					ArrayList<Col> fields = tb.getFields();
					for (Col field : fields) {
						if (field.getFieldName().equals(entryName)) {
							switch (field.getFieldType()) {
							case "integer":
								return check_int(entryValue);
							case "double":
								return check_double(entryValue);
							default:
								return true;
							}

						}
					}
				}
			}
		return false;

	}

	private boolean check_int(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean check_double(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// checks if the new database name already exists, if not it calls dbms to
	// create it

	public void validate_createDateBase(String name) {
		// check if database found
		if (check_dataBase(name))
			System.out.println("database already exists!");
		else {
			DBMS ob = new DBMSImplementer();
			ob.createDateBase(name);
		}
	}

	// checks if the new table name already exists if not it calls dbms to
	// create it

	public void validate_createTable(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName))
				System.out.println("table already exist!");
			else {

				DBMS ob = new DBMSImplementer();
				ob.createTable(dataBaseName, tableName, entries);
			}
		} else
			System.out.println("database does not exist!");

	}

	// checks if database and table exist if true it calls dbms to insert an
	// entry
	public void validate_insert(String dataBaseName, String tableName,
			ArrayList<Field> entries) {

		boolean flag = true;

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				for (Field element : entries) {

					if (!check_type(dataBaseName, tableName,
							element.getValue(), element.getName())) {
						flag = false;
						break;

					}
				}

				if (flag) {
					DBMS ob = new DBMSImplementer();
					ob.insert(dataBaseName, tableName, entries);
				} else {
					System.out
							.println("data type does not math the table's data type");
				}
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");
	}

	// checks if database and table exist if true it calls dbms to select
	// entries
	public void validate_selectAll(String dataBaseName, String tableName) {

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				DBMS ob = new DBMSImplementer();
				ob.selectAll(dataBaseName, tableName);
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");

	}

	// checks if database and table exist if true it calls dbms to select an
	// entry
	public void validate_select(String dataBaseName, String tableName,
			Field entry) {

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				if (check_type(dataBaseName, tableName, entry.getValue(),
						entry.getName())) {
					DBMS ob = new DBMSImplementer();
					ob.select(dataBaseName, tableName, entry);
				} else {
					System.out
							.println("data type does not math the table's data type");
				}
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");
	}

	// checks if database and table exist if true it calls dbms to update
	// entries
	public void validate_update(String dataBaseName, String tableName,
			ArrayList<Field> entries, Field entry) {

		boolean flag = true;

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				for (Field element : entries) {

					if (!check_type(dataBaseName, tableName,
							element.getValue(), element.getName())) {
						flag = false;
						break;

					}
				}

				if (flag) {
					if (check_type(dataBaseName, tableName, entry.getValue(),
							entry.getName())) {
						DBMS ob = new DBMSImplementer();
						ob.update(dataBaseName, tableName, entries, entry);
					} else {

						System.out.println("conition does not apply!");
					}
				} else {
					System.out
							.println("data type does not math the table's data type");
				}
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");
	}

	// checks if database and table exist if true it calls dbms to delete
	// entries
	public void validate_delete(String dataBaseName, String tableName,
			Field entry) {

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				if (check_type(dataBaseName, tableName, entry.getValue(),
						entry.getName())) {
					DBMS ob = new DBMSImplementer();
					ob.delete(dataBaseName, tableName, entry);
				} else
					System.out.println("conition does not apply!");

			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");

	}

}