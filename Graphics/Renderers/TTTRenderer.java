package Graphics.Renderers;

import Graphics.Window.GraphicPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.*;
import java.awt.geom.AffineTransform;

import BasicLogic.TicTacToe.tttBoard;

public class TTTRenderer implements Renderer{
    GraphicPanel panel;
    tttBoard board;
    int ppixel;
    boolean turn = false;
    int winner = 0;

    final static Color xcol = new Color(94, 204, 147);
    final static Color ocol = new Color(195, 86, 209);

    public TTTRenderer(tttBoard b, GraphicPanel gp){
        panel = gp;
        board = b;
        ppixel = panel.shortest()/board.size;
    }

    public void Render(Graphics g, int xmouse, int ymouse){
        g.setColor(Color.white);
        drawBoard(g);
        for(int x = 0; x < board.size; x++){
            for(int y = 0; y < board.size; y++){
                drawChar(g, board.board[y][x], x, y, board.board[y][x] == 1 ? xcol : ocol);
            }
        }
        if(winner != 0){
            drawWinner(g, winner);
        } 
        else if (board != null) 
            drawGhost(g, xmouse/ppixel, ymouse/ppixel);
    }

    void drawGhost(Graphics g, int x, int y){
        if(oob(x, y)) return;
        g.setColor(Color.gray);
        if(board.board[y][x] == 0) drawChar(g, turn?2:1, x, y, Color.gray);
        g.setColor(Color.white);
    }

    void drawBoard(Graphics g){
        for(int x = 1; x < board.size; x++){
            g.fillRect((x*ppixel) - 5, 0, 10, panel.shortest());
        }
        for(int y = 1; y < board.size; y++){
            g.fillRect(0, (y*ppixel) - 5, panel.shortest(), 10);
        }
    }

    void drawChar(Graphics g, int type, int x, int y, Color c){
        if(type == 0 || oob(x,y)) return;
        ((Graphics2D)g).setStroke(new BasicStroke(2));
        g.setColor(c);
        int offset = ppixel/5;
        int xmin = ppixel * x + offset, ymin = ppixel * y + offset,
            xmax = ppixel * x + (offset * 4), ymax = ppixel * y + (offset * 4);

        if(type == 1){
            for(int i = 24; i >= 0; i -= 3){
                Color col = g.getColor();
                g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 255 - i * 10));
                g.drawLine(xmin + i/2, ymin + i, xmax + i/2, ymax + i);
                g.drawLine(xmax + i/2, ymin + i, xmin + i/2, ymax + i);
            }
        } else {
            for(int i = 24; i >= 0; i -= 3){
                Color col = g.getColor();
                g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 255 - i * 10));
                g.drawOval(xmin + i/2, ymin + i, offset * 3, offset * 3);
            }
        }
    }

    void drawWinner(Graphics g, int winner){
        Font f = new Font("Comic Sans MS", 0, ppixel/4);
        FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
        String s = new String("Player " + winner + " wins!!");
        Shape shape =  new TextLayout(s, f, frc).getOutline(AffineTransform.getTranslateInstance((ppixel/3) * 2, panel.shortest()/2));
        g.setColor(Color.white);
        ((Graphics2D)g).fill(shape);
        g.setColor(Color.black);
        ((Graphics2D)g).draw(shape);
    }

    public void resize(){
        ppixel = panel.shortest()/board.size;
    }

    public void onMouse(int button, int x, int y){
        if(button != MouseEvent.BUTTON1) return;
        int xx = x / ppixel, yy = y / ppixel;
        if(winner != 0 || oob(xx, yy)) return;
        if(board.takeTurn(xx, yy, turn?2:1)) turn = !turn;
        checkWin();
    }

    void checkWin(){
        winner = board.checkWin();
    }

    public void reset(){
        turn = false;
        winner = 0;
        board.clearBoard();
    }

    //if the x, y board coordinates are out of bounds
    boolean oob(int x, int y){
        return x < 0 || y < 0 || x >= board.size || y >= board.size;
    }
}