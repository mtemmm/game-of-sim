package gui;

import client.Client;
import client.NetworkListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class GameDialog extends JDialog implements NetworkListener {

    private static final int SIZE = 6;
    private static final int NODE_RADIUS = 20;

    public enum GameColor { BLUE, RED, YELLOW }

    private final GameColor[][] graph = new GameColor[SIZE][SIZE];
    private final int[] nodeX = new int[SIZE];
    private final int[] nodeY = new int[SIZE];

    private int firstSelectedNode = -1;
    private boolean waitingForServer = false;

    private Client client;
    private final GamePanel gamePanel;
    private final JLabel statusLabel;

    public GameDialog(JFrame parent, String difficultyLevel) {
        super(parent, "SIM - " + difficultyLevel + " Mode", false);

        setBounds(100, 100, 500, 540);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Connecting to server...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        add(statusLabel, BorderLayout.NORTH);

        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.WHITE);
        add(gamePanel, BorderLayout.CENTER);

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (waitingForServer) return;

                int clickedNode = findClickedNode(e.getX(), e.getY());
                if (clickedNode == -1) return;

                if (firstSelectedNode == -1) {
                    firstSelectedNode = clickedNode;
                } else if (firstSelectedNode == clickedNode) {
                    firstSelectedNode = -1;
                } else {
                    int v1 = firstSelectedNode;
                    int v2 = clickedNode;

                    if (graph[v1][v2] == null) {
                        graph[v1][v2] = GameColor.RED;
                        graph[v2][v1] = GameColor.RED;

                        waitingForServer = true;
                        statusLabel.setText("Computer thinking...");
                        client.sendMove(v1, v2);
                    }
                    firstSelectedNode = -1;
                }
                gamePanel.repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client != null) {
                    client.disconnect();
                }
            }
        });

        initNetwork(difficultyLevel);
    }

    private void initNetwork(String level) {
        client = new Client(1234, this, "localhost");
        new Thread(() -> {
            try {
                client.connect();
                client.sendStart(level);
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> 
                    error("Could not connect to server at localhost:1234")
                );
            }
        }).start();
    }

    // --- NetworkListener Implementation ---

    @Override
    public void gameStarted(String level) {
        SwingUtilities.invokeLater(() -> {
            waitingForServer = false;
            statusLabel.setText("Game started (" + level + "). Your turn!");
            gamePanel.repaint();
        });
    }

    @Override
    public void computerMove(int v1, int v2) {
        SwingUtilities.invokeLater(() -> {
            graph[v1][v2] = GameColor.BLUE;
            graph[v2][v1] = GameColor.BLUE;
            waitingForServer = false;
            statusLabel.setText("Your turn!");
            gamePanel.repaint();
        });
    }

    @Override
    public void gameOver(boolean won, int v1, int v2, int v3) {
        SwingUtilities.invokeLater(() -> {
            GameColor finalColor = GameColor.YELLOW;
            
            graph[v1][v2] = finalColor; graph[v2][v1] = finalColor;
            graph[v2][v3] = finalColor; graph[v3][v2] = finalColor;
            graph[v1][v3] = finalColor; graph[v3][v1] = finalColor;

            gamePanel.repaint();

            waitingForServer = true;
            String message = won ? "You Won! Computer formed a triangle." : "You Lost! You formed a triangle.";
            statusLabel.setText(message);
            JOptionPane.showMessageDialog(GameDialog.this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void invalidMove() {
        SwingUtilities.invokeLater(() -> {
            waitingForServer = false;
            statusLabel.setText("Invalid move! Try selecting another line.");
        });
    }

    @Override
    public void error(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Error: " + message);
            JOptionPane.showMessageDialog(GameDialog.this, message, "Network Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    // --- Drawing & Geometry ---

    private void calculateNodePositions(int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY) - 50;

        for (int i = 0; i < SIZE; i++) {
            double angle = i * (2 * Math.PI / SIZE);
            nodeX[i] = (int) (centerX + radius * Math.cos(angle));
            nodeY[i] = (int) (centerY + radius * Math.sin(angle));
        }
    }

    private int findClickedNode(int x, int y) {
        for (int i = 0; i < SIZE; i++) {
            int dx = x - nodeX[i];
            int dy = y - nodeY[i];
            if (Math.sqrt(dx * dx + dy * dy) <= NODE_RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            calculateNodePositions(getWidth(), getHeight());

            for (int i = 0; i < SIZE; i++) {
                for (int j = i + 1; j < SIZE; j++) {
                    if (graph[i][j] == GameColor.BLUE) {
                        g2.setColor(Color.BLUE);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    } else if (graph[i][j] == GameColor.RED) {
                        g2.setColor(Color.RED);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    } else if (graph[i][j] == GameColor.YELLOW) {
                        g2.setColor(Color.yellow);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    	
                    }
                    else {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.setStroke(new BasicStroke(1));
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    }
                }
            }

            for (int i = 0; i < SIZE; i++) {
                g2.setColor(i == firstSelectedNode ? Color.YELLOW : Color.BLACK);
                g2.fillOval(nodeX[i] - NODE_RADIUS, nodeY[i] - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2.setColor(Color.BLACK);
                g2.drawOval(nodeX[i] - NODE_RADIUS, nodeY[i] - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);

                g2.setColor(Color.WHITE);
                g2.drawString(String.valueOf(i), nodeX[i] - 4, nodeY[i] + 4);
            }
        }
    }
}