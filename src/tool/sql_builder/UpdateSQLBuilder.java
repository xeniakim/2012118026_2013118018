package tool.sql_builder;

public class UpdateSQLBuilder {
	private String _update = null;
	private String[] _columns = null;
	private String[] _values = null;
	private String _where = null;
	
	UpdateSQLBuilder(String update) {
		this._update = String.format("\"%s\"", update);
	}
	
	public UpdateSQLBuilder columns(String... columns) {
		this._columns = columns;
		
		for (int i = 0; i < this._columns.length; i++)
			this._columns[i] = String.format("\"%s\"", this._columns[i]);
		
		return this;
	}
	
	public UpdateSQLBuilder values(String... values) {
		this._values = values;
		
		for (int i = 0; i < this._values.length; i++) 
			this._values[i] = String.format("'%s'", this._values[i]);
			
		return this;
	}
	
	public UpdateSQLBuilder where(String where) {
		this._where = where;
		
		return this;
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("UPDATE %s ", this._update));
		sb.append(String.format("SET (%s) = (%s)", String.join(", ", this._columns), String.join(", ", this._values)));
		
		if (this._where != null) {
			sb.append(String.format("\nWHERE %s;", this._where));
		} else {
			sb.append(";");
		}
		
		return sb.toString();
	}
	
}
