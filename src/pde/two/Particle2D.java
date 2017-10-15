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
        double distsq = Math.hypot(x - mx, y - my) + 0.001;
        
        switch(type){
            case heat:
                sign *= 20;
            case wave:
                ut += sign * 0.05 / distsq;
                break;
        }
    }
    
    static void interact(double dt, double dx, double dy, Particle2D[][] ps,
        boolean[][] mask, int fromi, int toi, int fromj, int toj){

        for(int i = fromi; i < toi; i++){
            for(int j = fromj; j < toj; j++){
                if(mask[i][j]){
                    Particle2D p = ps[i][j];
                    // Calculate the average of adjacent points in x- and y-direction
                //        double avgx = (p[Math.max(idx - 1, 0)][idy].u
                //                + p[Math.min(idx + 1, p.length - 1)][idy].u) / 2;
                //        double avgy = (p[idx][Math.max(idy - 1, 0)].u
                //                + p[idx][Math.min(idy + 1, p.length - 1)].u) / 2;

                    double avgx, avgy;

                    if(i == 0){
                //            avgx = p[1][idy].u;
                //            avgx = (p[0][idy].u + p[idx + 1][idy].u) / 2;
                        p.u = ps[1][j].u;
                        return;
                    }else if(i == ps.length - 1){
                //            avgx = p[p.length - 2][idy].u;
                //            avgx = (p[idx - 1][idy].u + p[idx][idy].u) / 2;
                        p.u = ps[i - 1][j].u;
                        return;
                    }else
                        avgx = (ps[i - 1][j].u + ps[i + 1][j].u) / 2;

                    if(j == 0){
                //            avgy = p[idx][1].u;
                //            avgy = (p[idx][0].u + p[idx][idy + 1].u) / 2;
                        p.u = ps[i][1].u;
                        return;
                    }else if(j == ps[0].length - 1){
                //            avgy = p[idx][p[0].length - 2].u;
                //            avgy = (p[idx][idy - 1].u + p[idx][idy].u) / 2;
                        p.u = ps[i][j - 1].u;
                        return;
                    }else
                        avgy = (ps[i][j - 1].u + ps[i][j + 1].u) / 2;

                    double uxx = (avgx - p.u) / dx;
                    double uyy = (avgy - p.u) / dy;

                    switch(p.type){
                        case wave:
                            // utt = ...
                            // is integrated to
                            // ut += dt * ...
                            p.ut += dt * (p.type.coeffs[0] * uxx - p.type.coeffs[1] * p.ut);
                            p.ut += dt * (p.type.coeffs[0] * uyy - p.type.coeffs[1] * p.ut);
                            break;

                        case heat:
                            p.ut = p.type.coeffs[0] * 40 * dt * (uxx + uyy);
                            break;
                    }
                }
            }
        }
    }
    
    void move(double t){
        u += ut * t;
    }
}
