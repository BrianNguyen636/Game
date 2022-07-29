import javax.swing.*;
import java.awt.*;

/**
 * Code fragments taken from Learn Code By Gaming youtube:
 * https://github.com/learncodebygaming/java_2d_game
 */
public class Main {

    private static void initWindow() {
        JFrame window = new JFrame("Placeholder title");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.addMouseListener(board);
        window.addMouseMotionListener(board);

        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);


    }

    public static void main(String[] args) {
	// write your code here
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow();
            }
        });
    }
}
