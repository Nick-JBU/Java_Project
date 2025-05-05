package game;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class Jenna_Complete extends JFrame implements ActionListener{
	public JLabel interactLabel;
	//public JTextField interactField;
	public JTextArea interactArea;
	private JButton continueButton;
	private JButton backButton;
	int page_count = 0;
	
	Jenna_Complete(){
		GridBagConstraints layoutConst = null;
		setTitle("Converstation");
		
		continueButton = new JButton("Continue");
		continueButton.addActionListener(this);
		
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		
		interactLabel = new JLabel("Jenna: ");
		
		interactArea = new JTextArea(3, 30);
		interactArea.setLineWrap(true);
		interactArea.setWrapStyleWord(true);
		interactArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(interactArea);
		
		setLayout(new GridBagLayout());
		layoutConst = new GridBagConstraints();
		
		layoutConst.gridx = 0;
		layoutConst.gridy = 0;	
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(interactLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 0;
		layoutConst.gridwidth = 2;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.weightx = 1.0;
		add(scrollPane, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(backButton, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.gridx = 1;
		layoutConst.gridy = 1;
		layoutConst.insets = new Insets(10, 10, 10, 10);
		add(continueButton, layoutConst);
		
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		if (page_count == 0) {
			interactArea.setText("Thanks for getting me those cups. What's that? You need some bandages? Well sure thing...");
		}
		else if (page_count == 1 && GameWindow.item_count < 6) {
			interactArea.setText("You now have bandages in your inventory.");
			if (GameWindow.item_count == 0) {
				GameWindow.item_1 = ("Bandages");
				GameWindow.item_count += 1;
			}
			else if (GameWindow.item_count == 1) {
				GameWindow.item_2 = ("Bandages");
				GameWindow.item_count += 1;
			}
			else if (GameWindow.item_count == 2) {
				GameWindow.item_3 = ("Bandages");
				GameWindow.item_count += 1;
			}
			else if (GameWindow.item_count == 3) {
				GameWindow.item_4 = ("Bandages");
				GameWindow.item_count += 1;
			}
			else if (GameWindow.item_count == 4) {
				GameWindow.item_5 = ("Bandages");
			}
			else if (GameWindow.item_count == 5) {
				GameWindow.item_6 = ("Bandages");
				GameWindow.item_count += 1;
			}
		}
		else if (page_count == 1 && GameWindow.item_count >= 6) {
			interactArea.setText("Oops, looks like you don't have room in your inventory");
		}
		else if (page_count == 2) {
			interactArea.setText("End of dialogue. Exit the window to continue the game.");
		}
		if (event.getSource() == continueButton) {
			//interactField.setText("Continue button");
			page_count += 1;
		}
        else if (event.getSource() == backButton) {
            //interactField.setText("Back button");
        	page_count -= 1;
        }
		if (page_count > 2) {
			page_count = 2;
		}
		if (page_count < 0) {
			page_count = 0;
		}
	}
}
