package final_project_package;
import java.util.Scanner;

public abstract class NPC {
	boolean interact() {
		return true;
		//return true if user is close to npc and interacts
		//return false otherwise
	}
	abstract void setDetails();
	abstract String prompt();
	
}

class QuestGiver extends NPC{
	void setDetails() {
		String name = "Mike";
	}
	String prompt(){
		return ("Hello Player, welcome to XXX, here is your first quest: ");
	}
}

class Reg_NPC extends NPC{
	void setDetails() {
		String name = "NPC";
	}
	String prompt() {
		return ("...");
	}
}

class Shop_NPC extends NPC{
	void setDetails() {
		String name = "Merchant";
	}
	String prompt() {
		return "Welcome to the shop";
	}
	
	void inventory() {} //Array list with items and prices
}