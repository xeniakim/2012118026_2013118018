package source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MakeConnection {
	
	private Connection DB_CONNECTION;
	private String DB_CONNECTION_URL;
	private String DB_USER;
	private String DB_PASSWORD;
	
	//추가됨
	private String DB_SCHEMA;

	
	public int Connect() {
//		String DB_DRIVER = "org.postgresql.Driver";
//		try {
//			Class.forName(DB_DRIVER);
//		} catch (ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		Properties props = new Properties();
		props.setProperty("user",this.DB_USER);
		props.setProperty("password",this.DB_PASSWORD);
		
		try {
			this.DB_CONNECTION = DriverManager.getConnection(this.DB_CONNECTION_URL, props);
			this.DB_CONNECTION.setSchema(this.DB_SCHEMA);
			this.DB_CONNECTION.setAutoCommit(true);
			
			return 1; //성공하면 1
		} catch (SQLException e) {
			e.printStackTrace();
			return 0; //실패하면 0
		}
	}
	
	public int takeConnectionInfo() {
		BufferedReader connection_info;
		try {
			connection_info = new BufferedReader(new FileReader("./connection.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		String ip = null, db_name = null;
		
        while(true) {
            String line = null;
			try {
				line = connection_info.readLine();
				if (line==null) break;
				
				//line을 파싱하는 부분, replaceAll로 공백 삭제
				String[] pair = line.replaceAll(" ", "").split(":");
				
				switch(pair[0]) {
				case "IP":
					ip = pair[1];
					break;
				case "DB_NAME":
					db_name = pair[1];
					break;
				case "SCHEMA_NAME":
					this.DB_SCHEMA = pair[1];
					break;
				case "ID":
					this.DB_USER = pair[1];
					break;
				case "PW":
					this.DB_PASSWORD = pair[1];
					break;
				}
				
				this.DB_CONNECTION_URL = String.format("jdbc:postgresql://%s/%s", ip, db_name);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        
        try {
			connection_info.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
        /*
         * TODO: 파일 내 띄어쓰기 처리하기 --replaceAll로 처리
         */
        return 1;
	}

	public Connection getDB_CONNECTION() {
		return DB_CONNECTION;
	}

	public String getDB_CONNECTION_URL() {
		return DB_CONNECTION_URL;
	}

	public String getDB_USER() {
		return DB_USER;
	}

	public  String getDB_PASSWORD() {
		return DB_PASSWORD;
	}

	public String getDB_SCHEMA() {
		return DB_SCHEMA;
	}
}


