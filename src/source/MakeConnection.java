package source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MakeConnection {
	private static String DB_DRIVER;
	private static String DB_CONNECTION_URL;
	private static String DB_USER;
	private static String DB_PASSWORD;
	
	//추가됨
	private static String DB_SCHEMA;
	
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
            
            System.out.println(line);
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
}


