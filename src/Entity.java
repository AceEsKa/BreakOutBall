import java.awt.*;
import java.awt.geom.*;

public abstract class Entity {
    protected double X;
    protected double Y;
    protected int Width;
    protected int Height;
    protected RectangularShape shape;
    protected Color color;
    public Entity(int x, int y, int width, int height, Color c) {
        X = x;
        Y = y;
        Width = width;
        Height = height;
        color = c;
    }

    public RectangularShape GetShape(){
        return shape;
    }
    public void Render(Graphics2D graphics2D) {
        graphics2D.setColor(color);
        graphics2D.fill(shape);
    }
    public double GetX(){
        return X;
    }
    public double GetY(){
        return Y;
    }
}
