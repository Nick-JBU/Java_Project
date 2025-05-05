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


public class NPC1_Dialogue extends JFrame implements ActionListener{
	public JLabel interactLabel;
	//public JTextField interactField;
	public JTextArea interactArea;
	private JButton continueButton;
	private JButton backButton;
	int page_count = 0;
	
	NPC1_Dialogue(){
		GridBagConstraints layoutConst = null;
		setTitle("Converstation");
		
		continueButton = new JButton("Continue");
		continueButton.addActionListener(this);
		
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		
		interactLabel = new JLabel("???: ");
		
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
			interactArea.setText("Are you okay, stranger? It looked like you hit your head pretty hard back there.");
		}
		else if (page_count == 1) {
			interactArea.setText("You came running in crazy fast back there. It looked like you were running from something.");
		}
		else if (page_count == 2) {
			interactArea.setText("Anyway, you got a name, stranger?... " + GameWindow.char_name + "? Cool name. We met somebody earlier with a name like that. He had a few screws loose though if you know what I mean.");
		}
		else if (page_count == 3) {
			interactArea.setText("Well, anyway, welcome to Elsewhere. Please feel free to look around and rest up while you're here.");
		}
		else if (page_count == 4) {
			interactArea.setText("Oh, and if your wondering how to move around, use the arrow keys. To interact with something, you press the 'z' key. To sprint, you press the 'shift' key.");
		}
		else if (page_count == 5) {
			interactArea.setText("End of dialogue. Exit the window to continue the game.");
		}
		if (event.getSource() == continueButton)
			//interactField.setText("Continue button");
			page_count += 1;
        else if (event.getSource() == backButton)
            //interactField.setText("Back button");
        	page_count -= 1;
		if (page_count > 5) {
			page_count = 5;
		}
		if (page_count < 0) {
			page_count = 0;
		}
	}

	public static void main(String[] args) {
		Display_Text myFrame = new Display_Text();
		myFrame.setLocationRelativeTo(null);  // Center on screen
		myFrame.setMinimumSize(new Dimension(400, 200)); // Optional
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);

	}

}
