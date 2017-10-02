package pde.two;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class Graphics extends JPanel implements Runnable, MouseListener, MouseMotionListener{

    private final int width, height;
    private final Logic logic;
    private final Type type;
    
    public Graphics(Logic logic, int width, int height, Type type){
        setPreferredSize(new Dimension(width, height));
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.width = width;
        this.height = height;
        this.logic = logic;
        this.type = type;
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
        
        BufferedImage img = new BufferedImage(logic.N, logic.M, BufferedImage.TYPE_INT_RGB);
        
        for(int i = 1; i < logic.N; i++)
            for(int j = 0; j < logic.M; j++){
                Particle2D p = logic.particles[i][j];
                img.setRGB(i, j, toRGB(p.z));
            }
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        
        g.setColor(Color.white);
        g.drawOval(logic.sineX * getWidth() / logic.N - 2,
                    logic.sineY * getHeight()/ logic.M - 2,
                    5, 5);
        
    }
    
    public int toRGB(double d){
        int r = 0, g = 0, b = 0;
        
        if(type == Type.heat){
            double val = d * 100;
                b = clampByte(-val);
                r = clampByte(val);
                
                val = Math.abs(val);
                g = clampByte(val - 200);
        }else if(type == Type.wave){
            int val = (int)(10 * d);
            if(val < 0)
                b = clampByte(-val);
            else
                r = clampByte(val);
            
            g = clampByte(Math.abs(val / 3) - 5);
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
