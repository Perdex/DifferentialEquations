package pde.two;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import pde.Type;

public class Logic{

    final int N, M;
    Particle2D[][] particles;

    double mouseX, mouseY;
    int mouseActive = 0;

    double sineFreq = 0;
    double sineStrength = 50;
    boolean sineActive = False;
    int sineX, sineY;
    boolean circleConstraint = false, pictureConstraint = false, freezeEdges = true;
    double[][] pic;

    private boolean resetflag = false, stopflag = false;

    final Type type;

    Logic(Type type, int size){
        N = size;
        M = size;
        sineX = N / 2;
        sineY = M / 2;
        particles = new Particle2D[N][M];
        this.type = type;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++)
            particles[i][j] = new Particle2D((double)i/N, (double)j/M, i, j, 0, type);
        
        pic = new double[N][M];
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++)
                pic[i][j] = 1;
    }

    void stop(){
        stopflag = true;
    }

    public void reset(){
        resetflag = true;
    }

    public void setCircleConstraint(){
        if(circleConstraint){
            circleConstraint = false;
            for(int i = 0; i < N; i++)
                for(int j = 0; j < M; j++)
                    pic[i][j] = 1;
            return;
        }

        circleConstraint = true;
        pictureConstraint = false;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++){
                int dx = i - N / 2;
                int dy = j - M / 2;
                pic[i][j] = dx * dx + dy * dy < Math.pow(Math.min(N, M)/2, 2) ? 1 : 0;
            }
    }

    public void setPictureConstraint(){
        if(pictureConstraint){
            pictureConstraint = false;
            for(int i = 0; i < N; i++)
                for(int j = 0; j < M; j++)
                    pic[i][j] = 1;
            return;
        }

        BufferedImage img = null;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;

        File file = fc.getSelectedFile();
        try{
            img = ImageIO.read(file);
        }catch(IOException e){
            System.err.println("Error when opening file:\n" + e);
        }

        if(img == null)
            return;

        int imgw = img.getWidth();
        int imgh = img.getHeight();
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++)
                pic[i][j] = 1 - (img.getRGB(i * imgw / N, j * imgh / M) & 0xFF) * (1. / 256.);

        pictureConstraint = true;
        circleConstraint = false;
    }

    public void start(){
        System.out.println("Starting logic loop");
        double dt = 0.001;
        long t = System.nanoTime();
        double sinePos = 0;
        while(!stopflag){
            
            if(resetflag){
                resetflag = false;
                for(int i = 0; i < N; i++)
                    for(int j = 0; j < M; j++)
                        particles[i][j].reset();
            }
            int buff = freezeEdges ? 1 : 0;
            //Calculate the differential equation
            Particle2D.interact(dt, 1.0/N, 1.0/M, particles, pic, buff, N-buff, buff, M-buff);
            
            //apply mouse interaction
            if(mouseActive != 0){
                int range = 5;
                int xfrom = Math.max((int)(mouseX * N) - range, 1);
                int xto = Math.min((int)(mouseX * N) + range, N-2);
                int yfrom = Math.max((int)(mouseY * M) - range, 1);
                int yto = Math.min((int)(mouseY * M) + range, M-2);
                for(int i = xfrom; i <= xto; i++)
                    for(int j = yfrom; j <= yto; j++)
                        particles[i][j].mouseAt(mouseX, mouseY, mouseActive);
            }
            
            if(sineActive){
                if(sineFreq == 0){
                    particles[sineX][sineY].u = 0;
                    sinePos = 0;
                }else{
                    //Add sine generator effect
                    particles[sineX][sineY].u += sineStrength * Math.sin(sinePos);
                    sinePos += dt * sineFreq * 2;
                }
            }
            
            //move the particles
            for(int i = 1; i < N-1; i++)
                for(int j = 1; j < M-1; j++)
                    particles[i][j].move(dt);
            
            long now = System.nanoTime();
            dt = (now - t) * 1e-9;
            t = now;
            
            int towait = (int)(type.sleepTime - dt * 1e-6);
            
            dt = Math.min(dt, 0.0012);
            
            if(towait > 10000)
                try{
                    System.out.println("waiting " + towait/1000000 + "ms");
                    Thread.sleep(towait/1000000, towait%1000000);
                }catch(InterruptedException e){}
        }
    }
}
