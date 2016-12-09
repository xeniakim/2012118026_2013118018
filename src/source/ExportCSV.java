package source;

import java.util.Scanner;

public class ExportCSV {
	//DB에서 table_name의 이름을 가지는 table의 데이터를 2차원 String배열로 리턴
	private String[][] getDataFromDB(String table_name) {
		return null;
	}
	
	//2차원 String배열을 file_name에 csv형식으로 저장
	private int saveDataToCSV(String[][] data, String file_name) {
		return 0;
	}
	
	//외부(메뉴)에서 호출되는 부분
	public void export() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Export to CSV]");
		System.out.print("Please specify the table name : ");
		String table_name = 
	}
	
	//작동 테스트용 메인함수
	public static void main(String[] args) {
		
	}
}
