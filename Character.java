package final_project_package;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Character {
	Scanner scan = new Scanner(System.in);
	public List<String> user_items;
	public List<String> user_moves;
	public String name;
	
	public Character() {
		user_items = new ArrayList<>();
        user_moves = new ArrayList<>();
        name = scan.nextLine();
        
        user_items.add("Sword");
        user_items.add("Potion");
        user_items.add("Map");
	}
	public List<String> displayInventory(){
		return user_items;
	}
	public void addInventory (String item) {
		user_items.add(item);
	}
	public void removeInventory (String item){
		if (user_items.contains(item)) {
			user_items.remove(item);
		}
	}
}
