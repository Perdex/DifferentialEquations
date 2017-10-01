package pde.one;


public class Logic{

    final int N;
    Particle1D[] particles;
    
    int mouseX, mouseY, mouseXprev = -1, mouseYprev = -1;
    boolean mouseActive = false;
    
    private Type type;

    Logic(int width, Type type){
        N = width + 1;
        particles = new Particle1D[N];
        this.type = type;
        for(int i = 0; i < N; i++)
            particles[i] = new Particle1D(i, 0, type);
    }
    
    public void start(){
        System.out.println("Starting logic loop");
        double dt = 1;
        double t = 0;
        while(true){
            
            //Calculate the differential equation
            for(int i = 1; i < N - 1; i++)
                particles[i].interact(dt, i, particles);
            
            //apply mouse interaction
            if(mouseActive)
                for(int i = 0; i < N-1; i++)
                    particles[i].mouseAt(mouseX, mouseY, mouseXprev, mouseYprev);
            
            //Set edge conditions
            if(type == Type.wave){
                particles[N-1].y = 20 * Math.sin(t * 0.01);
            }else if(type == Type.heat)
                particles[N-1].y = -150 * (1 - Math.cos(t * 0.002));
            
            //move the particles
            for(int i = 1; i < N; i++)
                particles[i].move(dt);
            
            t += dt;
            try{
                Thread.sleep(type.sleepTime);
            }catch(InterruptedException e){}
        }
    }
}
