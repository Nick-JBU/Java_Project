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


public class Mayor_Dialogue extends JFrame implements ActionListener{
	public JLabel interactLabel;
	//public JTextField interactField;
	public JTextArea interactArea;
	private JButton continueButton;
	private JButton backButton;
	int page_count = 0;
	private boolean hasPassed = false;
	
	Mayor_Dialogue(){
		GridBagConstraints layoutConst = null;
		setTitle("Converstation");
		
		continueButton = new JButton("Continue");
		continueButton.addActionListener(this);
		
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		
		interactLabel = new JLabel("Mayor: ");
		
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
			if (hasPassed == false) {
				interactArea.setText("Hello there " + GameWindow.char_name + ". My assistant Peter told me of your arrival.");
			}
			else {
				//page_count += 1;
			}
		}
		else if (page_count == 1) {
			if (hasPassed == false) {
				interactArea.setText("My name is Bryan, the mayor of Elsewhere. I'd like to give you a nice welcoming gift.");
			}
			else {
				//page_count += 1;
			}
		}
		else if (page_count == 2) {
			if (hasPassed == false) {
				interactArea.setText("The mayor hands you a small pouch. It looks like it can store about 6 items.");
			}
			else {
				//page_count += 1;
			}
		}
		else if (page_count == 3) {
			if (hasPassed == false) {
				interactArea.setText("This will serve as a storage device while you are here. Press the 'X' key to open it.");
			}
			else {
				//page_count += 1;
			}
		}
		else if (page_count == 4) {
			if (hasPassed == false) {
				interactArea.setText("Oh, and here's 50 coins to help get you started with things. Go out and say hi to our town. I'm sure everyone would love to meet you.");
				//GameWindow.money += 50;
			}
			else {
				//page_count += 1;
			}
		}
		else if (page_count == 5) {
			if (hasPassed == false) {
				interactArea.setText("Oh, and by the way, I absolutely love cats. If you were to perchance give me one, I would reward you handsomely");
			}
			else {
				//page_count += 1;
			}
		}
		else if (page_count == 6) {
			if ("cat".equals(GameWindow.item_1) || "cat".equals(GameWindow.item_2) ||"cat".equals(GameWindow.item_3) ||"cat".equals(GameWindow.item_4) ||
					"cat".equals(GameWindow.item_5) ||"cat".equals(GameWindow.item_6)){
						interactArea.setText("Oh my! You actually found one! Here, you are the new mayor now. Please take this 1 million dollars as a reward too!");
						GameWindow.money += 1000000;
					}
			else {
				interactArea.setText("Would you please find me a cat?");
				
			}
		}
		else if (page_count == 7) {
			interactArea.setText("End of dialogue. Exit the window to continue the game.");
			hasPassed = true;
		}
		if (event.getSource() == continueButton)
			//interactField.setText("Continue button");
			page_count += 1;
        else if (event.getSource() == backButton)
            //interactField.setText("Back button");
        	page_count -= 1;
		if (page_count > 7) {
			page_count = 7;
		}
		if (page_count < 0) {
			page_count = 0;
		}
	}

	public static void main(String[] args) {
		Mayor_Dialogue myFrame = new Mayor_Dialogue();
		myFrame.setLocationRelativeTo(null);  // Center on screen
		myFrame.setMinimumSize(new Dimension(400, 200)); // Optional
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);

	}

}
