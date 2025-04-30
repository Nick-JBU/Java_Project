package game;

import javax.swing.*;
import java.awt.*;

public class DebugPanel extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea statusText;
    private JTextArea logText;


    public DebugPanel() {
        setTitle("Debug Panel");
        setSize(430, 150);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());


        statusText = new JTextArea(4, 20);
        statusText.setEditable(false);
        statusText.setFont(new Font("Monospaced", Font.PLAIN, 12));


        logText = new JTextArea();
        logText.setEditable(false);
        logText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logText);

        add(statusText, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateDebugInfo(String loopStatus, String action, int row, int col, String map) {
        statusText.setText(
            "Game loop: " + loopStatus + "\n" +
            "Player action: " + action + "\n" +
            "Player location: " + row + ", " + col + "\n" +
            "Current map: " + map
        );
    }

    public void logDoorEntry(int row, int col) {
        logText.append("Door entered at: " + row + ", " + col + "\n");
        logText.setCaretPosition(logText.getDocument().getLength()); 
    }
}