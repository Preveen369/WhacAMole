import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class WhacAMole {
    // Create a new game window
    int boardWidth = 600;
    int boardHeight = 650;

    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton resetButton = new JButton("Reset");  // Reset button
    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon plantIcon;

    // To track the current tile of mole and plant
    JButton currMoleTile;
    JButton currPlantTile;

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    int score;

    WhacAMole() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.PLAIN, 45));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: 0");
        textLabel.setOpaque(true);

        textLabel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(resetButton);  // Add reset button
        resetButton.setFocusable(false);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        boardPanel.setBackground(Color.BLACK);
        frame.add(boardPanel);

        // plantIcon = new ImageIcon(getClass().getResource("./piranha.png"));  -> load image directly from source

        // to load the image from source, scale it to req. dimensions, loading them as an image icon
        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        score = 0;  // initialize the game score before button clicking...

        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;    // increment the score as many times, mole is clicked
                        textLabel.setText("Score: " + Integer.toString(score));
                    } else if (tile == currPlantTile) {
                        textLabel.setText("Game Over: " + Integer.toString(score));
                        setMoleTimer.stop();    // stop the mole's movement
                        setPlantTimer.stop();   // stop the plant's movement
                        for (int i = 0; i < 9; i++) {
                            board[i].setEnabled(false); // disable the buttons
                        }
                    }
                }
            });
        }

        setMoleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove mole from current mole
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }

                // randomly select another tile
                int num = random.nextInt(9); // 0-8
                JButton tile = board[num];

                // EDGE CASE
                // if tile is occupied by plant, skip tile for this turn
                if (currPlantTile == tile)  return;

                // set the tile to mole
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });

        setPlantTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove plant from current plant
                if (currPlantTile != null) {
                    currPlantTile.setIcon(null);
                    currPlantTile = null;
                }

                // randomly select another tile
                int num = random.nextInt(9); // 0-8
                JButton tile = board[num];

                // if tile is occupied by mole, skip tile for this turn
                if (currMoleTile == tile)   return;

                // set the tile to plant
                currPlantTile = tile;
                currPlantTile.setIcon(plantIcon);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        setMoleTimer.start();
        setPlantTimer.start();
        frame.setVisible(true);
    }

    private void resetGame() {
        score = 0;
        textLabel.setText("Score: 0");

        if (currMoleTile != null) {
            currMoleTile.setIcon(null);
            currMoleTile = null;
        }
        if (currPlantTile != null) {
            currPlantTile.setIcon(null);
            currPlantTile = null;
        }

        for (JButton tile : board) {
            tile.setEnabled(true);
            tile.setIcon(null);
        }

        setMoleTimer.restart();
        setPlantTimer.restart();
    }
}
