import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;


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
        gamePanel.setBackground(Color.WHITE); // add a background color to make the wall components more visible
        add(gamePanel);

        // Set up the player label

        ImageIcon playerIcon = new ImageIcon("player.gif");
        Image playerImage = playerIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        playerIcon = new ImageIcon(playerImage);
        playerLabel = new JLabel(playerIcon);
        playerX = 180;
        playerY = 360;
        playerLabel.setBounds(playerX, playerY, 40, 40);
        gamePanel.add(playerLabel);




        // Add a border of wall images around the game panel
        ImageIcon wallIcon = new ImageIcon("walls.jpg");
        JLabel topWall = new JLabel(wallIcon);
        topWall.setBounds(0, 0, gamePanel.getWidth(), wallIcon.getIconHeight());
        gamePanel.add(topWall);
        gamePanel.setComponentZOrder(topWall, 0);
        JLabel leftWall = new JLabel(wallIcon);
        leftWall.setBounds(0, 0, wallIcon.getIconWidth(), gamePanel.getHeight());
        gamePanel.add(leftWall);
        gamePanel.setComponentZOrder(leftWall, 0);
        JLabel rightWall = new JLabel(wallIcon);
        rightWall.setBounds(gamePanel.getWidth() - wallIcon.getIconWidth(), 0, wallIcon.getIconWidth(), gamePanel.getHeight());
        gamePanel.add(rightWall);
        gamePanel.setComponentZOrder(rightWall, 0);
        JLabel bottomWall = new JLabel(wallIcon);
        bottomWall.setBounds(0, gamePanel.getHeight() - wallIcon.getIconHeight(), gamePanel.getWidth(), wallIcon.getIconHeight());
        gamePanel.add(bottomWall);
        gamePanel.setComponentZOrder(bottomWall, 0);



        // Set up the door label
        doorLabel = new JLabel(new ImageIcon("door.png"));
        doorLabel.setBounds(180, 20, 40, 40);
        gamePanel.add(doorLabel);



        // Set up the obstacle label
        ImageIcon obstacleIcon = new ImageIcon("Enemy.png");
        Image obstacleImage = obstacleIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        obstacleIcon = new ImageIcon(obstacleImage);
        obstacleLabel = new JLabel(obstacleIcon);
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
        setContentPane(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void movePlayer(int deltaX, int deltaY) {
        // Get the current player position
        int oldPlayerX = playerX;
        int oldPlayerY = playerY;

        // Move the player to the new position
        playerX += deltaX;
        playerY += deltaY;

        // Get the size of the player image
        Image playerImage = ((ImageIcon) playerLabel.getIcon()).getImage();
        int playerWidth = playerImage.getWidth(null);
        int playerHeight = playerImage.getHeight(null);

        // Check if the new player position is within the game panel boundaries
        if (playerX >= 0 && playerX <= gamePanel.getWidth() - playerWidth && playerY >= 0 && playerY <= gamePanel.getHeight() - playerHeight) {
            // Set the new position of the player label
            playerLabel.setBounds(playerX, playerY, playerWidth, playerHeight);
        } else {
            // If the new position is out of bounds, revert to the old position
            playerX = oldPlayerX;
            playerY = oldPlayerY;
        }

        // Check for collisions with obstacles and the door
        checkCollisions();
    }
    private void checkCollisions() {
        // Get the non-transparent bounds of the player sprite
        Rectangle playerBounds = playerLabel.getBounds();
        playerBounds.setLocation(playerX, playerY);

        // Check for collisions with the obstacle sprite
        Rectangle obstacleBounds = obstacleLabel.getBounds();
        if (playerBounds.intersects(obstacleBounds)) {
            JOptionPane.showMessageDialog(this, "You hit an obstacle!");
            playerX = 180;
            playerY = 360;
            playerLabel.setBounds(playerX, playerY, 40, 40);
            return;
        }

        // Check for collisions with the door sprite
        Rectangle doorBounds = doorLabel.getBounds();
        if (playerBounds.intersects(doorBounds)) {
            JOptionPane.showMessageDialog(this, "You escaped!");
            System.exit(0);
        }
    }



}
