package source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MakeConnection {
	private static final String DB_DRIVER;
	private static final String DB_CONNECTION_URL;
	private static final String DB_USER;
	private static final String DB_PASSWORD;
	
	
	public int takeConnectionInfo() {
		BufferedReader connection_info;
		try {
			connection_info = new BufferedReader(new FileReader(".../connection.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
        while(true) {
            String line = null;
			try {
				line = connection_info.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (line==null) break;
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
         * TODO: 파일 내 띄어쓰기 처리하기
         */
        return 1;
	}
}


