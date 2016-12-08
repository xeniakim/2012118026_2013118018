package tool.sql_builder;


//SQL을 만들어주는 역할
public class SQLBuilder  {
	public static final String ASCENDING_ORDER = "ASC";
	public static final String DESCENDING_ORDER = "DESC";
	
	private SQLBuilder() {}
	
	public static SelectSQLBuilder select(String... select) {
		return new SelectSQLBuilder(select);
	}
	
	public static InsertSQLBuilder insert_into(String insert_into) {
		return new InsertSQLBuilder(insert_into);
	}
	
	public static UpdateSQLBuilder update(String update) {
		return new UpdateSQLBuilder(update);
	}
	
	public static DeleteSQLBuilder delete_from(String delete_from) {
		return new DeleteSQLBuilder(delete_from);
	}
	
	public static DropSQLBuilder drop_table(String... drop_table) {
		return new DropSQLBuilder(drop_table);
	}
	
	
}
