import java.util.ArrayList;

public class Tables {
	
	private ArrayList<Col> fields;
	private String tableName;
	public Tables(){
		fields=new ArrayList<Col>();
		tableName="";
	}
	
	public Tables(ArrayList<Col> arr){
		fields=arr;
		tableName="";
	}
	
	public ArrayList<Col> getFields(){
		return fields;
	}
	
	public void setFields(ArrayList<Col> arr){
		fields=arr;
	}
	
	public String getTableName(){
		return tableName;
	}
	public void setTableName(String name){
		tableName=name;
	}
}
