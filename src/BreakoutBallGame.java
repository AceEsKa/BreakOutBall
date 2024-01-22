import javax.swing.*;

public class BreakoutBallGame {
    public static void  main(String[] args) {
        int Width = 800;
        int Height = 820;
        int BrickRaws = 3;

        JFrame MainFrame = new JFrame( );

        GamePanel c = new GamePanel(Width, Height,BrickRaws,BrickRaws*6,3);

        MainFrame.setSize( Width, Height );
        MainFrame.setTitle( "Breakout Ball Game" );
        MainFrame.setResizable(false);
        MainFrame.add(c);
        MainFrame.addKeyListener(c);
        MainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        MainFrame.setVisible( true );
    }
}

