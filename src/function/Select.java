package function;

import java.sql.SQLException;
import java.util.Scanner;

import source.MakeConnection;
import source.PostgresConnector;
import tool.ConditionBuilder;
import tool.sql_builder.SQLBuilder;

public class Select {
	public Select() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please specify the table name : ");
		String table_name = sc.nextLine();
		
		System.out.print("Please specify columns which you want to retrieve (ALL : *) : ");
		String c_input = sc.nextLine();
		
		String[] columns;
		
		if (c_input.replaceAll(" ", "").equals("*")) {
			columns = new String[0];
		} else {
			columns = c_input.replaceAll(" ", "").split(",");
		}
		
		String where = new ConditionBuilder(sc).ask();
		
		System.out.print("Please specify the column name for ordering (Press enter : skip) : ");
		String order_input = sc.nextLine();
		String[] order = (order_input.isEmpty()) ? null : order_input.replaceAll(" ", "").split(",");
		
		String[] order_how = null;
		if (order != null) {
			while (true) {
				System.out.print("Please specify the sorting criteria (Press enter : skip) : ");
				String order_how_input = sc.nextLine();
				order_how = (order_how_input.isEmpty()) ? null : order_how_input.replaceAll(" ", "").split(",");
				
				if (order_how != null && order.length != order_how.length) 
					System.out.println(String.format("Please input same amount of criteria(%s)", order.length));
				else
					break;
			}
		}
		
		String sql = SQLBuilder
				.select(columns)
				.from(table_name)
				.where(where)
				.order_by(order)
				.order_how(order_how)
				.build();
		
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		
		PostgresConnector pc = new PostgresConnector(mc);
		try {
			String[][] result = pc.executeSelect(sql);
			pc.printSelectResult(result);
			System.out.print(String.format("<%s rows selected>", result.length - 1));
		} catch (SQLException e) {
			
			System.out.println("<error detected>" + sql);
		}
	}
}
