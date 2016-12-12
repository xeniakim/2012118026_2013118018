package source;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import tool.sql_builder.SQLBuilder;

public class ExportCSV {
	//DB에서 table_name의 이름을 가지는 table의 데이터를 2차원 String배열로 리턴
	private String[][] getDataFromDB(String table_name) {
		String[][] result = null;
		
		String sql = SQLBuilder
				.select()
				.from(table_name)
				.build();
		
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		
		PostgresConnector pc = new PostgresConnector(mc);
		
		try {
			result = pc.executeSelect(sql);
		} catch (SQLException e) {
			System.out.println("<error detected>");
		}
		
		return result;
	}
	
	//2차원 String배열을 file_name에 csv형식으로 저장
	private int saveDataToCSV(String[][] data, String file_name) {
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(file_name));
			
			for (int i = 0; i < data.length; i++) {
				bw.write(String.join(",", data[i]));
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			return 0;
		}
		
		return 1;
	}
	
	//외부(메뉴)에서 호출되는 부분
	public void export() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Export to CSV");
		System.out.print("Please specify the table name : ");
		String table_name = sc.nextLine();
		
		System.out.print("Please specify the CSV filename : ");
		String file_name = sc.nextLine();
		
		String[][] data = this.getDataFromDB(table_name);
		
		if (data != null && this.saveDataToCSV(data, file_name) == 1) {
			System.out.println("Data export completed.");
		} else {
			System.out.println("Failed to export.");
		}
		
	}
	
	//작동 테스트용 메인함수
	public static void main(String[] args) {
		ExportCSV e = new ExportCSV();
		e.export();
	}
}
