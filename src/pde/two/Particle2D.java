package pde.two;

import pde.Type;

public class Particle2D {
    
    final double x, y;
    //The index of this in the array
    final int idx, idy;
    double u, ut;
    private final Type type;

    Particle2D(double x, double y, int idx, int idy, double ut, Type type){
        this.x = x;
        this.y = y;
        this.idx = idx;
        this.idy = idy;
        this.u = 0;
        this.ut = ut;
        this.type = type;
    }
    
    void reset(){
        u = 0;
        ut = 0;
    }
    
    void mouseAt(double mx, double my, int sign){
        double distsq = Math.hypot(x - mx, y - my) + 0.005;
        
        switch(type){
            case heat:
                sign *= 100;
            case wave:
                ut += sign * 0.05 / distsq;
                break;
        }
    }
    
    void interact(double dt, double dx, double dy, Particle2D[][] p){
        
        // Calculate the average of adjacent points in x- and y-direction
//        double avgx = (p[Math.max(idx - 1, 0)][idy].u
//                + p[Math.min(idx + 1, p.length - 1)][idy].u) / 2;
//        double avgy = (p[idx][Math.max(idy - 1, 0)].u
//                + p[idx][Math.min(idy + 1, p.length - 1)].u) / 2;
        
        double avgx, avgy;
        
        if(idx == 0)
//            avgx = p[1][idy].u;
            avgx = (p[0][idy].u + p[idx + 1][idy].u) / 2;
        else if(idx == p.length - 1)
//            avgx = p[p.length - 2][idy].u;
            avgx = (p[idx - 1][idy].u + p[idx][idy].u) / 2;
        else
            avgx = (p[idx - 1][idy].u + p[idx + 1][idy].u) / 2;

        if(idy == 0)
//            avgy = p[idx][1].u;
            avgy = (p[idx][0].u + p[idx][idy + 1].u) / 2;
        else if(idy == p[0].length - 1)
//            avgy = p[idx][p[0].length - 2].u;
            avgy = (p[idx][idy - 1].u + p[idx][idy].u) / 2;
        else
            avgy = (p[idx][idy - 1].u + p[idx][idy + 1].u) / 2;
        
        double uxx = (avgx - u) / dx;
        double uyy = (avgy - u) / dy;
        
        switch(type){
            case wave:
                // utt = ...
                // is integrated to
                // ut += dt * ...
                ut += dt * (type.coeffs[0] * uxx - type.coeffs[1] * ut);
                ut += dt * (type.coeffs[0] * uyy - type.coeffs[1] * ut);
                break;

            case heat:
                ut = type.coeffs[0] * 40 * dt * (uxx + uyy);
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
        u += ut * t;
    }
}
