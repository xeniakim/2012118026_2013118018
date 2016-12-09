package function;

import java.sql.SQLException;
import java.util.Scanner;

import source.MakeConnection;
import source.PostgresConnector;

public class DescribeTable {
	public DescribeTable() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please specify the table name : ");
		String table_name = sc.next();
		
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		PostgresConnector pc = new PostgresConnector(mc);
		
		try {
			pc.showColumns(table_name);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		sc.close();
	}
}
