package game;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GameWindow {
    public static void main(String[] args) {
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
}