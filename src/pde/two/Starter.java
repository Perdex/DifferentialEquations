package pde.two;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import pde.Type;

public class Starter implements Runnable{

    private final int type;
    public Starter(int type){
        this.type = type;
    }
    
    @Override
    public void run(){
        Type t = null;
        switch(type){
            case 0:
                t = Type.wave;
                break;
            case 1:
                t = Type.heat;
                break;
        }
        if(t == null)
            return;
        
        System.out.println("Type is: " + t);
        
        JFrame frame = new JFrame("It's all wiggily!!!");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int w = 640, h = 640;
        Logic logic = new Logic(t, 320);
        Graphics graphics = new Graphics(logic, w, h, t);
        frame.add(graphics);

        frame.pack();
        
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
                        (int)(screen.getHeight() / 2 - frame.getHeight() / 2),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        
        JFrame settings = makeWaveSettings(logic, graphics);
        
        Thread graphicsThread = new Thread(graphics);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                settings.dispose();
                graphics.stop();
                logic.stop();
            }
        });
        
        graphicsThread.start();
        logic.start();
    }
    
    private JFrame makeWaveSettings(Logic logic, Graphics graphics){
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        p.add(logic.type.makeOptions());
        
        JPanel mid = new JPanel();
        mid.add(new JLabel("Color:"));
        JSlider colorSlider = new JSlider(1, 20, 5);
        colorSlider.setAlignmentX(0.5f);
        colorSlider.addChangeListener((ChangeEvent e) -> {
            graphics.colorStrength = 0.2 * colorSlider.getValue();
        });
        mid.add(colorSlider);
        p.add(mid);
        
        mid = new JPanel();
        mid.add(new JLabel("Sine wave generator:"));
        p.add(mid);
        
        mid = new JPanel();
        mid.add(new JLabel("F:"));
        JSlider sFreq = new JSlider(2, 20, 2);
        sFreq.setAlignmentX(0.5f);
        sFreq.addChangeListener((ChangeEvent e) -> {
            logic.sineFreq = 1.0 * sFreq.getValue();
        });
        mid.add(sFreq);
        p.add(mid);
        
        mid = new JPanel();
        mid.add(new JLabel("A:"));
        JSlider sStrength = new JSlider(0, 10, 0);
        sStrength.setAlignmentX(0.5f);
        sStrength.addChangeListener((ChangeEvent e) -> {
            logic.sineStrength = 30 * sStrength.getValue();
        });
        mid.add(sStrength);
        p.add(mid);
        
        mid = new JPanel();
        mid.add(new JLabel("x:"));
        JSlider sXPos = new JSlider(1, 399, 200);
        sXPos.setAlignmentX(0.5f);
        sXPos.addChangeListener((ChangeEvent e) -> {
            logic.sineX = sXPos.getValue() * logic.N / 400;
        });
        mid.add(sXPos);
        p.add(mid);
        
        mid = new JPanel();
        mid.add(new JLabel("y:"));
        JSlider sYPos = new JSlider(1, 399, 200);
        sYPos.setAlignmentX(0.5f);
        sYPos.addChangeListener((ChangeEvent e) -> {
            logic.sineY = sYPos.getValue() * logic.M / 400;
        });
        mid.add(sYPos);
        p.add(mid);
        
        
        JRadioButton toggleEdges = new JRadioButton("Freeze edges");
        toggleEdges.setSelected(true);
        toggleEdges.setAlignmentX(0.5f);
        toggleEdges.addActionListener((ActionEvent e) -> {
            logic.freezeEdges = !logic.freezeEdges;
        });
        p.add(toggleEdges);
        
        mid = new JPanel();
        JButton toggle = new JButton("Circular constraint");
        toggle.setAlignmentX(0.5f);
        toggle.addActionListener((ActionEvent e) -> {
            logic.setCircleConstraint();
        });
        mid.add(toggle);
        
        JButton toggle2 = new JButton("Image constraint");
        toggle2.setAlignmentX(0.5f);
        toggle2.addActionListener((ActionEvent e) -> {
            logic.setPictureConstraint();
        });
        mid.add(toggle2);
        
        JButton reset = new JButton("Reset");
        reset.setAlignmentX(0.5f);
        reset.addActionListener((ActionEvent e) -> {
            logic.reset();
        });
        mid.add(reset);
        p.add(mid);
        
        frame.add(p);
        frame.pack();
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 + 320), 
                        (int)(screen.getHeight() / 2 - frame.getHeight() / 2),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        return frame;
    }
}
