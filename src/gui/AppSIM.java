package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AppSIM {

    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AppSIM window = new AppSIM();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AppSIM() {
        initialize();
    }
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Game of SIM");
        frame.setBounds(100, 100, 450, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(new Color(245, 245, 245)); 
        
        frame.setLocationRelativeTo(null);
        
        JLabel lblGameOfSim = new JLabel("Game of SIM", SwingConstants.CENTER);
        lblGameOfSim.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblGameOfSim.setBounds(125, 25, 200, 35);
        frame.getContentPane().add(lblGameOfSim);
        
        int buttonWidth = 140;
        int buttonHeight = 30;
        int centerX = (450 - buttonWidth) / 2; 
        JButton btnVeryEasy = new JButton("Very Easy");
        btnVeryEasy.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnVeryEasy.setBounds(centerX, 85, buttonWidth, buttonHeight);
        btnVeryEasy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VeryEasyDialog ved = new VeryEasyDialog(frame);
                ved.setVisible(true);
            }
        });
        frame.getContentPane().add(btnVeryEasy);
        
        JButton btnEasy = new JButton("Easy");
        btnEasy.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnEasy.setBounds(centerX, 130, buttonWidth, buttonHeight);
        btnEasy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EasyDialog ed = new EasyDialog(frame);
                ed.setVisible(true);
            }
        });
        frame.getContentPane().add(btnEasy);
        
        // Medium
        JButton btnMedium = new JButton("Medium");
        btnMedium.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnMedium.setBounds(centerX, 175, buttonWidth, buttonHeight);
        btnMedium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MediumDialog md = new MediumDialog(frame);
                md.setVisible(true);
            }
        });
        frame.getContentPane().add(btnMedium);
        
        JButton btnImpossible = new JButton("Impossible");
        btnImpossible.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnImpossible.setBounds(centerX, 220, buttonWidth, buttonHeight);
        btnImpossible.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ImpossibleDialog id = new ImpossibleDialog(frame);
                id.setVisible(true);
            }
        });
        frame.getContentPane().add(btnImpossible);
    }
}