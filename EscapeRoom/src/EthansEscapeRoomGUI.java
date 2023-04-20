import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.util.ArrayList;


public class EthansEscapeRoomGUI extends JFrame {
    private JLayeredPane gamePanel;
    private JLabel playerLabel;
    private JLabel doorLabel;

    private JLabel wallLabel;
    private JLabel obstacleLabel;

    private int playerX;
    private int playerY;
    private int currentLevel = 1;
    private int resetLevel = 1;

    private int maxLevel = 5;

    private boolean isProcessingCollision = false;
    private ArrayList<JLabel> enemyLabels = new ArrayList<>();
    private Timer enemyTimer;


    public EthansEscapeRoomGUI() {
        // Set up the game panel
        gamePanel = new JLayeredPane(); // initialize as JLayeredPane
        gamePanel.setLayout(null);
        gamePanel.setPreferredSize(new Dimension(400, 400));
        gamePanel.setBackground(Color.WHITE); // add a background color to make the wall components more visible
        setContentPane(gamePanel); // add the JLayeredPane as the content pane


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

        // Add the obstacleLabel to the enemyLabels list
        enemyLabels.add(obstacleLabel);

        // Set up a timer to move the obstacle every 2 seconds
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JLabel enemyLabel : enemyLabels) {
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

                    // Check if the new enemy position intersects with any wall images or other enemy sprites
                    boolean insideWall = false;
                    Rectangle enemyBounds = enemyLabel.getBounds();
                    Rectangle newEnemyBounds = new Rectangle(enemyBounds);
                    newEnemyBounds.translate(deltaX, deltaY);
                    for (Component c : gamePanel.getComponents()) {
                        if (c instanceof JLabel && ((JLabel) c).getIcon() != null && ((JLabel) c).getIcon().toString().contains("Wall.jpg")) {
                            Rectangle wallBounds = c.getBounds();
                            if (newEnemyBounds.intersects(wallBounds)) {
                                insideWall = true;
                                break;
                            }
                        }
                    }

                    // Check if the new position intersects with other enemy sprites
                    boolean collidesWithOtherEnemies = enemyCollidesWithOtherEnemies(newEnemyBounds, enemyLabel);

                    // Move the enemy to the new position if it doesn't intersect with any walls or other enemy sprites
                    if (!insideWall && !collidesWithOtherEnemies) {
                        int enemyX = enemyLabel.getX() + deltaX;
                        int enemyY = enemyLabel.getY() + deltaY;
                        enemyLabel.setBounds(enemyX, enemyY, 40, 40);
                    }
                }

                // Check for collisions with walls and the player
                checkCollisions();
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

    private void checkCollisions() {
        // If the method is already being called, return immediately
        if (isProcessingCollision) {
            return;
        }
        isProcessingCollision = true;

        // Get the non-transparent bounds of the player sprite
        Rectangle playerBounds = playerLabel.getBounds();
        playerBounds.setLocation(playerX, playerY);

        // Check for collisions with the enemy sprites
        for (JLabel enemyLabel : enemyLabels) {
            Rectangle enemyBounds = enemyLabel.getBounds();
            boolean enemyInsideWall = false;
            for (Component c : gamePanel.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getIcon() != null && ((JLabel) c).getIcon().toString().contains("Wall.jpg") && c.getBounds().intersects(enemyBounds)) {
                    enemyInsideWall = true;
                    break;
                }
            }
            if (playerBounds.intersects(enemyBounds) && !enemyInsideWall) {
                JOptionPane.showMessageDialog(this, "You hit an enemy! You will be set back one level.");
                currentLevel = Math.max(resetLevel, currentLevel - 1);
                SwingUtilities.invokeLater(() -> resetLevel(false));
                isProcessingCollision = false;
                return;
            }
        }

        // Check for collisions with the door sprite
        Rectangle doorBounds = doorLabel.getBounds();
        if (playerBounds.intersects(doorBounds)) {
            if (currentLevel < maxLevel) {
                JOptionPane.showMessageDialog(this, "You escaped Level " + currentLevel + "!");
                currentLevel++;
                SwingUtilities.invokeLater(() -> resetLevel(true));
            } else {
                JOptionPane.showMessageDialog(this, "Congratulations! You beat all levels.");
                System.exit(0);
            }
        }

        isProcessingCollision = false;
    }
    private boolean enemyCollidesWithOtherEnemies(Rectangle newEnemyBounds, JLabel currentEnemy) {
        for (JLabel enemyLabel : enemyLabels) {
            if (enemyLabel != currentEnemy && enemyLabel.getBounds().intersects(newEnemyBounds)) {
                return true;
            }
        }
        return false;
    }


    private void resetLevel(boolean levelCompleted) {
        // Reset the player position
        playerX = 180;
        playerY = 320;
        playerLabel.setBounds(playerX, playerY, 40, 40);

        // Remove all existing enemy sprites
        for (JLabel enemyLabel : enemyLabels) {
            gamePanel.remove(enemyLabel);
        }
        enemyLabels.clear();

        // Only update the level configuration if the player completed the level
        if (levelCompleted) {
            // Add new enemy sprite for each level
            for (int i = 0; i < currentLevel; i++) {
                ImageIcon enemyIcon = new ImageIcon("Enemy.png");
                Image enemyImage = enemyIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
                enemyIcon = new ImageIcon(enemyImage);
                JLabel enemyLabel = new JLabel(enemyIcon);

                // Keep generating random positions until it doesn't collide with any other enemies
                int newX = 0;
                int newY = 0;
                boolean collisionDetected = true;
                while (collisionDetected) {
                    newX = (int) (Math.random() * 300) + 40;
                    newY = (int) (Math.random() * 200) + 40;

                    collisionDetected = false;
                    for (JLabel currentEnemy : enemyLabels) {
                        if (enemyCollidesWithOtherEnemies(currentEnemy, newX, newY)) {
                            collisionDetected = true;
                            break;
                        }
                    }
                }

                enemyLabel.setBounds(newX, newY, 40, 40);
                gamePanel.add(enemyLabel, JLayeredPane.DEFAULT_LAYER);
                enemyLabels.add(enemyLabel);
            }
        }

        // Update the door label based on the current level
        if (currentLevel == 1) {
            doorLabel.setBounds(180, 0, 40, 50);
        } else {
            doorLabel.setBounds(180, 0, 40, 50); // hide the door label
        }
    }




    private boolean enemyCollidesWithWalls(JLabel enemyLabel, int newX, int newY) {
        // Check if the new enemy position collides with any wall images
        boolean insideWall = false;
        for (Component c : gamePanel.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getIcon() != null && ((JLabel) c).getIcon().toString().contains("Wall.jpg")) {
                Rectangle wallBounds = c.getBounds();
                Rectangle enemyBounds = enemyLabel.getBounds();
                enemyBounds.setLocation(newX, newY);
                if (enemyBounds.intersects(wallBounds)) {
                    insideWall = true;
                    break;
                }
            }
        }
        return insideWall;
    }

    private boolean enemyCollidesWithOtherEnemies(JLabel enemyLabel, int newX, int newY) {
        // Check if the new enemy position collides with any other enemies
        boolean overlapsEnemy = false;
        Rectangle newEnemyBounds = new Rectangle(newX, newY, enemyLabel.getWidth(), enemyLabel.getHeight());
        for (JLabel currentEnemy : enemyLabels) {
            Rectangle currentEnemyBounds = currentEnemy.getBounds();
            if (newEnemyBounds.intersects(currentEnemyBounds)) {
                overlapsEnemy = true;
                break;
            }
        }
        return overlapsEnemy;
    }

}