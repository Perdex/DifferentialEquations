package pde.one;

import pde.Type;

public class Particle1D {
    
    double u, ut;
    final int x;
    private final Type type;

    Particle1D(int x, double vy, Type type){
        this.x = x;
        this.u = 0;
        this.ut = vy;
        this.type = type;
    }
    
    void mouseAt(int mx, int my, int mxprev, int myprev){
        
        int xhigh = Math.max(mx, mxprev);
        int xlow = Math.min(mx, mxprev);
        
        switch(type){
            case wave:
            case heat:
            case combination:
                if(x >= xlow && x <= xhigh){
                    ut = 0;
                    u = my;
                }
                break;
            case transport:
                //The options for x = 1 and x = 2 smoothen the curve
                if(x == 0)
                    u = my;
                if(x == 1 && Math.abs(u) < Math.abs(my * 0.8)){
                    u *= 0.5;
                    u += 0.4 * my;
                }
                if(x == 2 && Math.abs(u) < Math.abs(my * 0.5)){
                    u *= 0.5;
                    u += 0.25 * my;
                }
                break;
        }
    }
    
    void interact(double dt, double dx, int idx, Particle1D[] p){
        
        double ux = (p[idx].u - p[idx - 1].u) / dx;
        
        //Take average of previous and next positions
        double average = (p[idx - 1].u + p[idx + 1].u) / 2;
        //Subtract this position to get approximate concavity
        double uxx = (average - u) / dx;
        
        if(type == Type.heat || type == Type.combination)
            ut = type.coeffs[0] * uxx;
        
        if(type == Type.wave || type == Type.combination){
            // utt = ...
            // is integrated to
            // ut += dt * ...
            ut += dt * (type.coeffs[0] * uxx - type.coeffs[1] * ut);
        }
        
        if(type == Type.transport)
            ut = -type.coeffs[0] * ux - type.coeffs[1] * u;
        
    }
    
    int clamp(int d, int min, int max){
        return d > min ? d < max ? d : max : min;
    }
    double clamp(double d, double min, double max){
        return d > min ? d < max ? d : max : min;
    }
    
    void move(double dt){
        u += ut * dt;
    }
}
