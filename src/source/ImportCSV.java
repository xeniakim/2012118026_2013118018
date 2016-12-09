package source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
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
	
	ImportCSV() {
		this.mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		this.conn = mc.getDB_CONNECTION();
		defineTableDescription();
		readCSVFile();
	}
	
	public void defineTableDescription() {
		
		//table description 담긴 텍스트 파일 불러오기
		Scanner sc = new Scanner(System.in);
		System.out.println("[Import from CSV]\nPlease specify the filename for table description : ");
        String td = sc.next();
        
        BufferedReader table_description = null;
		try {
			table_description = new BufferedReader(new FileReader(td));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sc.close();
		
		//각 정보 리스트에 저장
		String table_name = null;
		List<String> column_names = new ArrayList<String>();
		List<String> column_data_types = new ArrayList<String>();
		List<String> primary_keys = new ArrayList<String>();
		List<String> notnull_columns = new ArrayList<String>();
		
		//table name 저장
        String line = null;
        try {
			line = table_description.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //line을 파싱하는 부분, replaceAll로 공백 삭제
		String[] pair = line.replaceAll(" ", "").split(":");
		
		table_name = pair[1];
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
			column_names.add(pair[1]);
			
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
			column_data_types.add(pair[1]);
		}
		
		//Primary key 저장
		if (line.startsWith("PK")) {
			pair = line.replaceAll(" ", "").split(":");
			String[] arr = pair[1].split(",");
			for (int i = 0; i < arr.length; i++) {
				primary_keys.add(arr[i]);
			}
		}
		
		//not null constraint 있는 컬럼 저장
		if (line.startsWith("Not NULL")) {
			pair = line.replaceAll(" ", "").split(":");
			String[] arr = pair[1].split(",");
			for (int i = 0; i < pair[1].length(); i++) {
				notnull_columns.add(arr[i]);
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
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("CREATE TABLE %s (", table_name));
		
		int i = 0;
		while (i < column_names.size()) {
			
			if (column_names != null && column_data_types != null) {
				sb.append(String.format("%s %s", column_names.get(i), column_data_types.get(i)));
				if (notnull_columns.contains(column_names)) {
					sb.append(" not null");
				}
				sb.append(", ");
			}
		}
		
		sb.append(String.format("PRIMARY KEY (%s)", String.join(",", primary_keys)));
					
		sb.append(")");
		
		String CreateTableSQL = sb.toString();
		try {
			st.executeUpdate(CreateTableSQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		sc.close();
		
		this.csv_file = csv_file;
	}
	
	
	
	public int insertIntoTable() {
		
		//csv 파일에서 첫 줄만 읽어와 column 이름들 저장
		String line = null;
		try {
			line = csv_file.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String[] column_names = null;
		if (line != null) {
			column_names = line.split(",");
		} else {
			return 0;
		}
		//until here
		
		
		//MakeConnection 객체에 있는 connection 가져와 sql문 생성
		Statement st = null;
		try {
			st = this.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		StringBuilder sb = new StringBuilder();
		
		//파일에서 한 줄씩 읽어와 INSERT문 만듦
		while (true) {
			try {
				line = csv_file.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line != null) {
				sb.append(String.format("INSERT INTO %s ", table_name));
				sb.append(String.format("(%s) ", String.join(", ", column_names)));
				sb.append(String.format("VALUES (%s);", line));
			} else {
				break;
			}
		}
		
		String InsertSQL = sb.toString();
		try {
			st.executeUpdate(InsertSQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 1;
	}
	

}


