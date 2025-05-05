package game;

import java.awt.Dimension;
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
	private JButton removeItem1Button;
	private JButton removeItem2Button;
	private JButton removeItem3Button;
	private JButton removeItem4Button;
	private JButton removeItem5Button;
	private JButton removeItem6Button;
	Display_Items(){
		GridBagConstraints layoutConst = null;
		int i;
		setTitle("Your Items");
		
		removeItem1Button = new JButton("Remove Item 1");
		removeItem1Button.addActionListener(this);
		removeItem2Button = new JButton("Remove Item 2");
		removeItem2Button.addActionListener(this);
		removeItem3Button = new JButton("Remove Item 3");
		removeItem3Button.addActionListener(this);
		removeItem4Button = new JButton("Remove Item 4");
		removeItem4Button.addActionListener(this);
		removeItem5Button = new JButton("Remove Item 5");
		removeItem5Button.addActionListener(this);
		removeItem6Button = new JButton("Remove Item 6");
		removeItem6Button.addActionListener(this);
		
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
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 2;
		add(removeItem1Button, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 3;
		add(removeItem2Button, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 4;
		add(removeItem3Button, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 5;
		add(removeItem4Button, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 6;
		add(removeItem5Button, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 7;
		add(removeItem6Button, layoutConst);
		
		removeItem1Button.setPreferredSize(new Dimension(100, 25));
		removeItem2Button.setPreferredSize(new Dimension(100, 25));
		removeItem3Button.setPreferredSize(new Dimension(100, 25));
		removeItem4Button.setPreferredSize(new Dimension(100, 25));
		removeItem5Button.setPreferredSize(new Dimension(100, 25));
		removeItem6Button.setPreferredSize(new Dimension(100, 25));
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == removeItem1Button) {
			GameWindow.item_count -= 1;
			GameWindow.item_1 = GameWindow.item_2;
			GameWindow.item_2 = GameWindow.item_3;
			GameWindow.item_3 = GameWindow.item_4;
			GameWindow.item_4 = GameWindow.item_5;
			GameWindow.item_5 = GameWindow.item_6;
			tableNames[0][0] = "item removed";
		}
		else if (event.getSource() == removeItem2Button) {
			GameWindow.item_count -= 1;
			GameWindow.item_2 = GameWindow.item_3;
			GameWindow.item_3 = GameWindow.item_4;
			GameWindow.item_4 = GameWindow.item_5;
			GameWindow.item_5 = GameWindow.item_6;
			tableNames[1][0] = "item removed";
		}
		else if (event.getSource() == removeItem3Button) {
			GameWindow.item_count -= 1;
			GameWindow.item_3 = GameWindow.item_4;
			GameWindow.item_4 = GameWindow.item_5;
			GameWindow.item_5 = GameWindow.item_6;
			tableNames[2][0] = "item removed";
		}
		else if (event.getSource() == removeItem4Button) {
			GameWindow.item_count -= 1;
			GameWindow.item_4 = GameWindow.item_5;
			GameWindow.item_5 = GameWindow.item_6;
			tableNames[3][0] = "item removed";
		}
		else if (event.getSource() == removeItem5Button) {
			GameWindow.item_5 = GameWindow.item_6;
			tableNames[4][0] = "item removed";
		}
		else if (event.getSource() == removeItem6Button) {
			GameWindow.item_count -= 1;
			GameWindow.item_6 = null;
			tableNames[5][0] = "item removed";
		}
	}
	public static void main(String [] args) {
		Display_Items myFrame = new Display_Items();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
	}
}
