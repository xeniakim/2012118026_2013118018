package function;

import java.sql.SQLException;
import java.util.Scanner;

import source.MakeConnection;
import source.PostgresConnector;
import tool.ConditionBuilder;
import tool.sql_builder.SQLBuilder;

public class Delete {
	public Delete() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please specify the table name : ");
		String table_name = sc.nextLine();
		
		String where = new ConditionBuilder(sc).ask();
		
		String sql = SQLBuilder
				.delete_from(table_name)
				.where(where)
				.build();
		
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		
		PostgresConnector pc = new PostgresConnector(mc);
		try {
			int result = pc.executeDelete(sql);
			System.out.print(String.format("<%s rows deleted>", result));
		} catch (SQLException e) {
			System.out.println("<0 row inserted due to error>");
		}
	}
}
