package pde.one;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Starter implements Runnable{

    @Override
    public void run(){
        
        Object[] options = {"Wave equation", "Heat equation", "Transport equation", "Damped transport equation"};
        String choice = (String)JOptionPane.showInputDialog(null,
                                                "Which equation do you want to model?",
                                                "Choose equation",
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                options,
                                                0);
        Type type = null;
        if(choice.equals(options[0]))
            type = Type.wave;
        else if(choice.equals(options[1]))
            type = Type.heat;
        else if(choice.equals(options[2]))
            type = Type.transport;
        else if(choice.equals(options[3]))
            type = Type.dampedTransport;
        
        if(type == null)
            return;
        
        System.out.println("Type is: " + type);
        
        JFrame frame = new JFrame("It's all wiggily!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int w = 1000, h = 600;
        Logic logic = new Logic(w, type);
        Graphics graphics = new Graphics(logic, w, h);
        frame.add(graphics);

        frame.pack();
        
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
                        (int)(screen.getHeight() / 2 - frame.getHeight() / 2),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        JOptionPane.showMessageDialog(null, type.getDescription());
        
        new Thread(graphics).start();
        logic.start();
    }
}
