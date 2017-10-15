package pde.one;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class Graphics extends JPanel implements Runnable, MouseListener, MouseMotionListener{

    private final int width, height;
    private final Logic logic;
    private boolean stopflag = false;
    boolean motionBlur = false;
    int drawTime = 2000;
    
    public Graphics(Logic logic, int width, int height){
        setPreferredSize(new Dimension(width, height));
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.width = width;
        this.height = height;
        this.logic = logic;
    }
    
    void stop(){
        stopflag = true;
    }
    
    @Override
    public void run(){
        System.out.println("Starting draw loop");
        
        while(!stopflag){
            repaint();
            try{
                Thread.sleep(drawTime / 100, (drawTime % 100) * 10000);
            }catch(InterruptedException e){}
        }
    }
    
    @Override
    public void paint(java.awt.Graphics g){
        g.clearRect(0, 0, getWidth(), getHeight());
        float linecol = 0.7f;
        g.setColor(new Color(linecol, linecol, linecol));
        g.drawLine(0, getHeight() / 2, width, getHeight() / 2);
        
        g.setColor(Color.red);
        if(!motionBlur){
            for(int i = 1; i < logic.N; i++)
                g.drawLine(xInScreen(i-1), (int)logic.particles[i-1].u + getHeight() / 2,
                            xInScreen(i), (int)logic.particles[i].u + getHeight() / 2);
        }else{
            for(int i = 1; i < logic.N; i++)
                g.drawLine(xInScreen(i), (int)logic.particles[i].blurMin() + getHeight() / 2,
                            xInScreen(i), (int)logic.particles[i].blurMax() + getHeight() / 2);
        }
            
    }

    private int xInScreen(int i){
        return (int)((double)logic.particles[i].x / logic.N * getWidth());
    }

    @Override
    public void mousePressed(MouseEvent e){
        requestFocus();
        //When pressed, set prev values to same
        logic.mouseX = e.getX();
        logic.mouseY = e.getY() - height / 2;
        logic.mouseXprev = logic.mouseX;
        logic.mouseYprev = logic.mouseY;
        logic.mouseActive = true;
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
        //When dragged, set prev values to previous ones
        logic.mouseXprev = logic.mouseX;
        logic.mouseYprev = logic.mouseY;
        logic.mouseX = e.getX();
        logic.mouseY = e.getY() - height / 2;
    }

    @Override
    public void mouseReleased(MouseEvent e){
        logic.mouseActive = false;
    }

    @Override
    public void mouseClicked(MouseEvent e){
    }
    @Override
    public void mouseEntered(MouseEvent e){
    }
    @Override
    public void mouseExited(MouseEvent e){
    }
    @Override
    public void mouseMoved(MouseEvent e){
    }
}
