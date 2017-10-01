package pde.two;

public class Particle2D {
    
    private static final double swave = 0.1, dampwave = 0.002, sheat = 0.45;
    final double x, y;
    double z, vz;
    private final Type type;

    Particle2D(double x, double y, double vz, Type type){
        this.x = x;
        this.y = y;
        z = 0;
        this.vz = vz;
        this.type = type;
    }
    
    void mouseAt(double mx, double my, int sign){
        double dx = x - mx;
        double dy = x - my;
        double distsq = dx * dx + dy * dy + 1;
        
        if(distsq < 0)
            System.err.println("asddfqwrqgasfaqrwfa");
        
        System.out.println("sign: " + sign);
        
        switch(type){
            case heat:
                sign *= 10;
            case wave:
                vz += sign * 0.2 / distsq;
                break;
        }
    }
    
    void interact(double dt, int idx, int idy, Particle2D[][] p){
        
        double avgx = (p[idx - 1][idy].z + p[idx + 1][idy].z) / 2;
        double avgy = (p[idx][idy - 1].z + p[idx][idy + 1].z) / 2;
        double ddzx = avgx - z;
        double ddzy = avgy - z;
        
        switch(type){
            case wave:
                double vx = Math.signum(ddzx) * vz;
                double vy = Math.signum(ddzy) * vz;
                vz += (swave - dampwave * vx) * dt * ddzx;
                vz += (swave - dampwave * vy) * dt * ddzy;
                break;

            case heat:
//                double wideavg = 0;
//                int width = 3;
//                for(int i = -3; i <= 3; i++){
//                    if(i != 0)
//                        wideavg += p[clamp(idx + i, 0, p.length-1)].y;
//                }
//                double diff2 = wideavg / (2 * width) - y;
                vz = sheat * dt * (ddzx + ddzy);
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
        z += vz * t;
    }
}
