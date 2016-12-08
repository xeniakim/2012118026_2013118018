package tool.sql_builder;

public class DropSQLBuilder {
	private String[] _drop_table;
	
	public DropSQLBuilder(String... drop_table) {
		this._drop_table = drop_table;
		
		for (int i = 0; i < this._drop_table.length; i++)
			this._drop_table[i] = String.format("\"%s\"", this._drop_table[i]);
			
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("DROP TABLE %s;", String.join(", ", this._drop_table)));
		
		return sb.toString();
	}
}
