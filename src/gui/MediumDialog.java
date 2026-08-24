package gui;

import javax.swing.*;
import sim.*;
import sim.GameOfSim.Edge;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MediumDialog extends JDialog {
    private static final int SIZE = 6;
    private static final int NODE_RADIUS = 20;
    
    public enum GameColor { BLUE, RED }
    
    private GameColor[][] graph = new GameColor[SIZE][SIZE];
    
    private int[] nodeX = new int[SIZE];
    private int[] nodeY = new int[SIZE];
    private int firstSelectedNode = -1;
    
    private Edge[] winningOrLosingTriangle = null;
    
    private GameOfSim gos = new GameLogicMedium();
    private boolean blueTurn = true; 

    public MediumDialog (JFrame parent) {
        super(parent, "SIM - Medium Mode", true);
        
        setBounds(100, 100, 500, 500);
        setLocationRelativeTo(parent);
        
        GamePanel gamePanel = new GamePanel();
        gamePanel.setBackground(java.awt.Color.WHITE);
        
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedNode = findClickedNode(e.getX(), e.getY());
                
                if (clickedNode != -1) {
                    if (firstSelectedNode == -1) {
                        firstSelectedNode = clickedNode;
                    } else if (firstSelectedNode == clickedNode) {
                        firstSelectedNode = -1; 
                    } else {
                        int v1 = firstSelectedNode;
                        int v2 = clickedNode;
                
                        if (graph[v1][v2] == null) {
                            GameColor currentColor = GameColor.RED;
                            graph[v1][v2] = currentColor;
                            graph[v2][v1] = currentColor;
                            
                            boolean cont = true;
                            gos.playerPlays(v1, v2);
                            
                            if (gos.isGameEnded()) {
                                winningOrLosingTriangle = gos.getGameEndedTriangle();
                                gamePanel.repaint();
                                
                                JOptionPane.showMessageDialog(MediumDialog .this, "You lost!");
                                cont = false;
                            }
                            
                            if (cont) {
                            	long startTime = System.nanoTime();
                                GameOfSim.Edge edge = gos.computerPlays();
                                long endTime = System.nanoTime();
                                
                                double durationInMilliseconds = (endTime - startTime) / 1000000.0;
                                System.out.println("Bot took " + durationInMilliseconds + "ms");
                                
                                currentColor = GameColor.BLUE;
                                graph[edge.v1][edge.v2] = currentColor;
                                graph[edge.v2][edge.v1] = currentColor;
                                
                                if (gos.isGameEnded()) {
                                    winningOrLosingTriangle = gos.getGameEndedTriangle();
                                    gamePanel.repaint();
                                    JOptionPane.showMessageDialog(MediumDialog .this, "You won!");
                                }
                            }
                        }
                        firstSelectedNode = -1; 
                    }
                    gamePanel.repaint();
                }
            }
        });
        
        getContentPane().add(gamePanel);
    }

    private boolean isPartOfEndTriangle(int u, int v) {
        if (winningOrLosingTriangle == null) return false;
        
        for (Edge e : winningOrLosingTriangle) {
            if (e != null) {
                if ((e.v1 == u && e.v2 == v) || (e.v1 == v && e.v2 == u)) {
                    return true;
                }
            }
        }
        return false;
    }

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
                    
                    if (isPartOfEndTriangle(i, j)) {
                        g2.setColor(java.awt.Color.YELLOW); 
                        g2.setStroke(new BasicStroke(8)); 
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    }

                    if (graph[i][j] == GameColor.BLUE) {
                        g2.setColor(java.awt.Color.BLUE);
                        g2.setStroke(new BasicStroke(3)); 
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    } else if (graph[i][j] == GameColor.RED) {
                        g2.setColor(java.awt.Color.RED);
                        g2.setStroke(new BasicStroke(3));
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    } else {
                        g2.setColor(java.awt.Color.LIGHT_GRAY);
                        g2.setStroke(new BasicStroke(1));
                        g2.drawLine(nodeX[i], nodeY[i], nodeX[j], nodeY[j]);
                    }
                }
            }

            for (int i = 0; i < SIZE; i++) {
                if (i == firstSelectedNode) {
                    g2.setColor(java.awt.Color.YELLOW);
                } else {
                    g2.setColor(java.awt.Color.BLACK);
                }
                g2.fillOval(nodeX[i] - NODE_RADIUS, nodeY[i] - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2.setColor(java.awt.Color.BLACK);
                g2.drawOval(nodeX[i] - NODE_RADIUS, nodeY[i] - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                
                g2.setColor(java.awt.Color.WHITE);
                g2.drawString(String.valueOf(i), nodeX[i] - 4, nodeY[i] + 4);
            }
        }
    }

    public static void main(String[] args) {
        JFrame dummyFrame = new JFrame();
        dummyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        MediumDialog  dialog = new MediumDialog(dummyFrame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}