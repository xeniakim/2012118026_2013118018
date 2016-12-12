package source;

import java.util.Scanner;

import function.Delete;
import function.DescribeTable;
import function.Drop;
import function.Insert;
import function.Select;
import function.ShowTables;
import function.Update;

import source.ImportCSV;

public class Menu {
	Scanner mn;
	
	public void showMenu() {
		
		mn = new Scanner(System.in);
		System.out.print("Please input the instruction number "
				+ "(1: Import from CSV, 2: Export to CSV, 3: Manipulate Data, 4: Exit) : ");
        int menu_selection = mn.nextInt();
	
        /*각 case에서 해당 기능으로 연결해주기*/
        switch(menu_selection) {
        case 1:
        	importFromCSV();
        	break;
        case 2:
        	new ExportCSV().export();
        	break;
        case 3:
        	while (true)
        		if (this.manipulateData())
        			break;
        	break;
        case 4:
        	System.exit(0);
        default:
        	System.out.println("WRONG!");
        		
        }
	}
	
	public void importFromCSV() {
		ImportCSV importing = new ImportCSV();
		importing.insertIntoTable();
	}
	
	public boolean manipulateData() {
    	System.out.print("Please input the instruction number "
    			+ "(1: Show Tables, 2: Describe Table, 3: Select, 4: Insert, "
    			+ "5: Delete, 6: Update, 7: Drop Table, 8: Back to main) : ");
    	
    	switch(mn.nextInt()) {
    	case 1:
    		new ShowTables();
    		break;
    	case 2:
    		new DescribeTable();
    		break;
    	case 3:
    		new Select();
    		break;
    	case 4:
    		new Insert();
    		break;
    	case 5:
    		new Delete();
    		break;
    	case 6:
    		new Update();
    		break;
    	case 7:
    		new Drop();
    		break;
    	case 8:
    		System.out.println("Back to main");
    		return true;
    	default:
    		System.out.println("WRONG!");
    	}
    	
    	return false;
	}
	
}
