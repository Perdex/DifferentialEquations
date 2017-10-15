package pde.one;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
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
            case 2:
                t = Type.transport;
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
        
        JFrame settings = makeSettings(logic, graphics);
        
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
    
    private JFrame makeSettings(Logic logic, Graphics graphics){
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);
        
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        MouseAdapter ma = new MouseAdapter(){
            int x, y;
            @Override
            public void mouseDragged(MouseEvent e){
                Point p = frame.getLocation();
                p.translate(e.getX() - x, e.getY() - y);
                frame.setLocation(p);
            }
            @Override
            public void mousePressed(MouseEvent e){
                x = e.getX();
                y = e.getY();
            }
        };

        p.addMouseListener(ma);
        p.addMouseMotionListener(ma);
        
        
        JPanel mid = new JPanel();
        mid.add(logic.type.makeOptions());
        p.add(mid);
        
        JButton reset = new JButton("Reset");
        reset.setAlignmentX(0.5f);
        reset.addActionListener((ActionEvent e) -> {
            logic.reset();
        });
        
        mid = new JPanel();
        mid.add(new JLabel("Edge wave freq:"));
        JSlider sYPos = new JSlider(0, 70, 0);
        sYPos.setAlignmentX(0.5f);
        sYPos.addChangeListener((ChangeEvent e) -> {
            logic.sine = (1.0/1000) * sYPos.getValue() * sYPos.getValue();
        });
        mid.add(sYPos);
        p.add(mid);
        
        mid = new JPanel();
        mid.add(new JLabel("Set draw time:"));
        JSlider drawTime = new JSlider(0, 9800, 8000);
        drawTime.setAlignmentX(0.5f);
        drawTime.addChangeListener((ChangeEvent e) -> {
            graphics.drawTime = 10000 - drawTime.getValue();
        });
        mid.add(drawTime);
        p.add(mid);
        
        JRadioButton toggleBlur = new JRadioButton("Motion blur");
        toggleBlur.setAlignmentX(0.5f);
        toggleBlur.addActionListener((ActionEvent e) -> {
            graphics.motionBlur = !graphics.motionBlur;
        });
        p.add(toggleBlur);
        
        
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
