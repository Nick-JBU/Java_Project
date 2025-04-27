package final_project_package;
import java.util.Scanner;

import javax.swing.JFrame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.sql.*;

public class Game {
	public static void main(String [] args) throws Exception {
		/*
		String url = "jdbc:mysql://localhost:3306/project_db";
		String username = "root";
		String password = "";
		String query = "select*from users";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, username, password);
		System.out.println("Connection Established Successfully");
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		String user_name = rs.getString("name");
		String char_name = rs.getString("character_name");
		System.out.println(user_name);
		System.out.println(char_name);
		
		
		/*
		System.out.println("Enter your username: ");
		String game_username = scan.nextLine();
		String username_query = "SELECT * FROM users WHERE name = game_username";
		rs = st.executeQuery(username_query);
		System.out.println(username_query);
		rs.next();
		System.out.println("Enter your password: ");
		String game_password = scan.nextLine();
		System.out.println("Enter your character's name");
		Character user = new Character();
		//System.out.println(user.name);*/
		Game_Login loginFrame = new Game_Login();
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(500, 300);
		//myFrame.pack();
		loginFrame.setVisible(true);
		
		Scanner scan = new Scanner(System.in);
		boolean running = true;
		System.out.println("Enter character name: ");
		
		Display_Text myFrame = new Display_Text();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
		Character user = new Character("hello");
		
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

/*
boolean fileExists = false;
try {
	File myFile = new File("gamefile.txt");
	if(myFile.createNewFile()) {
		System.out.println(myFile.getName());
	}
	else {
		System.out.println("File already exists.");
		fileExists = true;
	}
}
catch(IOException e) {
		System.out.println("An error occurred.");
		e.printStackTrace();
	}

Scanner scan = new Scanner(System.in);
System.out.println("Enter your character's name");
Character user = new Character();
//System.out.println(user.name);
try {
	FileWriter myWriter = new FileWriter("gamefile.txt");
	myWriter.write(user.name);
	myWriter.close();
	System.out.println("Successfully written");
}
catch(IOException e) {
	System.out.println("An error occurred.");
	e.printStackTrace();
}
*/
