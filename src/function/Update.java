package function;

import java.sql.SQLException;
import java.util.Scanner;

import source.MakeConnection;
import source.PostgresConnector;
import tool.ConditionBuilder;
import tool.sql_builder.SQLBuilder;

public class Update {
	public Update() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please specify the table name : ");
		String table_name = sc.nextLine();
		
		String where = new ConditionBuilder(sc).ask();
		
		System.out.print("Please specify column names which you want to update : ");
		String c_input = sc.nextLine();
		String[] columns = c_input.replaceAll(" ", "").split(",");
		
		System.out.print("Please specify the value which you want to put : ");
		String[] values = sc.nextLine().replaceAll(" ", "").split(",");
		
		String sql = SQLBuilder
				.update(table_name)
				.columns(columns)
				.values(values)
				.where(where)
				.build();
		
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		
		PostgresConnector pc = new PostgresConnector(mc);
		try {
			int result = pc.executeInsert(sql);
			System.out.print(String.format("<%s rows updated>", result));
		} catch (SQLException e) {
			System.out.println("<0 row updated due to error>");
		}
	}
}
