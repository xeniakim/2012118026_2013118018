package function;

import java.sql.SQLException;
import java.util.Scanner;

import source.MakeConnection;
import source.PostgresConnector;
import tool.sql_builder.SQLBuilder;

public class Insert {
	public Insert() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please specify the table name : ");
		String table_name = sc.nextLine();
		
		System.out.print("Please specify all columns in order of which you want to insert : ");
		String c_input = sc.nextLine();
		String[] columns = c_input.replaceAll(" ", "").split(",");
		
		System.out.print("Please specify values for each column : ");
		String[] values = sc.nextLine().replaceAll(" ", "").split(",");
		
		String sql = SQLBuilder
				.insert_into(table_name)
				.columns(columns)
				.values(values)
				.build();
		
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		
		PostgresConnector pc = new PostgresConnector(mc);
		try {
			int result = pc.executeInsert(sql);
			System.out.println(String.format("<%s rows inserted>", result));
		} catch (SQLException e) {
			System.out.println("<0 row inserted due to error>");
		}
		
		sc.close();
	}
}
