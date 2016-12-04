package source;

import java.util.Scanner;

public class Menu {
	public void showMenu() {
		
		Scanner mn = new Scanner(System.in);
		System.out.println("Please input the instruction number "
				+ "(1: Import from CSV, 2: Export to CSV, 3: Manipulate Data, 4: Exit) : ");
        int menu_selection = mn.nextInt();
	
        
        switch(menu_selection) {
        case 1:
        	/*각 case에서 해당 기능으로 연결해주기*/
        case 2:
        	
        case 3:
        	
        case 4:
        	
        default:
        		
        }
	}
	
	
}
