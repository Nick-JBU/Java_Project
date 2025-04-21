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

public class Display_Text extends JFrame implements ActionListener{
	public JLabel userLabel;
	public JLabel interactLabel;
	public JTextField userField;
	public JTextField interactField;
	
	Display_Text(){
		GridBagConstraints layoutConst = null;
		setTitle("Conversation");
		
		userLabel = new JLabel("You: ");
		interactLabel = new JLabel("NPC: ");
		
		userField = new JTextField(15);
		userField.setEditable(true);
		userField.setText("");
		userField.addActionListener(this);
		
		interactField = new JTextField(15);
		interactField.setEditable(false);
		
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
		add(interactLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 1;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(interactField, layoutConst);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String userInput;
		userInput = userField.getText();
		
		interactField.setText("Success");
	}

	public static void main(String[] args) {
		Display_Text myFrame = new Display_Text();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);

	}

}
