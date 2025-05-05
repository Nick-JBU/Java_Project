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

public class Create_Account extends JDialog implements ActionListener {
	public JLabel userLabel;
	public JLabel passwordLabel;
	public JLabel genderLabel;
	public JLabel char_nameLabel;
	public JTextField userField;
	public JTextField passwordField;
	public JTextField genderField;
	public JTextField char_nameField;
	public boolean accountPass = false;
	public String userInput = "";
	
	public Create_Account(JFrame parent) {
		super(parent, "Create_Account", true);
		initUI();
	}
	private void initUI() {
		GridBagConstraints layoutConst = null;
		setTitle("Create Account");
		
		userLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		genderLabel = new JLabel("Gender (male or female): ");
		char_nameLabel = new JLabel("Character Name: ");
		
		
		
		userField = new JTextField(15);
		userField.setEditable(true);
		userField.setText("");
		userField.addActionListener(this);
		
		passwordField = new JTextField(15);
		passwordField.setEditable(true);
		passwordField.setText("");
		passwordField.addActionListener(this);
		
		genderField = new JTextField(15);
		genderField.setEditable(true);
		genderField.setText("");
		genderField.addActionListener(this);
		
		char_nameField = new JTextField(15);
		char_nameField.setEditable(true);
		char_nameField.setText("");
		char_nameField.addActionListener(this);
		
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
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 0;
		layoutConst.gridy = 2;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(genderLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 2;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(genderField, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 0;
		layoutConst.gridy = 3;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(char_nameLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 3;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(char_nameField, layoutConst);
		
		userField.setColumns(30);
		passwordField.setColumns(30);
		genderField.setColumns(30);
		char_nameField.setColumns(30);
		
		Font font = new Font("SansSerif", Font.PLAIN, 18); // You can change size (18) and font family

		userLabel.setFont(font);
		passwordLabel.setFont(font);
		genderLabel.setFont(font);
		userField.setFont(font);
		passwordField.setFont(font);
		genderField.setFont(font);
		char_nameLabel.setFont(font);
		char_nameField.setFont(font);
		
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		String userInput;
		String userPassword;
		String charName;
		String gender;
		userInput = userField.getText();
		userPassword = passwordField.getText();
		gender = genderField.getText();
		charName = char_nameField.getText();
		
		String url = "jdbc:mysql://localhost:3306/project_db";
		String username = "root";
		String password = "";
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			String add_data = "INSERT INTO users (name, password, character_name, gender, item_1, item_2, item_3, item_4, item_5, item_6, item_count, money) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		    PreparedStatement preparedStatement = connection.prepareStatement(add_data);
		    preparedStatement.setString(1, userInput);
		    preparedStatement.setString(2, userPassword);
		    preparedStatement.setString(3, charName);
		    preparedStatement.setString(4, gender);
		    preparedStatement.setString(5, null);
		    preparedStatement.setString(6, null);
		    preparedStatement.setString(7, null);
		    preparedStatement.setString(8, null);
		    preparedStatement.setString(9, null);
		    preparedStatement.setString(10, null);
		    preparedStatement.setInt(11, 0);
		    preparedStatement.setInt(12, 50);
		    int rowsInserted = preparedStatement.executeUpdate();
		    accountPass = true;
		    dispose();
		    preparedStatement.close();
		    connection.close();
		    
		    //dispose();

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean accountCreated(){
		return accountPass;
	}
	public String loggedUsername(){
		return userInput;
	}
	public static void main(String[] args) {
		JFrame dummyParent = new JFrame();
		Create_Account myFrame = new Create_Account(dummyParent);
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);

	}
}
