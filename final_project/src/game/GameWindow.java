package game;
import javax.swing.*;

public class GameWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Java Game");
        GamePanel panel = new GamePanel();
        
        frame.setSize(1600, 900); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
        
        panel.requestFocusInWindow();
    }
}
