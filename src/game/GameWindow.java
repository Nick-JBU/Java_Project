package game;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameWindow {
	
	public static String user_name;
	public String password;
	public static String char_name;
	public static String gender;
	public static String item_1;
	public static String item_2;
	public static String item_3;
	public static String item_4;
	public static String item_5;
	public static String item_6;
	public static int item_count;
	public static int money;
	
	public static String getUserName() {
	    return user_name;
	}

	public static String getCharName() {
	    return char_name;
	}

	public static String getGender() {
	    return gender;
	}

	public static String getItem1() {
	    return item_1;
	}
	public static String getItem2() {
	    return item_2;
	}
	public static String getItem3() {
	    return item_3;
	}
	public static String getItem4() {
	    return item_4;
	}
	public static String getItem5() {
	    return item_5;
	}

	public static String getItem6() {
	    return item_6;
	}

	public static int getItemCount() {
	    return item_count;
	}

	public static int getMoney() {
	    return money;
	}
	
    public static void main(String[] args) {
    	JFrame dummyParent = new JFrame();
    	Login loginFrame = new Login(dummyParent);
    	loginFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	//loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loginFrame.pack();
		loginFrame.setSize(800, 600);
		loginFrame.setVisible(true);
		String name = loginFrame.loggedUsername();
		user_name = name;
		System.out.println(name);
		
		//get info from database
		String url = "jdbc:mysql://localhost:3306/project_db";
		String username = "root";
		String password = "";
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			String get_info = "SELECT character_name, gender, item_1, item_2, item_3, item_4, item_5, item_6, item_count, money FROM users WHERE name = ?";
		    PreparedStatement preparedStatement = connection.prepareStatement(get_info);
		    preparedStatement.setString(1, name);
		    ResultSet resultSet = preparedStatement.executeQuery();
		    if(resultSet.next()){
		    	char_name = resultSet.getString("character_name");
		    	gender = resultSet.getString("gender");
		    	item_1 = resultSet.getString("item_1");
		    	item_2 = resultSet.getString("item_2");
		    	item_3 = resultSet.getString("item_3");
		    	item_4 = resultSet.getString("item_4");
		    	item_5 = resultSet.getString("item_5");
		    	item_6 = resultSet.getString("item_6");
		    	item_count = resultSet.getInt("item_count");
		    	money = resultSet.getInt("money");
		    }
		    resultSet.close();
		    preparedStatement.close();
		    connection.close();
		    
		    //dispose();

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//finish getting info
		
		System.out.println("Character name: " + char_name);
		System.out.println("Gender: " + gender);
		System.out.println("Item_1: " + item_1);
		System.out.println("Item count: " + item_count);
		System.out.println("Money: " + money);
		
		boolean loggedIn = loginFrame.isLoggedIn();
		if (loggedIn == true) {
	        JFrame frame = new JFrame("Wanderer");

	        try {
	        	BufferedImage icon = ImageIO.read(GameWindow.class.getResourceAsStream("/resources/icons/Logo2.png"));
	            frame.setIconImage(icon);
	        } catch (IOException | NullPointerException e) {
	            System.err.println("Icon failed to load.");
	        }
	        //Code block for updating database when closing file
	        frame.addWindowListener(new java.awt.event.WindowAdapter() {
	            @Override
	            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            	System.out.println("Window is closing... ");
	                try {
	                    String url = "jdbc:mysql://localhost:3306/project_db";
	                    String username = "root";
	                    String password = "";

	                    Connection connection = DriverManager.getConnection(url, username, password);
	                    String updateQuery = "UPDATE users SET character_name = ?, gender = ?, item_1 = ?, item_2 = ?, item_3 = ?, item_4 = ?, item_5 = ?, item_6 = ?, item_count = ?, money = ? WHERE name = ?";
	                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

	                    preparedStatement.setString(1, GameWindow.getCharName());
	                    preparedStatement.setString(2, GameWindow.getGender());
	                    preparedStatement.setString(3, GameWindow.getItem1());
	                    preparedStatement.setString(4, GameWindow.getItem2());
	                    preparedStatement.setString(5, GameWindow.getItem3());
	                    preparedStatement.setString(6, GameWindow.getItem4());
	                    preparedStatement.setString(7, GameWindow.getItem5());
	                    preparedStatement.setString(8, GameWindow.getItem6());
	                    preparedStatement.setInt(9, GameWindow.getItemCount());
	                    preparedStatement.setInt(10, GameWindow.getMoney());
	                    preparedStatement.setString(11, GameWindow.getUserName());

	                    preparedStatement.executeUpdate();

	                    preparedStatement.close();
	                    connection.close();
	                    System.out.println("Game state saved successfully.");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        });

	        GamePanel panel = new GamePanel();
	        panel.parentFrame = frame;

	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        
	        frame.getContentPane().add(panel);
	        frame.setUndecorated(false);
	        frame.setSize(1600, 900);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
	        
			NPC1_Dialogue begFrame = new NPC1_Dialogue();
			begFrame.setLocationRelativeTo(null);  // Center on screen
			begFrame.setMinimumSize(new Dimension(400, 200)); // Optional
			begFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			begFrame.pack();
			begFrame.setVisible(true);
	        

	    }
		else {
			loginFrame.dispose();
			System.out.println("Login Failed");
			Create_Account createFrame = new Create_Account(dummyParent);
	    	createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			createFrame.pack();
			createFrame.setSize(800, 600);
			createFrame.setVisible(true);
			
			boolean accountCreated = createFrame.accountCreated();
			if (accountCreated == true) {
				JFrame frame = new JFrame("Wanderer");

		        try {
		        	BufferedImage icon = ImageIO.read(GameWindow.class.getResourceAsStream("/resources/icons/Logo2.png"));
		            frame.setIconImage(icon);
		        } catch (IOException | NullPointerException e) {
		            System.err.println("Icon failed to load.");
		        }

		        GamePanel panel = new GamePanel();
		        panel.parentFrame = frame;

		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.getContentPane().add(panel);
		        frame.setUndecorated(false);
		        frame.setSize(1600, 900);
		        frame.pack();
		        frame.setLocationRelativeTo(null);
		        frame.setVisible(true);
		        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
			}
			else {
				System.out.println("Failed to created account");
				createFrame.dispose();
			}
		}
	}
}
		
