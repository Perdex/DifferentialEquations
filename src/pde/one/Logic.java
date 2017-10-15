package pde.one;

import pde.Type;


public class Logic{

    final int N;
    Particle1D[] particles;
    
    int mouseX, mouseY, mouseXprev = -1, mouseYprev = -1;
    boolean mouseActive = false, stopflag = false, resetflag = false;
    double sine = 0;
    final Type type;

    Logic(int width, Type type){
        this.N = width;
        this.particles = new Particle1D[N];
        this.type = type;
        for(int i = 0; i < N; i++)
            particles[i] = new Particle1D(i, 0, type);
    }
    
    void stop(){
        stopflag = true;
    }
    void reset(){
        resetflag = true;
    }
    
    public void start(){
        System.out.println("Starting logic loop");
        double dt = 0.01;
        long t = System.nanoTime();
        double sinePos = 0;
        while(!stopflag){
            
            if(resetflag){
                resetflag = false;
                sinePos = 0;
                for(int i = 0; i < N; i++)
                    particles[i] = new Particle1D(i, 0, type);
            }
            //Calculate the differential equation
            for(int i = 1; i < N - 1; i++)
                particles[i].interact(dt, 1.0/N, i, particles);
            
            //apply mouse interaction
            if(type == Type.transport)
                if(mouseActive)
                    particles[0].u = mouseY;
                else
                    particles[0].u = 0;
            else if(mouseActive)
                for(int i = 0; i < N-1; i++)
                    particles[i].mouseAt(mouseX, mouseY, mouseXprev, mouseYprev);
            
            //Set edge conditions
            if(type == Type.wave){
                particles[N-1].u = 20 * Math.sin(sinePos);
                sinePos += sine * dt * 20;
            }else if(type == Type.heat){
                particles[N-1].u = -150 * (1 - Math.cos(sinePos));
                sinePos += sine * dt * 10;
            }else if(type == Type.transport){
                particles[0].u = 200 * Math.sin(sinePos);
                sinePos += sine * dt * 3;
            }
            
            //move the particles
            for(int i = 1; i < N; i++)
                particles[i].move(dt);
            
            long now = System.nanoTime();
            dt = (now - t) * 1e-9;
            t = now;
            
            int towait = (int)(type.sleepTime - dt * 1e-6);
            
            dt = Math.min(dt, 0.001);
            
            if(towait > 10000)
                try{
                    Thread.sleep(towait/1000000, towait%1000000);
                }catch(InterruptedException e){}
        }
    }
}
