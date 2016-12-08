package tool.sql_builder;

public class SelectSQLBuilder {
	private String[] _select = null;
	private String _from = null;
	private String _schema = null;
	private String _where = null;
	private String[] _order_by = null;
	private String[] _order_how = null;
	private String _group_by = null;
	private String _having = null;
	
	SelectSQLBuilder(String... select) {
		if (select.length == 0) {
			this._select = new String[1];
			this._select[0] = "*";
		} else {
			this._select = select;
			
			for (int i = 0; i < this._select.length; i++)
				this._select[i] = String.format("\"%s\"", this._select[i]);
		}
	}
	
	public SelectSQLBuilder from(String from) {
		this._from =  String.format("\"%s\"", from);
		return this;
	}
	
	public SelectSQLBuilder schema(String schema) {
		this._schema =  schema;
		return this;
	}
	
	public SelectSQLBuilder where(String where) {
		this._where = where;
		return this;
	}
	
	public SelectSQLBuilder order_by(String... order_by) {
		this._order_by = order_by;
		
		if (this._order_by != null) {
			for (int i = 0; i < this._order_by.length; i++)
				this._order_by[i] = String.format("\"%s\"", this._order_by[i]);
		}
		
		return this;
	}
	
	//어떤 방식으로 정렬할지 입력
	public SelectSQLBuilder order_how(String... order_how) {
		this._order_how = order_how;
		return this;
	}
	
	
	//Group_by와 having은 과제에 없어서 다행
	public SelectSQLBuilder group_by(String group_by) {
		this._group_by = group_by;
		return this;
	}
	
	public SelectSQLBuilder having(String having) {
		this._having = having;
		return this;
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("SELECT %s \n", String.join(", ", this._select)));
		
		if (this._schema == null)
			sb.append(String.format("FROM %s\n", this._from));
		else
			sb.append(String.format("FROM %s.%s\n", this._schema, this._from));
		
		if (this._where != null)
			sb.append(String.format("WHERE %s\n", this._where));
		
		if (this._order_by != null) { //order_by가 지정되었는데
			if (this._order_how == null) { //정렬 방식은 지정되지 않았다면
				sb.append(String.format("ORDER BY %s\n", String.join(", ", this._select)));
			} else { //정렬 방식도 지정되었다면
				String[] pairs = new String[this._order_by.length]; //(column명 정렬방식) pair
				for (int i = 0; i < this._order_by.length; i++) {
					pairs[i] = String.format("%s %s", this._order_by[i], this._order_how[i]);
				}
				
				sb.append(String.format("ORDER BY %s\n", String.join(", ", pairs)));
			}
		}
		
		if (this._group_by != null)
			sb.append(String.format("GROUP BY %s\n", this._group_by));
		if (this._having != null)
			sb.append(String.format("HAVING %s\n", this._having));
		
		sb.replace(sb.length() - 1, sb.length() - 1, ";");
		
		return sb.toString();
	}
}