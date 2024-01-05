import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    int board_Height = 400;
    int board_Width = 400;
    int x[] = new int[1600]; //400*400
    int y[] = new int[1700];
    int dot_size = 10;
    int dots;
    int apple_x;
    int apple_y;
    Image body, head, apple;
    Timer timer;
    int DELAY = 300;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;
    int score = 0;
    int highestScore = 0;
    JButton restartButton;

    Board() {
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(board_Width, board_Height));
        setBackground(Color.blue);
        initializeGame();
        loadImages();

        // Creating and adding the restart button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            initializeGame();
            inGame = true;
            timer.start();
            requestFocus(); // To regain focus for key events
        });
        add(restartButton);

    }

    public void initializeGame() {
        dots = 3;
        x[0] = 250;
        y[0] = 250;
        for (int i = 1; i < dots; i++) {
            x[i] = x[0] + dot_size * i;
            y[i] = y[0];
        }
        locateApplePosition();  // Locate the initial apple position
        timer = new Timer(DELAY, this);
        timer.start();
    }
    // Load images for game elements
    public void loadImages() {
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }

    @Override
    // Paint the game components
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[0], y[0], this);
                } else {
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        } else {
            gameOver(g);
            timer.stop();
        }
    }
    // Locate a random position for the apple
    public void locateApplePosition() {
        apple_x = ((int) (Math.random() * 39)) * dot_size;
        apple_y = ((int) (Math.random() * 39)) * dot_size;
    }
    // Check collision with borders and snake's body
    public void checkCollision() {
        for (int i = 1; i < dots; i++) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (x[0] >= board_Width) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (y[0] >= board_Height) {
            inGame = false;
        }
    }


    // Display game over message and update highest score if needed
    public void gameOver(Graphics g) {
        String msg = "Game Over";
        String scoremsg = "Score: " + Integer.toString(score);
        String highestScoreMsg = "Highest Score: " + Integer.toString(highestScore);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (board_Width - fontMetrics.stringWidth(msg)) / 2, board_Height / 4);
        g.drawString(scoremsg, (board_Width - fontMetrics.stringWidth(scoremsg)) / 2, 3 * (board_Height / 4));
        g.drawString(highestScoreMsg, (board_Width - fontMetrics.stringWidth(highestScoreMsg)) / 2, 5 * (board_Height / 6));

        if (score > highestScore) {
            highestScore = score;
        }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        checkApple();
        checkCollision();
        snakeMove();
        repaint();
    }
    // Move the snake based on direction
    public void snakeMove() {
        for (int i = dots - 1; i >= 1; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= dot_size;
        }
        if (rightDirection) {
            x[0] += dot_size;
        }
        if (upDirection) {
            y[0] -= dot_size;
        }
        if (downDirection) {
            y[0] += dot_size;
        }
    }

    // Check if the snake eats the apple
    public void checkApple() {
        if (apple_x == x[0] && apple_y == y[0]) {
            dots++;
            score++;
            locateApplePosition();
        }
    }
    // Keyboard input handling
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();

            if (key == keyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == keyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == keyEvent.VK_UP && !downDirection) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            } else if (key == keyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            repaint();
        }
    }
}
