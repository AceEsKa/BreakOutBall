import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RectangularShape;

import static java.awt.event.KeyEvent.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    protected int Width;
    protected int Height;
    protected int BrickRaws;
    protected Boundary LeftB;
    protected Boundary RightB;
    protected Boundary TopB;
    protected int PlatformSpeed = 13;
    protected Platform platform;
    protected Brick[] bricks; //make thread safe
    protected int BrickCount;
    protected int Score;
    protected Timer timer;
    protected boolean FirstHit = false;
    protected int BricksLeft;
    protected int Lives;
    protected Ball ball;
    protected boolean BallDrop = false;
    protected int x = 70;
    protected int y = 50;
    protected Graphics2D g2d;
    public GamePanel(int w, int h, int br, int bc, int lives ){
        Width = w-136;
        Height = h;
        BrickRaws = br;
        BrickCount = BricksLeft = bc;
        Lives = lives;
        bricks = new Brick[BrickCount];
        Score = 0;

        this.setPreferredSize(new Dimension(800,820));
        this.setBackground(Color.black);

        timer = new Timer(20,this);
        timer.start();

        MakeObjects();
    }
    protected void MakeObjects(){
        MakeBricks();
        MakeBoundaries();
        MakePlatform();
        MakeBall();
    }
    protected void MakeBricks(){
        int BricksInRaw = BrickCount/BrickRaws;
        int iterator = 0;
        int x1 = x;
        int y1 = y;

        for(int i = 0; i < BrickRaws; i++) {
            for(int j = 0; j < BricksInRaw; j++){
                bricks[iterator]=  new Brick(x1,y1, 100,50,Color.WHITE);
                iterator++;
                x1+= 100 + 5;
            }
            x1=x;
            y1 += 50 + 30;
        }
    }
    protected void MakeBoundaries(){
        LeftB = new Boundary(0,0,2,Height, Color.yellow);
        TopB = new Boundary(0,0,Width+136,5,Color.yellow);
        RightB = new Boundary(Width+118,0,2,Height,Color.yellow);
    }
    protected void MakePlatform(){
        platform = new Platform(340,755,120,10, Color.GREEN);
    }
    protected void MakeBall(){
        ball = new Ball(400 , 300,25,25,Color.yellow);
    }

    @Override
    protected  void paintComponent(Graphics g){
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        TopB.Render(g2d);
        LeftB.Render(g2d);
        RightB.Render(g2d);

        platform.Render(g2d);

        for(int i = 0; i < BrickCount; ++i ) {
            if(bricks[i] != null) {
                bricks[i].Render(g2d);
                if(bricks[i].getHitStatus()){
                    bricks[i] = null;
                    --BricksLeft;
                    Score+=25;
                }
            }
        }

        g2d.drawString(String.valueOf("Lives left: "), 330, 20);
        g2d.drawString(String.valueOf(Lives), 400, 20);
        if(BallDrop) {
            ball.Render(g2d);
            if(CollisionDetection() == 1){
                --Lives;
                if( Lives == 0 ) {
                    System.exit(1);
                }
            }else if(Score == 25*BrickCount) {
                System.exit(0);
            }
        }
        g2d.drawString(String.valueOf(Score), 700, 20);
    }

    protected int CollisionDetection(){

        if(ball.GetX() >= 50 && ball.GetX() <= 580 || y < 280){
            for(Brick b : bricks){

                if(b != null && Intersects(b.GetShape()) && !b.getHitStatus()){
                    ball.BallPhysics(b.GetShape());
                    b.setHitStatus(true);
                    return 0;
                }
            }
        }

        if(ball.GetY() >= 700 && (Intersects(platform.GetShape()))){
            int min = -1;
            int max = 1;
            int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
            if(FirstHit){
                FirstHit = false;
                ball.setVelocity(random_int,-1);
            }

            if(BricksLeft <= BrickCount-9){
                ball.setVelocity(random_int,-1);
            }
            else{
                ball.setVelocity(ball.getSpeed()*-1,-1);
            }
        }else if(ball.GetY() > 800){
            return 1;
        }
        return 0;
    }
    protected boolean Intersects(RectangularShape shape){
        return shape.intersects(ball.getHitBox().getFrame());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(BallDrop){
            ball.Move();
        }
        repaint();
    }

    @Override
    public void keyTyped (KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case VK_LEFT:
                if(BallDrop) {
                    platform.Move(Width, -1, PlatformSpeed);
                }
                break;
            case VK_RIGHT:
                if(BallDrop) {
                    platform.Move(Width, 1, PlatformSpeed);
                }
                break;
            case VK_SPACE:
                //drop ball
                if(!BallDrop) {
                    BallDrop = true;
                    ball.Render(g2d);
                }
                break;
        }
        CollisionDetection();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CollisionDetection();
    }
}
