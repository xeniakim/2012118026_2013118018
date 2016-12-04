package source;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tool.sql_builder.SQLBuilder;

public class PostgresConnector {
	MakeConnection mc;
	Connection conn;
	
	//MakeConnection 객체를 받아서 사용
	public PostgresConnector(MakeConnection mc) {
		this.mc = mc;
		this.conn = mc.getDB_CONNECTION();
	}
	
	//executeSelect의 실행결과인 String[][]을 규격에 맞추어 출력
	public void printSelectResult(String[][] result) {

		//column 명들 출력
		String columnStr = String.join("|", result[0]);
		System.out.println(new String(new char[columnStr.length()]).replace("\0", "="));//=를 반복출력
		System.out.println(columnStr);
		System.out.println(new String(new char[columnStr.length()]).replace("\0", "="));
		
		//value들 출력
		for (int i = 1; i < result.length; i++)
			System.out.println(String.join(", ", result[i]));
		
		
	}

	//information_schema에서 현재 schema의 table정보 불러옮
	public void showTables() throws SQLException {
		String sql = SQLBuilder
				.select("table_name")
				.schema("information_schema")
				.from("tables")
				.where(String.format("\"table_schema\" = '%s'", this.mc.getDB_SCHEMA()))
				.build();
		
		this.printSelectResult(this.executeSelect(sql));
	}
	
	//information_schema에서 현재 schema의 table_name에 대한 column정보 가져옴
	public void showColumns(String table_name) throws SQLException {
		String sql = SQLBuilder
				.select("column_name", "data_type", "character_maximum_length", "numeric_precision", "numeric_scale")
				.schema("information_schema")
				.from("columns")
				.where(String.format("\"table_name\" = '%s'", table_name))
				.build();
		
		String[][] result = this.executeSelect(sql);
		String[][] output = new String[result.length][3];
		
		//SELECT쿼리로 가져온 정보를 과제 설명PPT에 맞추어 형태 변경
		String[] columns = {"column_name", "data_type", "character_maxumum_length or numeric_precision and scale"};
		output[0] = columns;
		
		for (int i = 1; i < output.length; i++) {
			output[i][0] = result[i][0];
			output[i][1] = result[i][1];
			
			if (result[i][2] == null)
				output[i][2] = String.format("(%s,%s)", result[i][3], result[i][4]);
			else
				output[i][2] = result[i][2];
		}
		
		this.printSelectResult(output);
	}
	
	//SELECT 쿼리를 실행하여 2차원 String 배열로 변환
	public String[][] executeSelect(String sql) throws SQLException {
		Statement st = this.conn.createStatement();
		
		ResultSet rs = st.executeQuery(sql); //SELECT 쿼리 실행
		ResultSetMetaData md = rs.getMetaData(); //쿼리 실행 결과의 메타데이터
		
		ArrayList<String[]> result = new ArrayList<>(); //결과값을 담는 곳
		
		//result에 column data 넣기
		String[] columns = new String[md.getColumnCount()];
		for (int i = 0; i < columns.length; i++)
			columns[i] = md.getColumnLabel(i + 1);
		result.add(columns);
		
		//result에 value data 넣기
		while (rs.next()) {
			String[] values = new String[md.getColumnCount()];
			for (int i = 0; i < md.getColumnCount(); i++)
				values[i] = rs.getString(i + 1);
			
			result.add(values);
		}
		
		//String[][]으로 변환하여 리턴
		return result.toArray(new String[0][0]);
	}
	
	//update된 row의 갯수를 int로 리턴
	public int executeUpdate(String sql) throws SQLException {
		Statement st = this.conn.createStatement();
		return st.executeUpdate(sql);
	}
	
	//작동방식이 update와 같음
	public int executeInsert(String sql) throws SQLException {
		return this.executeUpdate(sql);
	}
	
	public int executeDelete(String sql) throws SQLException {
		return this.executeUpdate(sql);
	}
}
