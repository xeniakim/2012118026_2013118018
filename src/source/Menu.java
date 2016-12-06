package source;

import java.util.Scanner;

import function.DescribeTable;
import function.ShowTables;

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
        	System.out.println("Import from CSV");
        	break;
        case 2:
        	System.out.println("Export to CSV");
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
    		System.out.println("Select");
    		break;
    	case 4:
    		System.out.println("Insert");
    		break;
    	case 5:
    		System.out.println("Delete");
    		break;
    	case 6:
    		System.out.println("Update");
    		break;
    	case 7:
    		System.out.println("Drop Table");
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
