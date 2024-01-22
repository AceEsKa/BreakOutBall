import java.awt.*;
import java.awt.geom.*;

public class Ball extends Entity{
    protected RectangularShape hitBox;
    protected double VelocityX = 0;
    protected double VelocityY = 1;
    protected double Speed = 6;
    protected double RightCornerX;
    protected double TopCornerY;
    protected double LeftCornerX;
    protected double BottomCornerY;
    protected boolean Collision = false;
    int increment =0;
    public Ball(int x, int y, int width, int height, Color c) {
        super(x, y, width, height, c);
        shape = new Ellipse2D.Double( X, Y, Width, Height );
        hitBox = new Ellipse2D.Double(X+ Speed *VelocityX, Y+ Speed *VelocityY, Width, Height);
        DeterMineCorners();
    }
    public void BoundariesCollision() {
        if(hitBox.getY() <= 0 ) {
            Y = 0;
            X += Speed*VelocityX;
            VelocityY = 1;
            Collision = true;
        } else if(hitBox.getX() <= 0) {
            X = 0;
            Y += Speed * VelocityY;
            VelocityX = 1;
            Collision = true;
        } else if (hitBox.getX()+Width >= 782) {
            X = 782-Width;
            Y += Speed * VelocityY;
            VelocityX = -1;
            Collision = true;
        }
    }
    public void DeterMineCorners(){ //skontrolovat usage
        RightCornerX = X+Width;
        TopCornerY = Y;
        LeftCornerX = X;
        BottomCornerY = Y + Height;
    }
    public void setVelocity(double velocityX, double velocityY) {
            VelocityX = velocityX;
            VelocityY = velocityY;
    }
    public void BallPhysics(RectangularShape shape){
            ++increment;
            Collision = true;
            DeterMineCorners();
            double x1, x2, y1, y2;
            x1 = shape.getX();
            y1 = shape.getY();
            y2 = shape.getY() + shape.getHeight();
            x2 = x1 + shape.getWidth();

            if (numberOfContactPoints(shape) > 1) {
                if(increment == 2){
                    System.out.print(" tu ");
                }
                switch (determineIntersectingPoints(shape)) {
                    case "bc":
                        Y += (Speed-(y2-hitBox.getY()))*VelocityY;
                        X += Speed * VelocityX;
                        VelocityY *= -1;
                        break;
                    case "da":
                        Y += (Speed-(hitBox.getY()+hitBox.getHeight()-y1))*VelocityY;
                        X += Speed * VelocityX;
                        VelocityY *= -1;
                        break;
                    case "ba":
                        X += (Speed-(x2 - hitBox.getX()))*VelocityX;
                        Y += Speed * VelocityY;
                        VelocityX *= -1;
                        break;
                    case "cd":
                        X += (Speed- (hitBox.getX()+hitBox.getWidth()-x1))*VelocityX;
                        Y += Speed * VelocityY;
                        VelocityX *= -1;
                        break;
                }
            } else {
                double x;
                double y;
                switch (determineIntersectingPoints(shape)) {
                    case "a":
                        Point2D.Double C = new Point2D.Double(shape.getX() + shape.getWidth(), shape.getY());
                        x = C.getX() - hitBox.getX();
                        y = hitBox.getY() + hitBox.getHeight() - C.getY();
                        DetermineTrajectory(x, y, y >= x, 'a');
                        break;
                    case "b":
                        Point2D.Double D = new Point2D.Double(shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight());
                        x = D.getX() - hitBox.getX();
                        y = D.getY() - hitBox.getY();
                        if(increment == 2){
                            System.out.print(" B ");
                        }
                        DetermineTrajectory(x, y, y >= x, 'b');
                        break;
                    case "c":
                        Point2D.Double A = new Point2D.Double(shape.getX(), shape.getY() + shape.getHeight());
                        x = hitBox.getX() + hitBox.getWidth() - A.getX();
                        y = A.getY() - hitBox.getY();
                        DetermineTrajectory(x, y, y >= x, 'c');
                        break;
                    case "d":
                        Point2D.Double B = new Point2D.Double(shape.getX(), shape.getY());
                        x = hitBox.getX() + hitBox.getWidth() - B.getX();
                        y = hitBox.getY() + hitBox.getHeight() - B.getY();
                        DetermineTrajectory(x, y, y >= x, 'd');
                        break;
                }
            }
    }
    protected void DetermineTrajectory(double x, double y, boolean greaterY, char corner){
      if(VelocityX > 0 && VelocityY > 0){
            //BottomRight
          ChangeTrajectory(!greaterY || corner == 'c',x,y);
        }else if(VelocityX > 0 &&  VelocityY< 0){
            //UpRight

          ChangeTrajectory(!greaterY || corner == 'd',x,y);
        }else if(VelocityX < 0 && VelocityY > 0){
            //BottomLeft
          ChangeTrajectory(!greaterY || corner == 'a',x,y);
        }else if(VelocityX < 0 && VelocityY < 0) {
            //UpLeft
          ChangeTrajectory(!greaterY || corner == 'b',x,y);
        }
    }
    protected void ChangeTrajectory(boolean b,double x,double y){
        if (b){
            //hit from side
            X += (Speed - x)*VelocityX;
            Y += Speed *VelocityY;
            VelocityX *=-1;
        } else {
            //hit from top/bottom
            X += Speed *VelocityX;
            Y += (Speed - y)*VelocityY;
            VelocityY *=-1;
        }
    }
    protected int numberOfContactPoints(RectangularShape shape){
        int pointsOfContact = 0;

        //B x,y
        if(shape.contains(hitBox.getX(), hitBox.getY())){
            ++pointsOfContact;
        }
        //C x+width,y
        if(shape.contains(hitBox.getX() + hitBox.getWidth(), hitBox.getY())){
            ++pointsOfContact;
        }
        //D x+width,y+height
        if(shape.contains(hitBox.getX() + hitBox.getWidth(), hitBox.getY() + hitBox.getHeight())){
            ++pointsOfContact;
        }
        //A x,y+height
        if(shape.contains(hitBox.getX(), hitBox.getY() + hitBox.getHeight())){
            ++pointsOfContact;
        }
        return pointsOfContact;
    }
    protected String determineIntersectingPoints(RectangularShape shape){
        String contactPoints = "";
        //B x,y
        if(shape.contains(hitBox.getX(), hitBox.getY())){
            contactPoints += 'b';
        }
        //C x+width,y
        if(shape.contains(hitBox.getX()+ hitBox.getWidth(), hitBox.getY())){
            contactPoints += 'c';
        }
        //D x+width,y+height
        if(shape.contains(hitBox.getX()+  hitBox.getWidth(), hitBox.getY() + hitBox.getHeight())){
            contactPoints += 'd';
        }
        //A x,y+height
        if(shape.contains(hitBox.getX(), hitBox.getY() + hitBox.getHeight())) {
            contactPoints += 'a';
        }
        return contactPoints;
    }
    public void Move(){
        //dat do ifu
        if(!Collision){
            X += Speed *VelocityX;
            Y += Speed *VelocityY;
        }

        shape.setFrame(X,Y,Width,Height);
        double x2 = X+ Speed *VelocityX;
        double y2 = Y+ Speed *VelocityY;
        hitBox.setFrame(x2,y2,Width,Height);
        BoundariesCollision();
        Collision = false;
    }
    public double getSpeed(){
        return Speed;
    }
    public void IncreaseSpeed(double speedIncrease){
        Speed += speedIncrease;
    }
    public RectangularShape getHitBox(){
        return hitBox;
    }
}
