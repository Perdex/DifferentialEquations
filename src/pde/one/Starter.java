package pde.one;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
            case 2:
                t = Type.transport;
                break;
            case 3:
                t = Type.combination;
                break;
        }
        if(t == null)
            return;
        
        System.out.println("Type is: " + t);
        
        JFrame frame = new JFrame("It's all wiggily!!!");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int w = 1000, h = 600;
        Logic logic = new Logic(w, t);
        Graphics graphics = new Graphics(logic, w, h);
        frame.add(graphics);

        frame.pack();
        
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 - frame.getWidth() / 2), 
                        (int)(screen.getHeight() / 2 - frame.getHeight() / 2),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        //JOptionPane.showMessageDialog(null, t.getDescription());
        
        JFrame settings = makeSettings(logic);
        
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
    
    private JFrame makeSettings(Logic logic){
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        p.add(logic.type.makeOptions());
        
        JButton reset = new JButton("Reset");
        reset.setAlignmentX(0.5f);
        reset.addActionListener((ActionEvent e) -> {
            logic.reset();
        });
        p.add(Box.createVerticalStrut(10));
        p.add(reset);
        p.add(Box.createVerticalStrut(10));
        
        frame.add(p);
        frame.pack();
        //position frame to the center of screen
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 + 520), 
                        (int)(screen.getHeight() / 2 - frame.getHeight() / 2),
                        frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
        return frame;
    }
}
