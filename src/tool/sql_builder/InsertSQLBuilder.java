package tool.sql_builder;

public class InsertSQLBuilder {
	private String _insert_into = null;
	private String[] _columns = null;
	private String[] _values = null;
	
	InsertSQLBuilder(String insert_into) {
		this._insert_into = String.format("\"%s\"", insert_into);
	}
	
	
	public InsertSQLBuilder columns(String... columns) {
		this._columns = columns;
		
		for (int i = 0; i < this._columns.length; i++)
			this._columns[i] = String.format("\"%s\"", this._columns[i]);
		
		return this;
	}
	
	public InsertSQLBuilder values(String... values) {
		this._values = values;
		
		for (int i = 0; i < this._values.length; i++) 
			this._values[i] = String.format("'%s'", this._values[i]);
			
		return this;
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("INSERT INTO %s ", this._insert_into));
		if (this._columns != null)
			sb.append(String.format("(%s)", String.join(", ", this._columns)));
		sb.append("\n");
		
		sb.append(String.format("VALUES (%s);", String.join(", ", this._values)));
		
		return sb.toString();
	}
	
}
