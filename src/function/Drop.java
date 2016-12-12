package function;

import java.sql.SQLException;
import java.util.Scanner;

import source.MakeConnection;
import source.PostgresConnector;
import tool.ConditionBuilder;
import tool.sql_builder.SQLBuilder;

public class Drop {
	public Drop() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please specify the table name : ");
		String table_name = sc.nextLine();
		
		String sql = SQLBuilder
				.drop_table(table_name)
				.build();
		
		System.out.print("If you delete this table, it is not guaranteed to recover again."
				+ " Are you sure you want to delete this table (Y: yes, N, no)? ");
		
		if (sc.nextLine().replaceAll(" ", "").equals("Y")) {
		
			MakeConnection mc = new MakeConnection();
			mc.takeConnectionInfo();
			mc.Connect();
			
			PostgresConnector pc = new PostgresConnector(mc);
			try {
				pc.executeDrop(sql);
				System.out.println(String.format("<The table %s is deleted>", String.join(", ", table_name)));
			} catch (SQLException e) {
				System.out.println("<0 table dropped due to error>");
			}
		}
		
	}
}
