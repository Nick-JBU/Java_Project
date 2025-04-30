package game;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JDialog implements ActionListener {
	public JLabel userLabel;
	public JLabel passwordLabel;
	public JTextField userField;
	public JTextField passwordField;
	public boolean loginPass = false;
	public String userInput = "";
	
	public Login(JFrame parent) {
		super(parent, "Login", true);
		initUI();
	}
	private void initUI() {
		GridBagConstraints layoutConst = null;
		setTitle("Login");
		
		userLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		
		userField = new JTextField(15);
		userField.setEditable(true);
		userField.setText("");
		userField.addActionListener(this);
		
		passwordField = new JTextField(15);
		passwordField.setEditable(true);
		passwordField.setText("");
		passwordField.addActionListener(this);
		
		setLayout(new GridBagLayout());
		layoutConst = new GridBagConstraints();
		
		layoutConst.gridx = 0;
		layoutConst.gridy = 0;	
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(userLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 0;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(userField, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(passwordLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 1;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(passwordField, layoutConst);
		userField.setColumns(30);
		passwordField.setColumns(30);
		
		Font font = new Font("SansSerif", Font.PLAIN, 18); // You can change size (18) and font family

		userLabel.setFont(font);
		passwordLabel.setFont(font);
		userField.setFont(font);
		passwordField.setFont(font);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		String userInput;
		String userPassword;
		userInput = userField.getText();
		userPassword = passwordField.getText();
		
		String url = "jdbc:mysql://localhost:3306/project_db";
		String username = "root";
		String password = "";
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			String name_check = "SELECT COUNT(*) FROM users WHERE name = ? AND password = ?";
		    PreparedStatement preparedStatement = connection.prepareStatement(name_check);
		    preparedStatement.setString(1, userInput);
		    preparedStatement.setString(2, userPassword);
		    ResultSet resultSet = preparedStatement.executeQuery();
		    if(resultSet.next()){
		        int count = resultSet.getInt(1);
		        if(count > 0){
		            //Set variables/data to corresponding database data
		        	System.out.println("Login Successful");
		        	loginPass = true;
		        	dispose();
		        	
		        } else {
		            //Element does not exist. Create a new row in database with default values and set = to user data
		        	System.out.println("Login Failed");
		        	loginPass = false;
		        	dispose();
		        	
		        }
		    }
		    resultSet.close();
		    preparedStatement.close();
		    connection.close();
		    
		    //dispose();

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isLoggedIn(){
		return loginPass;
	}
	public String loggedUsername(){
		return userInput;
	}
	public static void main(String[] args) {
		JFrame dummyParent = new JFrame();
		Login myFrame = new Login(dummyParent);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);

	}
}
