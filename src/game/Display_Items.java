package game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class Display_Items extends JFrame implements ActionListener{
	private JTable itemNamesTable;
	private final int numElements = 6;
	private String [] nameHeading;
	private String [][] tableNames;
	Display_Items(){
		GridBagConstraints layoutConst = null;
		int i;
		setTitle("Your Items");
		
		
		nameHeading = new String[1];
		tableNames = new String[6][1];
		
		nameHeading[0] = "Items";
		
		tableNames[0][0] = GameWindow.getItem1();
		tableNames[1][0] = GameWindow.getItem2();
		tableNames[2][0] = GameWindow.getItem3();
		tableNames[3][0] = GameWindow.getItem4();
		tableNames[4][0] = GameWindow.getItem5();
		tableNames[5][0] = GameWindow.getItem6();
		
		itemNamesTable = new JTable(tableNames, nameHeading) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		
		setLayout(new GridBagLayout());
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 10, 10, 5);
		layoutConst.fill = GridBagConstraints.BOTH;
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		layoutConst.weightx = 0.33;
		layoutConst.weighty = 1.0;
		add(new JScrollPane(itemNamesTable), layoutConst);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
	}
	public static void main(String [] args) {
		Display_Items myFrame = new Display_Items();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
	}
}
