import java.awt.geom.Rectangle2D;
import java.awt.*;

public class Platform extends Entity{
    public Platform(int x, int y, int width, int height, Color c) {
        super(x, y, width, height, c);
        shape = new Rectangle2D.Double(X,Y,Width,Height);
    }
    public void Move(int w, int direction,int speed){
        double difference;

        if(X+(speed)*direction > w) {
            difference = X+speed-w;
            X+=speed-difference;
        } else if(X+(speed)*direction < 0) {
            X = 0;
        } else {
            X+=speed*direction;
        }
        shape.setFrame(X,Y,Width,Height);
    }
}
