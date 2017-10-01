package pde.two;


public class Logic{

    final int N, M;
    Particle2D[][] particles;
    
    double mouseX, mouseY;
    int mouseActive = 0;
    
    private final Type type;

    Logic(Type type){
        N = 300;
        M = 300;
        particles = new Particle2D[N][M];
        this.type = type;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++)
            particles[i][j] = new Particle2D((double)i/N, (double)j/M, 0, type);
    }
    
    public void start(){
        System.out.println("Starting logic loop");
        double dt = 1, t = 0;
        while(true){
            
            //Calculate the differential equation
            for(int i = 1; i < N-1; i++)
                for(int j = 1; j < M-1; j++)
                    particles[i][j].interact(dt, i, j, particles);
            
            //apply mouse interaction
            if(mouseActive != 0){
                int range = 3;
                int xfrom = Math.max((int)(mouseX * N) - range, 1);
                int xto = Math.min((int)(mouseX * N) + range, N-2);
                int yfrom = Math.max((int)(mouseY * M) - range, 1);
                int yto = Math.min((int)(mouseY * M) + range, M-2);
                for(int i = xfrom; i <= xto; i++)
                    for(int j = yfrom; j <= yto; j++)
                        particles[i][j].mouseAt(mouseX, mouseY, mouseActive);
            }
            //Set edge conditions
//            if(type == Type.wave){
//                particles[N-1].y = 20 * Math.sin(t * 0.01);
//            }else if(type == Type.heat)
//                particles[N-1].y = -150 * (1 - Math.cos(t * 0.002));
            
            //move the particles
            for(int i = 1; i < N; i++)
                for(int j = 1; j < M; j++)
                    particles[i][j].move(dt);
            
            t += dt;
            try{
                Thread.sleep(type.sleepTime);
            }catch(InterruptedException e){}
        }
    }
}
