package tool.sql_builder;

public class DeleteSQLBuilder {
	private String _delete_from = null;
	private String _where = null;
	
	DeleteSQLBuilder(String delete_from) {
		this._delete_from = String.format("\"%s\"", delete_from);
	}
	
	public DeleteSQLBuilder where(String where) {
		this._where = where;
		
		return this;
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("DELETE FROM %s", this._delete_from));
		
		if (this._where != null) {
			sb.append(String.format("\nWHERE %s;", this._where));
		} else {
			sb.append(";");
		}
		
		return sb.toString();
	}
	
	
}
