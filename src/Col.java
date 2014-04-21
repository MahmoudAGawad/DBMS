import java.util.ArrayList;
 
 
public class Col {
 
	private String fieldName ;
	private String fieldType ;
	private ArrayList<String> elements;
 
	public Col() {
		// TODO Auto-generated constructor stub
		fieldName = "";
		fieldType = "";
		elements = new ArrayList<>();
	}
	public Col(String name , String Type){
		fieldName = name;
		fieldType = Type;
		elements = new ArrayList<>();
 
 
	}
	public Col(String name , String Type , ArrayList<String> array){
		fieldName = name;
		fieldType = Type;
		elements = array;
 
	}
	public String getFieldName(){
		return fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public ArrayList<String> getElements() {
		return elements;
	}
	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
 
 
 
	public String get(int index){
		return elements.get(index);
 
	}
 
	public void setElement(int index,String val){
		  elements.set(index, val);
		 }
 
	public void addElem(String element){
		elements.add(element);
	}
	public void addElemAt(String element , int index){
		elements.add(index, element);
	}
	public int getLen(){
		return elements.size();
	}
	public void removeIndex(int index){
		elements.remove(index);
	}
 
	public void addElemByDefault(){
		if (fieldType.equalsIgnoreCase("string")) {
			elements.add("Null");
		}
		else if(fieldType.equalsIgnoreCase("double")) {
			elements.add("0.0");
		}
		else {
			elements.add("0");
		}
	}
}