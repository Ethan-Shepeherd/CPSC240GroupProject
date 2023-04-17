
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EscapeRoomGUI extends JFrame {
    private JPanel gamePanel;
    private JLabel playerLabel;
    private JLabel doorLabel;
    private JLabel obstacleLabel;

    private int playerX;
    private int playerY;

    public EscapeRoomGUI() {
        // Set up the game panel
        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setPreferredSize(new Dimension(400, 400));
        add(gamePanel);

        // Set up the player label
        playerLabel = new JLabel(new ImageIcon("player.png"));
        playerX = 180;
        playerY = 360;
        playerLabel.setBounds(playerX, playerY, 40, 40);
        gamePanel.add(playerLabel);

        // Add a border of wall images around the game panel
        ImageIcon wallIcon = new ImageIcon("Wall.jpg");
        JLabel topWall = new JLabel(wallIcon);
        topWall.setBounds(0, 0, gamePanel.getWidth(), wallIcon.getIconHeight());
        topWall.setOpaque(true);
        gamePanel.add(topWall);
        JLabel leftWall = new JLabel(wallIcon);
        leftWall.setBounds(0, 0, wallIcon.getIconWidth(), gamePanel.getHeight());
        gamePanel.add(leftWall);
        JLabel rightWall = new JLabel(wallIcon);
        rightWall.setBounds(gamePanel.getWidth() - wallIcon.getIconWidth(), 0, wallIcon.getIconWidth(), gamePanel.getHeight());
        gamePanel.add(rightWall);
        JLabel bottomWall = new JLabel(wallIcon);
        bottomWall.setBounds(0, gamePanel.getHeight() - wallIcon.getIconHeight(), gamePanel.getWidth(), wallIcon.getIconHeight());
        gamePanel.add(bottomWall);

        add(gamePanel);

        // Set up the door label
        doorLabel = new JLabel(new ImageIcon("door.png"));
        doorLabel.setBounds(180, 20, 40, 40);
        gamePanel.add(doorLabel);

        // Set up the obstacle label
        obstacleLabel = new JLabel(new ImageIcon("obstacle.png"));
        obstacleLabel.setBounds(20, 100, 40, 40);
        gamePanel.add(obstacleLabel);

        // Set up the key listener to move the player
        gamePanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    movePlayer(-10, 0);
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    movePlayer(10, 0);
                } else if (keyCode == KeyEvent.VK_UP) {
                    movePlayer(0, -10);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    movePlayer(0, 10);
                }
            }
        });
        gamePanel.setFocusable(true);

        // Set up the window
        setTitle("Escape Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void movePlayer(int deltaX, int deltaY) {
        int newPlayerX = playerX + deltaX;
        int newPlayerY = playerY + deltaY;
        if (newPlayerX >= 0 && newPlayerX <= 360 && newPlayerY >= 0 && newPlayerY <= 360) {
            playerX = newPlayerX;
            playerY = newPlayerY;
            playerLabel.setBounds(playerX, playerY, 40, 40);
            checkCollisions();
        }
    }

    private void checkCollisions() {
        if (playerLabel.getBounds().intersects(doorLabel.getBounds())) {
            JOptionPane.showMessageDialog(this, "You escaped!");
            System.exit(0);
        } else if (playerLabel.getBounds().intersects(obstacleLabel.getBounds())) {
            JOptionPane.showMessageDialog(this, "You hit an obstacle!");
            playerX = 180;
            playerY = 360;
            playerLabel.setBounds(playerX, playerY, 40, 40);
        }
    }
}