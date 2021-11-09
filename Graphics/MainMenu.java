package Graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import BasicLogic.Hexes.hexesBoard;
import BasicLogic.Minesweeper.msBoard;
import BasicLogic.Tetris.tetrisBoard;
import BasicLogic.TicTacToe.tttBoard;
import Graphics.Renderers.Renderer;
import Graphics.Renderers.*;
import Graphics.Window.GraphicPanel;

public class MainMenu implements Renderer {
    int width, height, hw, hh;
    GraphicPanel panel;
    Window win;
    BufferedImage[] images;

    public MainMenu(GraphicPanel gp, Window w){
        images = new BufferedImage[8];
        try {
            images[0] = ImageIO.read(new File("res/MenuImages/TicTacToeBW.png"));
            images[1] = ImageIO.read(new File("res/MenuImages/MineSweeperBW.png"));
            images[2] = ImageIO.read(new File("res/MenuImages/HexesBW.png"));
            images[3] = ImageIO.read(new File("res/MenuImages/TetrisBW.png"));
            images[4] = ImageIO.read(new File("res/MenuImages/TicTacToe.png"));
            images[5] = ImageIO.read(new File("res/MenuImages/MineSweeper.png"));
            images[6] = ImageIO.read(new File("res/MenuImages/Hexes.png"));
            images[7] = ImageIO.read(new File("res/MenuImages/Tetris.png"));
        } catch (IOException e){}
        win = w;
        panel = gp;
        resize();
    }

    @Override
    public void Render(Graphics g, int xmouse, int ymouse) {
        int quadrant = getQuadrant(xmouse, ymouse);
        g.drawImage(getSized(images[0 + (quadrant == 1 ? 4 : 0)], hw, hh), hw, hh, null);
        g.drawImage(getSized(images[1 + (quadrant == 2 ? 4 : 0)], hw, hh),  0, hh, null);
        g.drawImage(getSized(images[2 + (quadrant == 3 ? 4 : 0)], hw, hh),  0,  0, null);
        g.drawImage(getSized(images[3 + (quadrant == 4 ? 4 : 0)], hw, hh), hw,  0, null);
    }

    @Override
    public void onMouse(int button, int x, int y) {
        switch(getQuadrant(x, y)){
            case 1: win.setScene(new TTTRenderer(new tttBoard(3), panel)); break;
            case 2: win.setScene(new MSRenderer(new msBoard(2), panel)); break;
            case 3: win.setScene(new HexRenderer(new hexesBoard(2), panel)); break;
            case 4: win.setScene(new TetrisRenderer(new tetrisBoard(), panel)); break;
        }
    }

    @Override
    public void reset() {}

    @Override
    public void resize() {
        width = panel.panewidth;
        height = panel.paneheight;
        hw = width/2;
        hh = height/2;
    }
    
    int getQuadrant(int x, int y){
        int xx = x - (width/2), yy = y - (height/2);
        if(xx >= 0 && yy >= 0) return 1;
        if(xx <= 0 && yy >= 0) return 2;
        if(xx <= 0 && yy <= 0) return 3;
        return 4;
    }

    //  _________________
    // |    |      |    |
    // |    |      |    |
    // |    |      |    |
    // |____|______|____|

    // source aspect - new aspect * width

    // 6 by 4
    // 4 by 4
    // sa = 1.5, na = 1
    // 0.25

    BufferedImage getSized(BufferedImage source, int width, int height){
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int sw = source.getWidth(), sh = source.getHeight();
        float saspect = (float)sw/(float)sh;
        float daspect = (float)width/(float)height;
        int sx1 = (saspect < daspect) ? 0 : (int)Math.round(((((float)height/sh) * sw) - width) * 0.5f),
            sx2 = sw - sx1,
            sy1 = (saspect > daspect) ? 0 : (int)Math.round(((((float)width/sw) * sh) - height) * 0.5f),
            sy2 = sh - sy1;
        bi.getGraphics().drawImage(source, 0, 0, width, height, sx1, sy1, sx2, sy2, null);
        //System.out.println(sx1 + ", " + sy1 + ", " + sx2 + ", " + sy2);
        return bi;
    }
}
