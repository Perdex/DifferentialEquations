package pde.two;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class Starter implements Runnable{

    private final int type;
    public Starter(int type){
        this.type = type;
    }
    
    @Override
    public void run(){
        
//        Object[] options = {"Wave equation", "Heat equation"};
//        String choice = (String)JOptionPane.showInputDialog(null,
//                                                "Which equation do you want to model?",
//                                                "Choose equation",
//                                                JOptionPane.PLAIN_MESSAGE,
//                                                null,
//                                                options,
//                                                0);
//        Type type = null;
//        if(choice.equals(options[0]))
//            type = Type.wave;
//        else if(choice.equals(options[1]))
//            type = Type.heat;
        
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
        
        int w = 1000, h = 600;
        Logic logic = new Logic(t);
        Graphics graphics = new Graphics(logic, w, h, t);
        frame.add(graphics);

        frame.pack();
        
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
                        (int)(screen.getHeight() / 2 - frame.getHeight() / 2),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        
        JFrame settings = makeWaveSettings(logic);
        //TODO close the settings appropriately
        
        JOptionPane.showMessageDialog(null, t.getDescription());
        
        new Thread(graphics).start();
        logic.start();
    }
    
    private JFrame makeWaveSettings(Logic logic){
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel p = new JPanel();
        
        p.add(new JLabel("F:"));
        JSlider sFreq = new JSlider(8, 20);
        sFreq.addChangeListener((ChangeEvent e) -> {
            logic.sineFreq = 0.1 * Math.pow(sFreq.getValue(), 2);
        });
        p.add(sFreq);
        
        p.add(new JLabel("A:"));
        JSlider sStrength = new JSlider(0, 10, 0);
        sStrength.addChangeListener((ChangeEvent e) -> {
            logic.sineStrength = 10 * sStrength.getValue();
        });
        p.add(sStrength);
        
        p.add(new JLabel("x:"));
        JSlider sXPos = new JSlider(0, 19, 9);
        sXPos.addChangeListener((ChangeEvent e) -> {
            logic.sineX = sXPos.getValue() * logic.N / 20;
        });
        p.add(sXPos);
        
        p.add(new JLabel("y:"));
        JSlider sYPos = new JSlider(0, 19, 9);
        sYPos.addChangeListener((ChangeEvent e) -> {
            logic.sineY = sYPos.getValue() * logic.M / 20;
        });
        p.add(sYPos);
        
        JButton reset = new JButton("Reset");
        reset.addActionListener((ActionEvent e) -> {
            logic.reset();
        });
        p.add(reset);
        
        frame.add(p);
        frame.pack();
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
                        (int)(screen.getHeight() / 2 - 400),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        return frame;
    }
}
