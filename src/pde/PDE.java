package pde;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class PDE {

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Choose your equation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(Box.createVerticalStrut(10));
        
        int vert = 6, hor = 10;
        
        JButton b = new JButton("2D wave equation");
        b.setMargin(new Insets(vert, hor, vert, hor));
        b.setAlignmentX(0.5f);
        b.addActionListener((ActionEvent e) -> {
            new Thread(new pde.two.Starter(0)).start();
        });
        p.add(b);
        p.add(Box.createVerticalStrut(10));
        
        b = new JButton("2D heat equation");
        b.setMargin(new Insets(vert, hor, vert, hor));
        b.setAlignmentX(0.5f);
        b.addActionListener((ActionEvent e) -> {
            new Thread(new pde.two.Starter(1)).start();
        });
        p.add(b);
        p.add(Box.createVerticalStrut(10));
        
         b = new JButton("1D wave equation");
        b.setMargin(new Insets(vert, hor, vert, hor));
        b.setAlignmentX(0.5f);
        b.addActionListener((ActionEvent e) -> {
            new Thread(new pde.one.Starter(0)).start();
        });
        p.add(b);
        p.add(Box.createVerticalStrut(10));
        
        b = new JButton("1D heat equation");
        b.setMargin(new Insets(vert, hor, vert, hor));
        b.setAlignmentX(0.5f);
        b.addActionListener((ActionEvent e) -> {
            new Thread(new pde.one.Starter(1)).start();
        });
        p.add(b);
        p.add(Box.createVerticalStrut(10));
        
        b = new JButton("1D heat & wave");
        b.setMargin(new Insets(vert, hor, vert, hor));
        b.setAlignmentX(0.5f);
        b.addActionListener((ActionEvent e) -> {
            new Thread(new pde.one.Starter(3)).start();
        });
        p.add(b);
        p.add(Box.createVerticalStrut(10));
        
        b = new JButton("1D transport equation");
        b.setMargin(new Insets(vert, hor, vert, hor));
        b.setAlignmentX(0.5f);
        b.addActionListener((ActionEvent e) -> {
            new Thread(new pde.one.Starter(2)).start();
        });
        p.add(b);
        p.add(Box.createVerticalStrut(10));
        
        frame.add(p);
        frame.pack();
        
        frame.setLocation(200, 300);
        frame.setVisible(true);
        
//        Object[] options = {"1-dimensional", "2-dimensional"};
//        int choice = JOptionPane.showOptionDialog(null,
//                                                "How many dimensions to model?",
//                                                "Choose dimensionality",
//                                                JOptionPane.YES_NO_OPTION,
//                                                JOptionPane.PLAIN_MESSAGE,
//                                                null,
//                                                options,
//                                                options[0]);
//        
//        switch(choice){
//            case 0:
//                System.out.println("1D");
//                //new Thread(new pde.one.Starter()).start();
//                new pde.one.Starter().run();
//                break;
//            case 1:
//                System.out.println("1D");
//                //new Thread(new pde.one.Starter()).start();
//                new pde.two.Starter().run();
//                break;
//        }
    }

}
