package final_project_package;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class Login extends JFrame implements ActionListener {
	public JLabel userLabel;
	public JLabel passwordLabel;
	public JTextField userField;
	public JTextField passwordField;
	
	Login(){
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
		        } else {
		            //Element does not exist. Create a new row in database with default values and set = to user data
		        	System.out.println("Login Failed");
		        }
		    }
		    resultSet.close();
		    preparedStatement.close();
		    connection.close();

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Login myFrame = new Login();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);

	}
}
