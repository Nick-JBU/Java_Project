package game;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GameWindow {
    public static void main(String[] args) {
    	JFrame dummyParent = new JFrame();
    	Login loginFrame = new Login(dummyParent);
    	loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loginFrame.pack();
		loginFrame.setSize(800, 600);
		loginFrame.setVisible(true);
		
		boolean loggedIn = loginFrame.isLoggedIn();
		
		if (loggedIn == true) {
	        JFrame frame = new JFrame("Wanderer");

	        try {
	        	BufferedImage icon = ImageIO.read(GameWindow.class.getResourceAsStream("/resources/icons/Logo2.png"));
	            frame.setIconImage(icon);
	        } catch (IOException | NullPointerException e) {
	            System.err.println("Icon failed to load.");
	        }

	        GamePanel panel = new GamePanel();
	        panel.parentFrame = frame;

	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().add(panel);
	        frame.setUndecorated(false);
	        frame.setSize(1600, 900);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());

	    }
		else {
			loginFrame.dispose();
			System.out.println("Login Failed");
			Create_Account createFrame = new Create_Account(dummyParent);
	    	createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			createFrame.pack();
			createFrame.setSize(800, 600);
			createFrame.setVisible(true);
			
			boolean accountCreated = createFrame.accountCreated();
			if (accountCreated == true) {
				JFrame frame = new JFrame("Wanderer");

		        try {
		        	BufferedImage icon = ImageIO.read(GameWindow.class.getResourceAsStream("/resources/icons/Logo2.png"));
		            frame.setIconImage(icon);
		        } catch (IOException | NullPointerException e) {
		            System.err.println("Icon failed to load.");
		        }

		        GamePanel panel = new GamePanel();
		        panel.parentFrame = frame;

		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.getContentPane().add(panel);
		        frame.setUndecorated(false);
		        frame.setSize(1600, 900);
		        frame.pack();
		        frame.setLocationRelativeTo(null);
		        frame.setVisible(true);
		        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
			}
			else {
				System.out.println("Failed to created account");
				createFrame.dispose();
			}
		}
	}
}
		
