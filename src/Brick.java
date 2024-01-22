import java.awt.geom.Rectangle2D;
import java.awt.*;

public class Brick extends Entity {
    protected Boolean Hit = false;

    Brick(int x, int y, int width ,int height, Color c) {
        super( x, y, width, height, c );
        shape = new Rectangle2D.Double(X,Y,Width,Height);
    }

    public void setHitStatus(boolean hit){
        Hit = hit;
    }

    public boolean getHitStatus(){
        return Hit;
    }
}
