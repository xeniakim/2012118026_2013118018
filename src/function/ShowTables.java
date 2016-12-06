package function;

import java.sql.SQLException;

import source.MakeConnection;
import source.PostgresConnector;

public class ShowTables {
	public ShowTables() {
		MakeConnection mc = new MakeConnection();
		mc.takeConnectionInfo();
		mc.Connect();
		
		PostgresConnector pc = new PostgresConnector(mc);
		try {
			pc.showTables();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
