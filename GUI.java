//codice modificato con l'aiuto di chatgpt, da rifare lo stesso, ma almeno ora è bellino
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GUI {
    static final Color GREEN = new Color(44, 165, 141);
    static final Color BACKGROUND = new Color(192, 132, 100);

    JFrame frame;
    JPanel panel;
    ArrayList<JButton> board;

    public GUI() {
        frame = new JFrame();
        panel = new JPanel(new GridLayout(8, 8, 0, 0)); // Layout a griglia 8x8
        frame.setSize(730, 730);
        frame.setResizable(false);
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
            jb.setBorder(null);
            jb.setMargin(new Insets(0, 0, 0, 0));
            jb.setFocusable(false);
            jb.setBackground((i / 8 + i % 8) % 2 == 0 ? new Color(236, 212, 180) : bg); // Colori alternati
            board.add(jb);
            panel.add(jb);
        }
    }

    public void setPiece(int index, String piece) {
        try {
            // Carica l'immagine originale
            BufferedImage originalImage = ImageIO.read(getClass().getResource("/images/pieces/" + piece + ".png"));

            // Ottieni le dimensioni del pulsante
            int buttonWidth = board.get(index).getWidth();
            int buttonHeight = board.get(index).getHeight();

            // Se le dimensioni del pulsante non sono ancora determinate, usa dimensioni predefinite
            if (buttonWidth == 0 || buttonHeight == 0) {
                buttonWidth = 90; // Larghezza predefinita
                buttonHeight = 90; // Altezza predefinita
            }

            // Ridimensiona l'immagine per adattarla al pulsante
            Image scaledImg = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

            // Crea un BufferedImage con l'immagine ridimensionata
            BufferedImage resizedImage = new BufferedImage(buttonWidth, buttonHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(scaledImg, 0, 0, null);
            g2d.dispose();

            // Imposta l'icona sul pulsante
            board.get(index).setIcon(new ImageIcon(resizedImage));
            board.get(index).setText(""); // Rimuove il testo del pulsante
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
