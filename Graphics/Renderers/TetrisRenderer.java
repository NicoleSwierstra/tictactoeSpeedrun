package Graphics.Renderers;

import java.awt.*;

import BasicLogic.Tetris.Tetrimino;
import BasicLogic.Tetris.pointi;
import BasicLogic.Tetris.tetrisBoard;
import BasicLogic.Tetris.tetrisInfo;
import Graphics.Window.GraphicPanel;
import java.awt.event.KeyEvent;

public class TetrisRenderer implements Renderer {
    tetrisBoard board;
    GraphicPanel panel;
    int ppixel = 0;

    public TetrisRenderer(tetrisBoard b, GraphicPanel gp){
        board = b;
        panel = gp;
    }

    @Override
    public void Render(Graphics g, int xmouse, int ymouse) {
        for(int y = 0; y < board.HEIGHT; y++){
            for(int x = 0; x < board.WIDTH; x++){
                g.setColor(tetrisInfo.COLORS[board.board[19-y][x]]);
                g.fillRect(x * ppixel, y * ppixel, ppixel, ppixel);
            }
        }
        g.setColor(tetrisInfo.COLORS[board.current.type]);
        for(pointi p : board.current.getPoints()){
            g.fillRect(p.x * ppixel, (19-p.y) * ppixel, ppixel, ppixel);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, ppixel/2));
        g.drawString("HELD:", ppixel * 11, ppixel * 1);
        g.drawString("NEXT:", ppixel * 11, ppixel * 6);
        g.drawString("LINES: " + board.lines, ppixel * 11, ppixel * 18);
        g.drawString("LEVEL: " + board.level, ppixel * 11, ppixel * 19);

        //held
        if(board.stored != 0) {
            g.setColor(tetrisInfo.COLORS[board.stored]);
            for(pointi p : new Tetrimino(tetrisInfo.baseTetriminos[board.stored]).getPoints()){
                g.fillRect((p.x + 8) * ppixel, (24-p.y) * ppixel, ppixel, ppixel);
            }
        }
        //next
        for(int i = 0; i < 3; i++){
            g.setColor(tetrisInfo.COLORS[board.next.get(i)]);
            for(pointi p : new Tetrimino(tetrisInfo.baseTetriminos[board.next.get(i)]).getPoints()){
                g.fillRect((p.x + 8) * ppixel, ((28 + i * 4)-p.y) * ppixel, ppixel, ppixel);
            }
        }
    }

    @Override
    public void onMouse(int button, int x, int y) {
        
    }

    public void onKey(int key){
        switch(key){
            case KeyEvent.VK_DOWN: board.update(); break;
            case KeyEvent.VK_LEFT: board.MoveCurrent(1); break;
            case KeyEvent.VK_RIGHT: board.MoveCurrent(2); break;
            case KeyEvent.VK_UP: board.RotateCurrent(true); break;
            case KeyEvent.VK_SPACE: board.SlamCurrent();
            case KeyEvent.VK_Z: board.StoreCurrent();
        }
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resize() {
        ppixel = Math.min(panel.panewidth * 2, panel.paneheight) / 20;
        System.out.println(ppixel);
    }
    
}
