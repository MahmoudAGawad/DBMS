import java.util.ArrayList;
 
 
public interface DBMS {
 
	public void createDateBase ( String name );
	public void createTable ( String dataBaseName , String tableName , ArrayList<Field> entries );
	public void insert ( String dataBaseName , String tableName , ArrayList<Field> entries);
	public ArrayList<Col> selectAll ( String dataBaseName , String tableName );
	public void select( String dataBaseName , String tableName , Field entry );
	public void update ( String dataBaseName , String tableName , ArrayList<Field> entries , Field entry );
	public void delete( String dataBaseName , String tableName , Field entry );
 
 
}