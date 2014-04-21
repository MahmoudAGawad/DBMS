public class Field {
 
	private String name;
	private String value;
	private char relation;
 
	public Field() {
 
		this.name = "";
		this.value = "";
		this.relation = '=';
	}
 
	public Field(String Name, String Value, char Relation) {
		// TODO Auto-generated constructor stub
		this.name = Name;
		this.value = Value;
		this.relation = Relation;
	}
 
	public Field(String Name, String Value) {
		// TODO Auto-generated constructor stub
		this.name = Name;
		this.value = Value;
		this.relation = '=';
	}
	public String getName() {
		return name;
	}
 
	public String getValue() {
		return value;
	}
 
	public char getRelation() {
		return relation;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public void setValue(String value) {
		this.value = value;
	}
 
	public void setRelation(char relation) {
		this.relation = relation;
	}
 
}