package tool;

import java.util.Scanner;

//WHERE에 들어갈 조건문을 만들어 주는 클래스 new ConditionBuilder().ask()로 사용
public class ConditionBuilder {
	private Scanner sc;
	private StringBuilder output = new StringBuilder();
	
	//column을 골라보아
	private final String COLUMNQUESTION = "Please specify the column which you want to make condition(Press enter : skip) : ";
	//연산자를 골라보아
	private final String OPSQUESTION = "Please specify the condition (1: =, 2: >, 3: < , 4: >=, 5: <=, 6: !=) : ";
	//어떤 값이랑 비교할거니?
	private final String VALUEQUESTION = "Please specify the condition value (%s ?) : ";
	//더 할거니?
	private final String BOOLOPSQUESTION = "Please specify the condition (1: AND, 2: OR, 3: finish) : ";
	
	public ConditionBuilder(Scanner scanner) {
		this.sc = scanner;
		
		if(sc.hasNextLine())
			sc.nextLine();
	}
	
	public String ask() {
		
		boolean finished = false;
		
		while (!finished) {

			try {
				System.out.print(this.COLUMNQUESTION);
				String opsAnswer = sc.nextLine();
				if (opsAnswer.isEmpty()) {
					return null;
				}
					 
				
				output.append(String.format("\"%s\" ", opsAnswer));
				
				System.out.print(this.OPSQUESTION);
				switch (sc.next()) {
				case "1":
					output.append("= ");
					break;
				case "2":
					output.append("> ");
					break;
				case "3":
					output.append("< ");
					break;
				case "4":
					output.append(">= ");
					break;
				case "5":
					output.append("<= ");
					break;
				case "6":
					output.append("!= ");
					break;
				default:
					break;
				}
				
				System.out.print(String.format(this.VALUEQUESTION,
						this.output.toString().replaceAll("'", "").replaceAll("\"", "")));
				
				this.output.append(String.format("'%s' ", sc.next()));
				
				System.out.print(this.BOOLOPSQUESTION);
				switch (sc.next()) {
				case "1":
					this.output.append("AND ");
					break;
				case "2":
					this.output.append("OR ");
					break;
				case "3":
					finished = true;
					if(sc.hasNextLine())
						sc.nextLine();
					break;
				}
			} finally {

			}
		} 
		
		return this.output.toString();
	}
	
}
