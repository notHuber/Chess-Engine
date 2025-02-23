import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GUI {
    static final Color green = new Color(44, 165, 141);
    static final Color BACKGROUND = green;

    JFrame frame;
    JPanel panel;
    ArrayList<JButton> board;

    public GUI() {
        frame = new JFrame();
        panel = new JPanel(new GridLayout(8, 8, 0, 0)); // 8x8 grid layout
        frame.setSize(730, 730);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess Engine");
        frame.setVisible(true);

        setBoard(BACKGROUND);
    }

    public void setBoard(Color bg) {
        board = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            JButton jb = new JButton();
            jb.setFocusable(false);
            jb.setBackground((i / 8 + i % 8) % 2 == 0 ? Color.white : bg); // alternate colors
            board.add(jb);
            panel.add(jb);
        }
    }

    public void setPiece(int index, String piece) {
        board.get(index).setText(piece);
        try {
            Image img = ImageIO.read(getClass().getResource("/images/" + piece + ".png"));
            board.get(index).setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
