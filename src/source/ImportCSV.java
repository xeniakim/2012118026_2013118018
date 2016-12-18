package source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImportCSV {

	MakeConnection mc;
	Connection conn;
	String table_name;
	BufferedReader csv_file;
	
	List<String> column_names;
	List<String> column_data_types;
	List<String> primary_keys;
	List<String> notnull_columns;
	
	ImportCSV() {
		this.column_names = new ArrayList<String>();
		this.column_data_types = new ArrayList<String>();
		this.primary_keys = new ArrayList<String>();
		this.notnull_columns = new ArrayList<String>();
		this.mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		this.conn = mc.getDB_CONNECTION();
		try {
			defineTableDescription();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readCSVFile();
	}
	
	public void defineTableDescription() throws SQLException {
		
		//table description 담긴 텍스트 파일 불러오기
		Scanner sc = new Scanner(System.in);
		System.out.println("[Import from CSV]\nPlease specify the filename for table description : ");
        String td = sc.next();
        
        BufferedReader table_description = null;
		try {
			table_description = new BufferedReader(new FileReader("./" + td));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//table name 저장
		String table_name = null;
		
		String line = null;
        try {
			line = table_description.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		String[] pair = line.replaceAll(" ", "").split(":");
		
		table_name = "\"" + pair[1] + "\"";
		//until here
		
		//컬럼명과 컬럼 데이터타입 저장
		while(true) {		
			try {
				line = table_description.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line==null) break;
			if (!line.startsWith("Column")) break;
		    //line을 파싱하는 부분, replaceAll로 공백 삭제
			pair = line.replaceAll(" ", "").split(":");
			this.column_names.add("\"" + pair[1] + "\"");
			
			try {
				line = table_description.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line==null) break;
			if (!line.startsWith("Column")) break;
		    //line을 파싱하는 부분, replaceAll로 공백 삭제
			pair = line.replaceAll(" ", "").split(":");
			this.column_data_types.add(pair[1].toLowerCase());
		}
		
		//Primary key 저장
		if (line.startsWith("PK")) {
			pair = line.replaceAll(" ", "").split(":");
			String[] pk_arr = pair[1].split(",");
			for (int i = 0; i < pk_arr.length; i++) {
				this.primary_keys.add("\"" + pk_arr[i] + "\"");
			}
			
			try {
				line = table_description.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//not null constraint 있는 컬럼 저장
		if (line != null && line.startsWith("Not NULL")) {
			pair = line.replaceAll(" ", "").split(":");
			String[] nn_arr = pair[1].split(",");
			int nn_loc;
			for (int i = 0; i < nn_arr.length; i++) {
				nn_arr[i] = nn_arr[i].replaceAll("Column", "");
				nn_loc = Integer.parseInt(nn_arr[i]) - 1;
				this.notnull_columns.add(this.column_names.get(nn_loc));
			}
		}
		
		
		try {
			table_description.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//MakeConnection 객체에 있는 connection 가져와 sql문 생성
		Statement st = null;
		try {
			st = this.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//사용자가 입력한 table description대로 CREATE
		
		DatabaseMetaData dbm = this.conn.getMetaData();
		// check if table is there
		ResultSet tables = dbm.getTables(null, null, table_name.replaceAll("\"", ""), null);
		if (tables.next()) {
			// Table exists
			System.out.println("Table already exists.");
		}
		else {
			// Table does not exist
			StringBuilder sb = new StringBuilder();
			
			sb.append(String.format("CREATE TABLE %s (", table_name));
			
			int i = 0;
			while (i < this.column_names.size()) {
				
				if (this.column_names != null && this.column_data_types != null) {
					sb.append(String.format("%s %s", this.column_names.get(i), 
							this.column_data_types.get(i)));
					if (this.notnull_columns.contains(this.column_names.get(i))) {
						sb.append(" not null");
					}
					sb.append(", ");
					i++;
				}
			}
			
			sb.append(String.format("PRIMARY KEY (%s)", String.join(",", this.primary_keys)));
						
			sb.append(")");
			
			String CreateTableSQL = sb.toString();
			
			
			
			try {
				st.executeUpdate(CreateTableSQL);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

        this.table_name = table_name;
	}
	
	
	
	//사용자가 입력한 파일 이름의 csv파일 읽어 리턴
	public void readCSVFile() {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please specify the CSV filename : ");
        String csv = sc.next();

        
        BufferedReader csv_file = null;
		try {
			csv_file = new BufferedReader(new FileReader("./" + csv));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.csv_file = csv_file;
	}
	
	
	
	public int insertIntoTable() throws SQLException {
		
		//csv 파일에서 첫 줄만 읽어와 column 이름들 저장
		String line = null;
		try {
			line = csv_file.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String[] csv_column_names = null;
		if (line != null) {
			csv_column_names = line.split(",");
			for (int i = 0; i < csv_column_names.length; i++) {
				csv_column_names[i] = "\"" + csv_column_names[i] + "\"";
			}
		} else {
			return 0;
		}
		//until here
		
		
		//csv파일과 테이블의 컬럼 수가 다를 경우 오류 표출
		if (csv_column_names.length != this.column_names.size()) {
			System.out.println("Data import failure. (The number of columns does not match between the table description and the CSV file.)");
			return 0;
		}
		
		
		
		//csv에 있는 컬럼 순서와 위치대로 primary key 저장
		List<String> csv_primary_keys = new ArrayList<String>();
		List<Integer> csv_primary_key_locations = new ArrayList<Integer>();
		for (int i = 0; i < csv_column_names.length; i++) {
			for (int j = 0; j < this.primary_keys.size(); j++) {
				if (csv_column_names[i].equals(this.primary_keys.get(j))) {
					csv_primary_keys.add(this.primary_keys.get(j));
					csv_primary_key_locations.add(i);
				}
			}
		}
		
		//MakeConnection 객체에 있는 connection 가져와 sql문 생성
		Statement st = null;
		st = this.conn.createStatement();
	
		
		StringBuilder sb = new StringBuilder();
		
		
		//파일에서 한 줄씩 읽어와 검사 후 이상이 없으면 INSERT문 만들고 삽입
		boolean duplicated = false;
		boolean notnull_is_null = false;
		int line_no = 1;
		List<Integer> failed_tuple_numbers = new ArrayList<Integer>();
		List<String> failed_tuple_data = new ArrayList<String>();
		
		while (true) {
			line_no++;
			
			try {
				line = csv_file.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
			if (line != null) {
				//String[] tuple = line.split(",");

				String converted = line.replaceAll(",", ",\0");
				String[] tuple = converted.split(",");
				for (int i = 0; i < tuple.length; i++)
			         tuple[i] = tuple[i].replaceAll("\0", "");
				
				for (int i = 0; i < tuple.length; i++) {
					String data_type = this.column_data_types.get(i);
					if (data_type.startsWith("char") || data_type.startsWith("varchar") || data_type.startsWith("date") || data_type.startsWith("time")) {
						if (!tuple[i].equals("")) {
							tuple[i] = "'" + tuple[i] + "'";
						}
					}
				}
				
				//notnull constraint 위배하는 경우 검사
				List<String> csv_notnull_columns = new ArrayList<String>();
				List<Integer> csv_notnull_column_locations = new ArrayList<Integer>();
				for (int i = 0; i < csv_column_names.length; i++) {
					for (int j = 0; j < this.notnull_columns.size(); j++) {
						if (csv_column_names[i].equals(this.notnull_columns.get(j))) {
							csv_notnull_columns.add(this.notnull_columns.get(j));
							csv_notnull_column_locations.add(i);
						}
					}
				}
				
				for (int i = 0; i < csv_notnull_column_locations.size(); i++) {
					if (tuple[csv_notnull_column_locations.get(i)].equals("")) {
						notnull_is_null = true;
				        break;
					}
				}
				
				if (notnull_is_null) {
					failed_tuple_numbers.add(line_no);
					failed_tuple_data.add(line);
					notnull_is_null = false;
					continue;
				}
				
				//primary key value가 중복되는 레코드 검사
				Statement isThereRecord = null;
				for (int i = 0; i < csv_primary_keys.size(); i++) {
					if (!tuple[csv_primary_key_locations.get(i)].equals("")) {
						String query = "SELECT * FROM" + table_name + 
								"WHERE " + csv_primary_keys.get(i) + "= " + 
								tuple[csv_primary_key_locations.get(i)];
						isThereRecord = conn.createStatement();
						ResultSet rs = isThereRecord.executeQuery(query);
					    if (rs != null) {
					    	while (rs.next()) {
					        	duplicated = true;
					        	break;
					    	}
					    } else {
					        continue;
					    }
					}
				}
			
				if (duplicated) {
			        failed_tuple_numbers.add(line_no);
			        failed_tuple_data.add(line);
			        duplicated = false;
					continue;
				}
				
				for (int i = 0; i < tuple.length; i++) {
					if (tuple[i] == null || tuple[i].equals("")) {
						tuple[i] = null;
					}
				}
				
				sb.delete(0, sb.length());
				sb.append(String.format("INSERT INTO %s ", this.table_name));
				sb.append(String.format("(%s) ", String.join(", ", csv_column_names)));
				sb.append(String.format("VALUES (%s);", String.join(", ", tuple)));
				
				String InsertSQL = sb.toString();
				st.executeUpdate(InsertSQL);

			} else {
				break;
			}
			
		}
		
		System.out.println(String.format("Data import completed. " +
				"(Insertion Success : %s, Insertion Failure : %s)", 
				line_no - 2 - failed_tuple_numbers.size(), failed_tuple_numbers.size()));
		for (int i = 0; i < failed_tuple_numbers.size(); i++) {
			System.out.println(String.format("Failed tuple : %d line in CSV - %s", 
					failed_tuple_numbers.get(i), failed_tuple_data.get(i)));
		}
		return 1;
	}
}
