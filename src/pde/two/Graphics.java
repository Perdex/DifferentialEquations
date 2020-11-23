package pde.two;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import pde.Type;

public class Graphics extends JPanel implements Runnable, MouseListener, MouseMotionListener{

    private final Logic logic;
    private final Type type;
    private boolean stopflag = false;
    double colorStrength = 1;
    
    public Graphics(Logic logic, int width, int height, Type type){
        setPreferredSize(new Dimension(width, height));
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.logic = logic;
        this.type = type;
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
                Thread.sleep(50);
            }catch(InterruptedException e){}
        }
    }
    
    @Override
    public void paint(java.awt.Graphics g){
        
        g.clearRect(0, 0, getWidth(), getHeight());
        float linecol = 0.9f;
        g.setColor(new Color(linecol, linecol, linecol));
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight()/2);
        
        BufferedImage img = new BufferedImage(logic.N, logic.M, BufferedImage.TYPE_INT_RGB);
        
        for(int i = 1; i < logic.N; i++)
            for(int j = 0; j < logic.M; j++){
                Particle2D p = logic.particles[i][j];
                img.setRGB(i, j, toRGB(p.u, 2-logic.pic[i][j]));
            }
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        
        //draw sine gen pos
        if(logic.sineStrength != 0){
            g.setColor(Color.white);
            g.drawOval(logic.sineX * getWidth() / logic.N - 2,
                        logic.sineY * getHeight()/ logic.M - 2,
                        5, 5);
        }
    }
    
    public int toRGB(double d, double multiplier){
        d *= colorStrength;
        int r = 0, g = 0, b = 0;
        
        if(type == Type.heat){
            double val = d * 50;
            r = clampByte(val);
            r = Math.max(r, clampByte(-val - 520));
            g = clampByte(Math.abs(val) - 220);
            b = clampByte(-val);
            b = Math.max(b, clampByte(val - 520));
        }else if(type == Type.wave){
            int val = (int)(20 * d);
            if(val < 0)
                b = clampByte(-val);
            else
                r = clampByte(val);
            b = clampByte(b + multiplier * 100);
            r = clampByte(r + multiplier * 100);
            
            g = clampByte(Math.abs(val / 3) - 5 + multiplier * 100);
        }
        
        return (r << 16) + (g << 8) + b;
    }

    int clampByte(double d){
        return d > 0 ? d < 255 ? (int)d : 255 : 0;
    }
    
    int button = 0;
    @Override
    public void mousePressed(MouseEvent e){
        logic.mouseX = (double)e.getX() / getWidth();
        logic.mouseY = (double)e.getY() / getHeight();
        logic.mouseActive = e.getButton() == 1 ? 1 : -1;
        button = e.getButton();
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
        logic.mouseX = (double)e.getX() / getWidth();
        logic.mouseY = (double)e.getY() / getHeight();
        logic.mouseActive = button == 1 ? 1 : -1;
    }

    @Override
    public void mouseReleased(MouseEvent e){
        logic.mouseActive = 0;
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
