import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;

public class EscapeRoomGUI extends JFrame {
    private JLayeredPane gamePanel;
    private JLabel playerLabel;
    private JLabel doorLabel;

    private JLabel obstacleLabel;

    private int playerX;
    private int playerY;

    public EscapeRoomGUI() {
        // Set up the game panel
        gamePanel = new JLayeredPane(); // initialize as JLayeredPane
        gamePanel.setLayout(null);
        gamePanel.setPreferredSize(new Dimension(400, 400));
        gamePanel.setBackground(Color.WHITE); // add a background color to make the wall components more visible
        setContentPane(gamePanel); // add the JLayeredPane as the content pane
        GameStatus status = GameStatus.ACTIVE;

        // Set up the player label
        ImageIcon playerIcon = new ImageIcon("player.gif");
        Image playerImage = playerIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        playerIcon = new ImageIcon(playerImage);
        playerLabel = new JLabel(playerIcon);
        playerX = 180;
        playerY = 320;
        playerLabel.setBounds(playerX, playerY, 40, 40);
        gamePanel.add(playerLabel, JLayeredPane.DEFAULT_LAYER);


        // Set up the wall icon
        ImageIcon wallIcon = new ImageIcon("Wall.jpg");
        //setting up Top wall Right.
        JLabel topWallR = new JLabel(wallIcon);
        topWallR.setBounds(220, 0, 200, 40);
        gamePanel.add(topWallR, JLayeredPane.DEFAULT_LAYER);
        //setting up Top wall Left.
        JLabel topWallL = new JLabel(wallIcon);
        topWallL.setBounds(0, 0, 180, 40);
        gamePanel.add(topWallL, JLayeredPane.DEFAULT_LAYER);
        //setting up left wall.
        JLabel leftWall = new JLabel(wallIcon);
        leftWall.setBounds(0, 0, 40, 310);
        gamePanel.add(leftWall, JLayeredPane.DEFAULT_LAYER);
        //setting up left wall2.
        JLabel leftWall2 = new JLabel(wallIcon);
        leftWall2.setBounds(0, 200, 40, 310);
        gamePanel.add(leftWall2, JLayeredPane.DEFAULT_LAYER);
        //setting up right wall.
        JLabel rightWall = new JLabel(wallIcon);
        rightWall.setBounds(360, 0, 40, 310);
        gamePanel.add(rightWall, JLayeredPane.DEFAULT_LAYER);
        //setting up right wall2.
        JLabel rightWall2 = new JLabel(wallIcon);
        rightWall2.setBounds(360, 200, 40, 310);
        gamePanel.add(rightWall2, JLayeredPane.DEFAULT_LAYER);

        //setting up Bottom wall.
        JLabel bottomWall = new JLabel(wallIcon);
        bottomWall.setBounds(0, 360, 280, 40);
        gamePanel.add(bottomWall, JLayeredPane.DEFAULT_LAYER);
        //setting up Bottom Wall2.
        JLabel bottomWall2 = new JLabel(wallIcon);
        bottomWall2.setBounds(215, 360, 280, 40);
        gamePanel.add(bottomWall2, JLayeredPane.DEFAULT_LAYER);


        // Set up the door label
        doorLabel = new JLabel(new ImageIcon("door.png"));
        doorLabel.setBounds(180, 0, 40, 50);
        gamePanel.add(doorLabel, JLayeredPane.DEFAULT_LAYER);

        // Set up the obstacle label
        ImageIcon obstacleIcon = new ImageIcon("Enemy.png");
        Image obstacleImage = obstacleIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        obstacleIcon = new ImageIcon(obstacleImage);
        obstacleLabel = new JLabel(obstacleIcon);
        obstacleLabel.setBounds(120, 100, 40, 40);
        gamePanel.add(obstacleLabel, JLayeredPane.DEFAULT_LAYER);

        // Set up the obstacle label 2
        ImageIcon obstacleIcon2 = new ImageIcon("Enemy2.png");
        Image obstacleImage2 = obstacleIcon2.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        obstacleIcon2 = new ImageIcon(obstacleImage2);
        obstacleLabel = new JLabel(obstacleIcon2);
        obstacleLabel.setBounds(220, 200, 40, 40);
        gamePanel.add(obstacleLabel, JLayeredPane.DEFAULT_LAYER);

        // Set up a timer to move the obstacle every 2 seconds
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Generate a random direction
                int direction = (int) (Math.random() * 4);
                int deltaX = 0;
                int deltaY = 0;
                switch (direction) {
                    case 0:
                        deltaX = 10;
                        break;  // move right
                    case 1:
                        deltaX = -10;
                        break; // move left
                    case 2:
                        deltaY = 10;
                        break;  // move down
                    case 3:
                        deltaY = -10;
                        break; // move up
                }

                // Check if the new obstacle position intersects with any wall images
                boolean insideWall = false;
                for (Component c : gamePanel.getComponents()) {
                    if (c instanceof JLabel && ((JLabel) c).getIcon() != null && ((JLabel) c).getIcon().toString().contains("Wall.jpg")) {
                        Rectangle wallBounds = c.getBounds();
                        Rectangle obstacleBounds = obstacleLabel.getBounds();
                        obstacleBounds.translate(deltaX, deltaY);
                        if (obstacleBounds.intersects(wallBounds)) {
                            insideWall = true;
                            break;
                        }
                    }
                }

                // Move the obstacle to the new position if it doesn't intersect with any walls
                if (!insideWall) {
                    int obstacleX = obstacleLabel.getX() + deltaX;
                    int obstacleY = obstacleLabel.getY() + deltaY;
                    obstacleLabel.setBounds(obstacleX, obstacleY, 40, 40);
                }

                // Check for collisions with walls and the player
                try {
                    checkCollisions();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        timer.start();


        // Set up the key listener to move the player

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    try {
                        movePlayer(-10, 0);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    try {
                        movePlayer(10, 0);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (keyCode == KeyEvent.VK_UP) {
                    try {
                        movePlayer(0, -10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    try {
                        movePlayer(0, 10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
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


    private void movePlayer(int deltaX, int deltaY) throws InterruptedException {
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
        if (playerX >= 40 && playerX <= gamePanel.getWidth() - playerWidth - 40 && playerY >= 40 && playerY <= gamePanel.getHeight() - playerHeight - 40) {
            // Check if the new player position is inside the wall area
            boolean insideWall = false;
            for (Component c : gamePanel.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getIcon() != null && ((JLabel) c).getIcon().toString().contains("Wall.jpg") && c.getBounds().intersects(playerX, playerY, playerWidth, playerHeight)) {
                    insideWall = true;
                    break;
                }
            }
            if (!insideWall) {
                // Set the new position of the player label
                playerLabel.setBounds(playerX, playerY, playerWidth, playerHeight);
            } else {
                // If the new position is inside a wall, revert to the old position
                playerX = oldPlayerX;
                playerY = oldPlayerY;
            }
        } else {
            // If the new position is out of bounds, revert to the old position
            playerX = oldPlayerX;
            playerY = oldPlayerY;
        }

        // Check for collisions with obstacles and the door
        checkCollisions();
    }


    private void checkCollisions() throws InterruptedException {
        // Get the non-transparent bounds of the player sprite
        Rectangle playerBounds = playerLabel.getBounds();
        playerBounds.setLocation(playerX, playerY);

        // Check for collisions with the obstacle sprite
        Rectangle obstacleBounds = obstacleLabel.getBounds();
        int deltaX = obstacleBounds.x - obstacleLabel.getX();
        int deltaY = obstacleBounds.y - obstacleLabel.getY();
        boolean obstacleInsideWall = false;
        for (Component c : gamePanel.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getIcon() != null && ((JLabel) c).getIcon().toString().contains("Wall.jpg") && c.getBounds().intersects(obstacleBounds)) {
                obstacleInsideWall = true;
                break;
            }
        }
        if (playerBounds.intersects(obstacleBounds) && !obstacleInsideWall) {
            JOptionPane.showMessageDialog(this, "You hit an enemy!");
            playerX = 180;
            playerY = 320;
            playerLabel.setBounds(playerX, playerY, 40, 40);
            return;
        }

        // Check for collisions with the door sprite
        Rectangle doorBounds = doorLabel.getBounds();
        if (playerBounds.intersects(doorBounds)) {
            JOptionPane.showMessageDialog(this, "Level 1 Complete");
            GameStatus status = GameStatus.PAUSED;
            transitionToNextLevel();
        }
    }
    private void transitionToNextLevel() {
        // Update the panel for the next level
        gamePanel.removeAll();
        gamePanel.setLayout(new FlowLayout());
        JLabel level2Label = new JLabel("Level 2");
        gamePanel.add(level2Label);
        gamePanel.revalidate(); // Refresh the panel
        gamePanel.
        repaint();
        setBackground(Color.WHITE);
    }
}
