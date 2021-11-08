package Graphics.Renderers;

import java.awt.*;
import java.awt.event.*;

import BasicLogic.Minesweeper.msBoard;
import Graphics.Window.GraphicPanel;

public class MSRenderer implements Renderer{
    final static Color cole = new Color(  0,   0,   0);
    final static Color col1 = new Color(  0,   0, 255);
    final static Color col2 = new Color(  0, 125,   0);
    final static Color col3 = new Color(255,   0,   0);
    final static Color col4 = new Color(  0,   0, 125);
    final static Color col5 = new Color(127,   0,   0);
    final static Color col6 = new Color(  0, 127, 127);
    final static Color col7 = new Color(255, 255, 255);
    final static Color col8 = new Color(127, 127, 127);
    final static Color[] colNum = {cole, col1, col2, col3, col4, col5, col6, col7, col8};

    int ppixel=0;
    boolean won = false, lost = false;
    final static float headerHeight = 0.1f;

    msBoard board;
    GraphicPanel panel;

    public MSRenderer(msBoard b, GraphicPanel gp){
        board = b;
        panel = gp;
        resize();
    }

    public void reset(){
        won = false; lost = false;
        board.reset();
    }

    int getHeaderHeight(){
        return (int)(headerHeight * panel.paneheight);
    }
    
    void renderHeader(Graphics g, int hHeight){
        g.setColor(new Color(0.1f, 0.1f, 0.1f));
        g.fillRect(0, 0, panel.panewidth, hHeight);
        Font font = new Font("Comic Sans MS", Font.BOLD, (int)(hHeight * 0.8f));
        g.setFont(font);
        g.setColor(Color.gray);
        FontMetrics metrics = g.getFontMetrics(font);
        g.drawString("Mines: " + (board.mines - board.flags), 20, metrics.getAscent());
        g.drawString(won ? "YOU WIN!" : (lost ? "YOU LOSE!" : ""), hHeight * 5, metrics.getAscent());
    }

    public void Render(Graphics g, int mousex, int mousey){
        renderHeader(g, getHeaderHeight());
        g.translate(0, getHeaderHeight());
        g.setColor(Color.white);
        drawBoard(g);
        for(int y = 0; y < board.height; y++){
            for(int x = 0; x < board.width; x++){
                drawCell(g, x, y);
            }
        }
        if(!won && !lost && !board.oob(mousex/ppixel, mousey/ppixel)) drawGhost(g, mousex, mousey);
    }

    void drawCell(Graphics g, int x, int y){
        Font font = new Font("Comic Sans MS", Font.BOLD, ppixel);
        g.setFont(font);
        int halfx = x * ppixel + ppixel/4, halfy = y * ppixel + ppixel/4;
        if(board.boardCovered[y][x]){
            g.setColor(Color.GRAY);
            g.fillRect(x*ppixel, y*ppixel, ppixel, ppixel);
            if(board.boardFlagged[y][x])
                g.setColor(Color.GREEN);
                g.fillRect(halfx, halfy, ppixel/2, ppixel/2);
        }   
        else if(board.board[y][x] == msBoard.MINE){
            g.setColor(Color.RED);
            g.fillOval(halfx, halfy, ppixel/2, ppixel/2);
        }
        else if(board.board[y][x] > 0){
            g.setColor(colNum[board.board[y][x]]);
            String label = ""+board.board[y][x];
            FontMetrics metrics = g.getFontMetrics(font);
            int xloc = x * ppixel + (ppixel - metrics.stringWidth(label)) / 2,
                yloc = y * ppixel + ((ppixel - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(label, xloc, yloc);
        }
        return;
    }

    void drawBoard(Graphics g){
        for(int x = 1; x < board.width; x++)
            g.fillRect((x*ppixel) - 2, 0, 4, ppixel * board.height);
        for(int y = 1; y < board.height; y++)
            g.fillRect(0, (y*ppixel) - 2, ppixel * board.width, 4);
    }

    void drawGhost(Graphics g, int x, int y){
        g.setColor(new Color(255, 0, 0, 100));
        int xx = x / ppixel, yy = (y - getHeaderHeight()) / ppixel;
        g.fillRect(xx*ppixel, yy*ppixel, ppixel, ppixel);
    }

    public void resize(){
        ppixel = Math.min(panel.panewidth/board.width, panel.paneheight/board.height);
    }

    public void onMouse(int click, int x, int y){
        int xx = x / ppixel, yy = (y - getHeaderHeight()) / ppixel;
        if(won || lost || board.oob(xx, yy)) return;
        if(click == MouseEvent.BUTTON1)
            lost = !board.remove(xx, yy);
        else
            board.flag(xx, yy);
        if(board.checkWin()) won = true;
    }
}
