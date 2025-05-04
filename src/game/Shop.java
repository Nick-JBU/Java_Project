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


public class Shop extends JFrame implements ActionListener{
	private JLabel priceLabel;
	private JFormattedTextField priceField;
	private JButton purchaseButton;
	private JButton updatePriceButton;
	private JTable itemCountTable;
	private JTable itemNamesTable;
	private JTable itemPriceTable;
	private final int numElements = 8;
	private String [] columnHeadings;
	private String [] nameHeading;
	private String [] countHeading;
	private String [] priceHeading;
	private String [][] tablePrice;
	private String [][] tableNames;
	private String [][] tableCount;
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
		
		updatePriceButton = new JButton("Update Price");
		updatePriceButton.addActionListener(this);
		
		columnHeadings = new String[1];
		nameHeading = new String[1];
		countHeading = new String[1];
		priceHeading = new String[1];
		
		tablePrice = new String[8][1];
		tableNames = new String[8][1];
		tableCount = new String[8][1];
		
		columnHeadings[0] = "Shop";
		nameHeading[0] = "Item";
		countHeading[0] = "Quantity to Buy";
		priceHeading[0] = "Price";
		
		
		tablePrice[0][0] = "100";
		tablePrice[1][0] = "30";
		tablePrice[2][0] = "25";
		tablePrice[3][0] = "10";
		tablePrice[4][0] = "60";
		tablePrice[5][0] = "40";
		tablePrice[6][0] = "200";
		tablePrice[7][0] = "5";
		
		for (i = 0; i<numElements; i++) {
			tableCount[i][0] = "0";
		}
		
		tableNames[0][0] = "map";
		tableNames[1][0] = "wood";
		tableNames[2][0] = "cat";
		tableNames[3][0] = "rock";
		tableNames[4][0] = "shield";
		tableNames[5][0] = "water";
		tableNames[6][0] = "paper";
		tableNames[7][0] = "coat";
		
