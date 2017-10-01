package pde.one;


public class Particle1D {
    
    private static final double swave = 0.2, dampwave = 0.002, sheat = 0.99, tdamp = 0.00001;
    double y, vy;
    int x;
    private final Type type;

    Particle1D(int x, double vy, Type type){
        this.x = x;
        y = 0;
        this.vy = vy;
        this.type = type;
    }
    
    void mouseAt(int mx, int my, int mxprev, int myprev){
        int dx = mx - x;
        
        int xhigh = Math.max(mx, mxprev);
        int xlow = Math.min(mx, mxprev);
        
        double dy = my - y;
        
        switch(type){
            case wave:
                vy += 0.001 * dy / Math.abs(dx);
            case heat:
                if(x >= xlow && x <= xhigh){
                    vy = 0;
                    y = my;
                }
                break;
            case transport:
            case dampedTransport:
                //The options for x = 1 and x = 2 smoothen the curve
                if(x == 0)
                    y = my;
                if(x == 1 && Math.abs(y) < Math.abs(my * 0.8)){
                    y *= 0.5;
                    y += 0.4 * my;
                }
                if(x == 2 && Math.abs(y) < Math.abs(my * 0.5)){
                    y *= 0.5;
                    y += 0.25 * my;
                }
                break;
        }
    }
    
    void interact(double dt, int idx, Particle1D[] p){
        
        double average = (p[idx - 1].y + p[idx + 1].y) / 2;
        double ddy = average - y;
        double dy = p[idx].y - p[idx - 1].y;
        
        switch(type){
            case wave:
                double v = Math.signum(ddy) * vy;
                vy += (swave - dampwave * v) * dt * ddy;
                break;
            
            case heat:
                double wideavg = 0;
                int width = 3;
                for(int i = -3; i <= 3; i++){
                    if(i != 0)
                        wideavg += p[clamp(idx + i, 0, p.length-1)].y;
                }
                double diff2 = wideavg / (2 * width) - y;
                vy = sheat * dt * diff2;
                break;
                
            case transport:
                vy = - dy;
                break;
            case dampedTransport:
                vy = - dy;
                y *= Math.exp(-tdamp*x);
                break;
            
        }
    }
    
    int clamp(int d, int min, int max){
        return d > min ? d < max ? d : max : min;
    }
    double clamp(double d, double min, double max){
        return d > min ? d < max ? d : max : min;
    }
    
    void move(double t){
        y += vy * t;
    }
}
