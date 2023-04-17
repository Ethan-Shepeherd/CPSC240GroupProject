import javax.swing.*;

public class EscapeRoom {
    public static void main(String[] args) {
        // Start the GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EscapeRoomGUI();
            }
        });
    }
}