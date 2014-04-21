import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {

	private ArrayList<Field> entries = new ArrayList<>();
	private Field entry;
	private String dataBaseName = "", tableName = "";
	private boolean all = false;

	public void parseStatment(String stat) {
		all = false;
		entry = null;
		entries.clear();
		// dataBaseName = "";
		tableName = "";
		Validator validate = new Validator();

		StringTokenizer str = new StringTokenizer(stat);
		String first = str.nextToken();
		if (first.equalsIgnoreCase("use")) {
			dataBaseName = str.nextToken().replace(";", "");
			return;
		}

		if (createDatabaseStatment(stat)) {
			// System.out.println(dataBaseName);
			// Validator validate = new Validator();
			validate.validate_createDateBase(dataBaseName);
		} else if (createTableStatment(stat)) {
			// System.out.println(dataBaseName + " " + tableName);
			for (int i = 0; i < entries.size(); i++) {
				System.out.println(entries.get(i).getName() + " "
						+ entries.get(i).getValue());
			}
			validate.validate_createTable(dataBaseName, tableName, entries);
		} else if (selectStatment(stat)) {
			if (all) {
				// System.out.println(tableName + " all");
				validate.validate_selectAll(dataBaseName, tableName);
			} else {
				// System.out.println(tableName + " where " + entry.getName()
				// + " " + entry.getRelation() + " " + entry.getValue());
				validate.validate_select(dataBaseName, tableName, entry);
			}

		} else if (deleteStatment(stat)) {
			// System.out.println(tableName + " where " + entry.getName() + " "
			// + entry.getRelation() + " " + entry.getValue());
			validate.validate_delete(dataBaseName, tableName, entry);
		} else if (insertStatment(stat)) {
			// System.out.println(tableName);
			for (int i = 0; i < entries.size(); i++) {
				// System.out.println(entries.get(i).getName() + " "
				// + entries.get(i).getValue());
			}
			validate.validate_insert(dataBaseName, tableName, entries);

		} else if (updateStatment(stat)) {
			// System.out.println(tableName + " where " + entry.getName() + " "
			// + entry.getRelation() + " " + entry.getValue());
			for (int i = 0; i < entries.size(); i++) {
				// System.out.println(entries.get(i).getName() + " "
				// + entries.get(i).getValue());
			}
			validate.validate_update(dataBaseName, tableName, entries, entry);
		} else {
			System.out.println("unspecified command !");
		}

	}

	public boolean selectStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;
		String firstWord = str.nextToken();
		if (firstWord.equalsIgnoreCase("select")) {

			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equals("*")) {
				if (!str.hasMoreTokens())
					return false;

				String thirdWord = str.nextToken();
				if (thirdWord.equalsIgnoreCase("from")) {
					if (!str.hasMoreTokens())
						return false;

					tableName = str.nextToken().replace(";", "");

					if (str.hasMoreTokens()) {
						String forthWord = str.nextToken();
						if (forthWord.equalsIgnoreCase("where")) {
							// parameter
							String line = "";
							while (str.hasMoreTokens()) {
								line += str.nextToken();
							}
							line = line.replaceAll("'", "")
									.replaceAll("\"", "").replace(";", "");
							boolean check = getvar(line);

							// String fieldName = str.nextToken();
							// String relation = str.nextToken();
							// String value =str.nextToken().replaceAll("'",
							// "").replaceAll("\"", "");
							// entry = new
							// Field(fieldName,value,relation.charAt(0));
							return check;
						} else if (forthWord.equalsIgnoreCase(";")) {
							// selectAll
							all = true;
							return true;
						}

					} else {
						// selectAll
						all = true;
						return true;
					}

				}

			}

		}
		return false;
	}

	public boolean createDatabaseStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();

		if (firstWord.equalsIgnoreCase("create")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("database")) {
				if (!str.hasMoreTokens())
					return false;

				dataBaseName = str.nextToken().replace(";", "");
				return true;

			}
		}
		return false;

	}

	public boolean createTableStatment(String stat) {
		entries.clear();
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();
		if (firstWord.equalsIgnoreCase("create")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("table")) {
				if (!str.hasMoreTokens())
					return false;

				tableName = str.nextToken();
				String name = "", type = "";
				while (str.hasMoreTokens()) {
					String word = str.nextToken();
					if (word.equals("(")) {
						continue;
					}
					if (name.equals("")) {
						name = word;
					} else if (type.equals("")) {
						type = word;
						if (!type.equalsIgnoreCase("integer")
								&& !type.equalsIgnoreCase("double")
								&& !type.equalsIgnoreCase("string")) {
							return false;
						}
					} else if (word.equals(",") || word.equals(")")) {
						entries.add(new Field(name, type));
						type = "";
						name = "";
					}
				}
				return true;
			}
		}
		return false;

	}

	public boolean deleteStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();
		if (firstWord.equalsIgnoreCase("delete")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("from")) {
				if (!str.hasMoreTokens())
					return false;

				tableName = str.nextToken();
				
				if (!str.hasMoreTokens())
					return false;
				
				String forthWord = str.nextToken();

				if (forthWord.equalsIgnoreCase("where")) {
					// parameter
					String line = "";
					while (str.hasMoreTokens()) {
						line += str.nextToken();
					}
					line = line.replaceAll("'", "").replaceAll("\"", "")
							.replace(";", "");
					boolean check = getvar(line);

					// String fieldName = str.nextToken();
					// String relation = str.nextToken();
					// String value = str.nextToken().replaceAll("'",
					// "").replaceAll("\"", "");
					// entry = new Field(fieldName, value, relation.charAt(0));
					return check;
				}

			}

		}
		return false;

	}

	public boolean insertStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();

		if (firstWord.equalsIgnoreCase("insert")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("into")) {
				boolean check = false, choose = false;
				ArrayList<String> fieldname = new ArrayList<>();
				ArrayList<String> value = new ArrayList<>();
				String line = "";
				while (str.hasMoreTokens()) {
					line += str.nextToken();
				}
				line = line.replace(";", "");
				String word = "";
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == '(') {
						if (i == 0)
							return false;
						if (!check) {
							tableName = word;

						}

						check = true;
						word = "";
					} else if (line.charAt(i) == ')') {
						StringTokenizer strr = new StringTokenizer(word, ",");

						while (strr.hasMoreTokens() && !choose) {
							fieldname.add(strr.nextToken());
						}
						while (strr.hasMoreTokens() && choose) {
							value.add(strr.nextToken().replace("'", "")
									.replace("\"", ""));
						}
						choose = true;

					} else
						word += line.charAt(i);
				}
				// tableName = str.nextToken().replace("(", "");
				//
				// while (str.hasMoreTokens()) {
				// String word = str.nextToken().replace("'", "").replace("\"",
				// "");
				// if
				// (word.equals(",")||word.equals("(")||word.equals(")")||word.equals(";"))
				// {
				// continue;
				// }
				// else
				// if(word.equalsIgnoreCase("values")||word.equalsIgnoreCase("value"))
				// {
				// check = ! check;
				// }
				// else if(check) {
				// value.add(word.replace("(", "").replace(")", ""));
				// }
				// else {
				// fieldname.add(word.replace("(", "").replace(")",
				// "").replace(";", ""));
				// }
				// }
				if (fieldname.size() == value.size()) {
					entries.clear();
					for (int i = 0; i < fieldname.size(); i++) {
						entries.add(new Field(fieldname.get(i), value.get(i)));
					}
					return true;
				}
				// parameter

			}

		}

		return false;

	}

	public boolean updateStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();

		if (firstWord.equalsIgnoreCase("update")) {
			if (!str.hasMoreTokens())
				return false;

			tableName = str.nextToken();
			if (!str.hasMoreTokens())
				return false;

			String thirdWord = str.nextToken();

			if (thirdWord.equalsIgnoreCase("set")) {
				// parameter
				String fieldName = "", value = "";
				entries.clear();
				String line = "";
				while (str.hasMoreTokens()) {
					String word = str.nextToken();

					if (word.equalsIgnoreCase("where")) {
						break;
					}
					line += word;

				}
				line = line.replaceAll("'", "").replaceAll("\"", "")
						.replace(";", "");
				boolean check = getvar2(line);
				if (!check) {
					return false;
				}
				line = "";
				while (str.hasMoreTokens()) {
					line += str.nextToken();
				}
				line = line.replaceAll("'", "").replaceAll("\"", "")
						.replace(";", "");
				check = getvar(line);
				// fieldName=str.nextToken();
				// String relation = str.nextToken();
				// value = str.nextToken().replaceAll("'", "").replaceAll("\"",
				// "");
				// entry=new Field(fieldName,value,relation.charAt(0));
				return check;

			}

		}

		return false;

	}

	public boolean getvar(String stat) {
		String first = "", second = "", relation = "";
		boolean check = false;
		for (int i = 0; i < stat.length(); i++) {
			if (stat.charAt(i) == '=' || stat.charAt(i) == '>'
					|| stat.charAt(i) == '<') {
				check = true;
				relation += stat.charAt(i);
				continue;
			}
			if (check) {
				second += stat.charAt(i);
			} else {
				first += stat.charAt(i);

			}
		}
		if (first.equals("") || second.equals("") || relation.equals(""))
			return false;

		entry = new Field(first, second, relation.charAt(0));
		return true;
	}

	public boolean getvar2(String stat) {
		StringTokenizer str = new StringTokenizer(stat, ",");
		while (str.hasMoreTokens()) {
			String line = str.nextToken();
			String first = "", second = "", relation = "";
			boolean check = false;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == '=' || line.charAt(i) == '>'
						|| line.charAt(i) == '<') {
					check = true;
					relation += line.charAt(i);
					continue;
				}
				if (check) {
					second += line.charAt(i);
				} else {
					first += line.charAt(i);

				}
			}

			if (!check) {
				return false;
			}
			entries.add(new Field(first, second, relation.charAt(0)));

		}
		return true;

	}
}