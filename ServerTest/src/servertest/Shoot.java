package servertest;

public class Shoot {
    public double x;
    public double y;
    
    public double vx;
    public double vy;
    
    public int time;
    
    public double shootSpeed=10;

    public Shoot(double x, double y, double targetX, double targetY) {
        this.x = x;
        this.y = y;
        
        double a = Math.sqrt(Math.pow((targetX-x),2) + Math.pow((targetY-y),2));
      
        
        vx=((targetX-x)/a)*shootSpeed;
        vy=((targetY-y)/a)*shootSpeed;
 
        
        time =0;
    }
    
    
}
