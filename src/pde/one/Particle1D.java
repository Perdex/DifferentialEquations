package pde.one;

import pde.Type;

public class Particle1D {
    
    double u, ut;
    double blurmax, blurmin;
    final int x;
    private final Type type;

    Particle1D(int x, double vy, Type type){
        this.x = x;
        this.u = 0;
        this.blurmax = 0;
        this.blurmin = 0;
        this.ut = vy;
        this.type = type;
    }
    
    //Will not be called for transport equation
    void mouseAt(int mx, int my, int mxprev, int myprev){
        int xhigh = Math.max(mx, mxprev);
        int xlow = Math.min(mx, mxprev);
        
        if(x >= xlow && x <= xhigh){
            ut = 0;
            u = my;
        }
    }
    
    void interact(double dt, double dx, int idx, Particle1D[] p){
        
        double ux = (p[idx].u - p[idx - 1].u) / dx;
        
        //Take average of previous and next positions
        double average = (p[idx - 1].u + p[idx + 1].u) / 2;
        //Subtract this position to get approximate concavity
        double uxx = (average - u) / dx;
        
        switch(type){
            case heat:
                ut = type.coeffs[0] * uxx;
                break;
            case wave:
                // utt = ...
                // is integrated to
                // ut += dt * ...
                ut += dt * (type.coeffs[0] * uxx - type.coeffs[1] * ut);
                break;
            case transport:
                ut = -type.coeffs[0] * ux - type.coeffs[1] * u;
                break;
        }
    }
    
    void move(double dt){
        u += ut * dt;
        blurmax = Math.max(u, blurmax);
        blurmin = Math.min(u, blurmin);
    }
    double blurMin(){
        double prev = blurmin;
        blurmin = u;
        return prev;
    }
    double blurMax(){
        double prev = blurmax;
        blurmax = u;
        return prev;
    }
}
