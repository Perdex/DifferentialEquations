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
    
    public Graphics(Logic logic, int width, int height){
        setPreferredSize(new Dimension(width, height));
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.width = width;
        this.height = height;
        this.logic = logic;
    }
    

    @Override
    public void run(){
        System.out.println("Starting draw loop");
        
        while(true){
            repaint();
            try{
                Thread.sleep(50);
            }catch(InterruptedException e){}
        }
    }
    
    @Override
    public void paint(java.awt.Graphics g){
        
        g.clearRect(0, 0, width, height);
        float linecol = 0.9f;
        g.setColor(new Color(linecol, linecol, linecol));
        g.drawLine(0, height / 2, width, height/2);
        
        g.setColor(Color.red);
        for(int i = 1; i < logic.N; i++)
            g.drawLine((int)logic.particles[i-1].x, (int)logic.particles[i-1].y + height / 2,
                        (int)logic.particles[i].x, (int)logic.particles[i].y + height / 2);
    }


    @Override
    public void mousePressed(MouseEvent e){
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
