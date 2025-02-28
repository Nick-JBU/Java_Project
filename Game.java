package final_project_package;
import java.util.Scanner;

public class Game {
	public static void main(String [] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your character's name");
		Character user = new Character();
		System.out.println(user.name);
		boolean running = true;
		System.out.println("1. Display Inventory\n2. Add inventory\n3. Remove inventory");
		while (running) {
			int menu_choice = scan.nextInt();
			if (menu_choice == 1) {
				System.out.println(user.displayInventory());
			}
			else if (menu_choice == 2){
				System.out.println("What item would you like to add?");
				String add_item = scan.next();
				user.addInventory(add_item);
				System.out.println("Item added");
			}
			else if (menu_choice == 3) {
				System.out.println("What item would you like to remove?");
				String remove_item = scan.next();
				user.removeInventory(remove_item);
			}
			else {
				running = false;
			}
		}		
	}
}
