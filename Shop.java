package final_project_package;

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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTable;


public class Shop extends JFrame implements ActionListener{
	private JLabel priceLabel;
	private JFormattedTextField priceField;
	private JButton purchaseButton;
	private JTable itemValsTable;
	private JTable itemNamesTable;
	private final int numElements = 8;
	private String [] columnHeadings;
	private String [][] tableVals;
	private String [][] tableNames;
	Shop(){
		GridBagConstraints layoutConst = null;
		int i;
		setTitle("Shop");
		priceLabel = new JLabel("Price: ");
		
		priceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		priceField.setColumns(15);
		priceField.setEditable(false);
		priceField.setValue(0);
		
		purchaseButton = new JButton("Check Out");
		purchaseButton.addActionListener(this);
		
		columnHeadings = new String[1];
		tableVals = new String[8][1];
		tableNames = new String[8][1];
		
		columnHeadings[0] = "Shop";
		
		for (i = 0; i<numElements; i++) {
			tableVals[i][0] = "0";
		}
		
		tableNames[0][0] = "map";
		tableNames[1][0] = "wood";
		tableNames[2][0] = "cat";
		tableNames[3][0] = "rock";
		tableNames[4][0] = "shield";
		tableNames[5][0] = "water";
		tableNames[6][0] = "paper";
		tableNames[7][0] = "coat";
		
		itemValsTable = new JTable(tableVals, columnHeadings);
		itemNamesTable = new JTable(tableNames, columnHeadings);
		
		setLayout(new GridBagLayout());
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 0, 0);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 0;
		layoutConst.gridwidth = 2;
		add(itemValsTable.getTableHeader(), layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 10, 10, 0);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 1;
		layoutConst.gridy = 1;
		layoutConst.gridwidth = 2;
		add(itemValsTable, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 10, 10, 0);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		layoutConst.gridwidth = 2;
		add(itemNamesTable, layoutConst);
	
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 2;
		add(purchaseButton, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 1);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 1;
		layoutConst.gridy = 2;
		add(priceLabel, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 1, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 2;
		layoutConst.gridy = 2;
		add(priceField, layoutConst);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		int i;
		int maxElement;
		String strElem;
		int elemVal;
		strElem = tableVals[0][0];
		maxElement = Integer.parseInt(strElem);
		
		for (i = 1; i < numElements; ++i) {
			strElem = tableVals[i][0];
			elemVal = Integer.parseInt(strElem);
			if (elemVal > maxElement) {
				maxElement = elemVal;
			}
		}
		priceField.setValue(maxElement);
	}
	public static void main(String [] args) {
		Shop myFrame = new Shop();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
	}
}