		itemPriceTable = new JTable(tablePrice, priceHeading) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		itemNamesTable = new JTable(tableNames, nameHeading) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		itemCountTable = new JTable(tableCount, countHeading);
		
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
		layoutConst.insets = new Insets(0, 5, 10, 5);
		layoutConst.fill = GridBagConstraints.BOTH;
		layoutConst.gridx = 1;
		layoutConst.gridy = 1;
		layoutConst.weightx = 0.33;
		layoutConst.weighty = 1.0;
		add(new JScrollPane(itemPriceTable), layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(0, 5, 10, 10);
		layoutConst.fill = GridBagConstraints.BOTH;
		layoutConst.gridx = 2;
		layoutConst.gridy = 1;
		layoutConst.weightx = 0.33;
		layoutConst.weighty = 1.0;
		add(new JScrollPane(itemCountTable), layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 0;
		layoutConst.gridy = 2;
		add(purchaseButton, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 10);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 1;
		layoutConst.gridy = 2;
		add(updatePriceButton, layoutConst);
		
		layoutConst = new GridBagConstraints();
		layoutConst.insets = new Insets(10, 10, 10, 1);
		layoutConst.fill = GridBagConstraints.HORIZONTAL;
		layoutConst.gridx = 2;
		layoutConst.gridy = 1;
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
		int totalPrice = 0;
		
		String item1PriceString = tablePrice[0][0];
		int item1PriceInt = Integer.parseInt(item1PriceString);
		String item1CountString = tableCount[0][0];
		int item1CountInt = Integer.parseInt(item1CountString);
		
		String item2PriceString = tablePrice[1][0];
		int item2PriceInt = Integer.parseInt(item2PriceString);
		String item2CountString = tableCount[1][0];
		int item2CountInt = Integer.parseInt(item2CountString);

		String item3PriceString = tablePrice[2][0];
		int item3PriceInt = Integer.parseInt(item3PriceString);
		String item3CountString = tableCount[2][0];
		int item3CountInt = Integer.parseInt(item3CountString);
		
		String item4PriceString = tablePrice[3][0];
		int item4PriceInt = Integer.parseInt(item4PriceString);
		String item4CountString = tableCount[3][0];
		int item4CountInt = Integer.parseInt(item4CountString);
		
		String item5PriceString = tablePrice[4][0];
		int item5PriceInt = Integer.parseInt(item5PriceString);
		String item5CountString = tableCount[4][0];
		int item5CountInt = Integer.parseInt(item5CountString);
		
		String item6PriceString = tablePrice[5][0];
		int item6PriceInt = Integer.parseInt(item6PriceString);
		String item6CountString = tableCount[5][0];
		int item6CountInt = Integer.parseInt(item6CountString);
		
		String item7PriceString = tablePrice[6][0];
		int item7PriceInt = Integer.parseInt(item7PriceString);
		String item7CountString = tableCount[6][0];
		int item7CountInt = Integer.parseInt(item7CountString);
		
		String item8PriceString = tablePrice[7][0];
		int item8PriceInt = Integer.parseInt(item8PriceString);
		String item8CountString = tableCount[7][0];
		int item8CountInt = Integer.parseInt(item8CountString);

		if (item1CountInt > 0) {
			totalPrice += item1CountInt * item1PriceInt;
		}
		if (item2CountInt > 0) {
			totalPrice += item2CountInt * item2PriceInt;
		}
		if (item3CountInt > 0) {
			totalPrice += item3CountInt * item3PriceInt;
		}
		if (item4CountInt > 0) {
			totalPrice += item4CountInt * item4PriceInt;
		}
		if (item5CountInt > 0) {
			totalPrice += item5CountInt * item5PriceInt;
		}
		if (item6CountInt > 0) {
			totalPrice += item6CountInt * item6PriceInt;
		}
		if (item7CountInt > 0) {
			totalPrice += item7CountInt * item7PriceInt;
		}
		if (item8CountInt > 0) {
			totalPrice += item8CountInt * item8PriceInt;
		}
		
		if (event.getSource() == updatePriceButton) {
			priceField.setValue(totalPrice);
		}		
        else if (event.getSource() == purchaseButton && GameWindow.money > totalPrice && GameWindow.item_count <= 6) {
    		if (item1CountInt > 1 || item2CountInt > 1 || item3CountInt > 1 || item4CountInt > 1 || item5CountInt > 1 || item6CountInt > 1 || item7CountInt > 1 || item8CountInt > 1) {
    			priceField.setText("Can only purchase 1 item at a time");
    		}
    		else {
                priceField.setText("Items have been purchased.");
                if (GameWindow.item_count == 0){
                	if (totalPrice == 100) {
                		GameWindow.item_1 = tableNames[0][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 30) {
                		GameWindow.item_1 = tableNames[1][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 25) {
                		GameWindow.item_1 = tableNames[2][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 10) {
                		GameWindow.item_1 = tableNames[3][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 60) {
                		GameWindow.item_1 = tableNames[4][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 40) {
                		GameWindow.item_1 = tableNames[5][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 200) {
                		GameWindow.item_1 = tableNames[6][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 5) {
                		GameWindow.item_1 = tableNames[7][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                }
                if (GameWindow.item_count == 1){
                	if (totalPrice == 100) {
                		GameWindow.item_2 = tableNames[0][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 30) {
                		GameWindow.item_2 = tableNames[1][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 25) {
                		GameWindow.item_2 = tableNames[2][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 10) {
                		GameWindow.item_2 = tableNames[3][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 60) {
                		GameWindow.item_2 = tableNames[4][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 40) {
                		GameWindow.item_2 = tableNames[5][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 200) {
                		GameWindow.item_2 = tableNames[6][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 5) {
                		GameWindow.item_2 = tableNames[7][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                }
                if (GameWindow.item_count == 2){
                	if (totalPrice == 100) {
                		GameWindow.item_3 = tableNames[0][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 30) {
                		GameWindow.item_3 = tableNames[1][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 25) {
                		GameWindow.item_3 = tableNames[2][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 10) {
                		GameWindow.item_3 = tableNames[3][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 60) {
                		GameWindow.item_3 = tableNames[4][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 40) {
                		GameWindow.item_3 = tableNames[5][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 200) {
                		GameWindow.item_3 = tableNames[6][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 5) {
                		GameWindow.item_3 = tableNames[7][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                }
                if (GameWindow.item_count == 3){
                	if (totalPrice == 100) {
                		GameWindow.item_4 = tableNames[0][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 30) {
                		GameWindow.item_4 = tableNames[1][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 25) {
                		GameWindow.item_4 = tableNames[2][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 10) {
                		GameWindow.item_4 = tableNames[3][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 60) {
                		GameWindow.item_4 = tableNames[4][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 40) {
                		GameWindow.item_4 = tableNames[5][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 200) {
                		GameWindow.item_4 = tableNames[6][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 5) {
                		GameWindow.item_4 = tableNames[7][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                }
                if (GameWindow.item_count == 4){
                	if (totalPrice == 100) {
                		GameWindow.item_5 = tableNames[0][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 30) {
                		GameWindow.item_5 = tableNames[1][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 25) {
                		GameWindow.item_5 = tableNames[2][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 10) {
                		GameWindow.item_5 = tableNames[3][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 60) {
                		GameWindow.item_5 = tableNames[4][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 40) {
                		GameWindow.item_5 = tableNames[5][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 200) {
                		GameWindow.item_5 = tableNames[6][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 5) {
                		GameWindow.item_5 = tableNames[7][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                }
                if (GameWindow.item_count == 5){
                	if (totalPrice == 100) {
                		GameWindow.item_6 = tableNames[0][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 30) {
                		GameWindow.item_6 = tableNames[1][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 25) {
                		GameWindow.item_6 = tableNames[2][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 10) {
                		GameWindow.item_6 = tableNames[3][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 60) {
                		GameWindow.item_6 = tableNames[4][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 40) {
                		GameWindow.item_6 = tableNames[5][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 200) {
                		GameWindow.item_6 = tableNames[6][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}
                	else if (totalPrice == 5) {
                		GameWindow.item_2 = tableNames[7][0];
                		GameWindow.item_count += 1;
                		GameWindow.money -= totalPrice;
                		return;
                	}

                }
    		}
        }
	}
	public static void main(String [] args) {
		Shop myFrame = new Shop();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
	}
}
