package Graphics.Renderers;

import java.awt.*;
import BasicLogic.Hexes.*;
import java.awt.event.MouseEvent;
import java.awt.font.*;
import java.awt.geom.AffineTransform;

import Graphics.Window;
import Graphics.Window.GraphicPanel;

public class HexRenderer implements Renderer {
    hexesBoard board;
    GraphicPanel panel;

    final long _starttime = System.currentTimeMillis();
    final double sqrtof3 = Math.sqrt(3.0);
    final double sqrtof2 = Math.sqrt(2.0);
    final double sqrtof3over3 = Math.sqrt(3.0) / 3.0;
    final double sqrtof2over2 = Math.sqrt(2.0) / 2.0;

    final Color BOARDCOL = new Color(25, 25, 25);
    final Color BOARDOUT = new Color(150, 150, 150);
    final Color NONCOLOR = new Color(0, 0, 0, 0);
    final Color PLAYER1C = new Color(255, 0, 0);
    final Color PLAYER2C = new Color(0, 255, 0);
    final Color PLAYER3C = new Color(0, 0, 255);
    final Color PLAYER4C = new Color(255, 255, 0);
    final Color PLAYER5C = new Color(255, 0, 255);
    final Color PLAYER6C = new Color(255, 255, 255);
    final Color[] COLLUT = {BOARDCOL, PLAYER1C, PLAYER2C, PLAYER3C, PLAYER4C, PLAYER5C, PLAYER6C};

    int spixel;
    int winner = 0;

    public HexRenderer(hexesBoard b, GraphicPanel g){
        panel = g;
        board = b;
    }

    public void Render(Graphics g, int xmouse, int ymouse) {
        renderBoard(g);
        if(winner != 0)
            drawWinner(g, winner);
        else
            drawGhost(g, xmouse, ymouse);
    }

    void drawGhost(Graphics g, int x, int y){
        int[] mouseloc = mouseToHex(x, y);
        if(mouseloc == null) return;
        if(!board.validTurn(board.size/2 + mouseloc[1], board.size/2 + mouseloc[0])) return;
        Color old = COLLUT[board.turn + 1];
        Color col = new Color(old.getRed(), old.getGreen(), old.getBlue(), 125);
        renderHexagon((Graphics2D)g, NONCOLOR, col, board.size/2 + mouseloc[1], board.size/2 + mouseloc[0], 1.0f, false);
    }

    private void renderBoard(Graphics g){
        for(int q = 0; q < board.size; q++){
            for(int r = 0; r < board.size; r++){
                if(board.board[q][r] != -1){
                    renderHexagon((Graphics2D)g, BOARDOUT, COLLUT[board.board[q][r]], q, r, 1.0f, false);
                }
            }
        }
        for(int i = 0; i < board.lastMoves.length; i++){
            Color c = board.playing[i] ? board.turn == i ? Color.white : COLLUT[i+1].darker().darker() : Color.black;
            renderHexagon((Graphics2D)g, NONCOLOR, c, board.lastMoves[i][0], board.lastMoves[i][1], 0.5f, board.turn == i);
        }
    }

    private void renderHexagon(Graphics2D g, Color outlineC, Color inC, int q, int r, float scale, boolean rotate){
        q = q - board.size/2;
        r = r - board.size/2;

        int x = (int)((r + (q * 0.5f)) * sqrtof3 * spixel), y = (int)(q * 1.5f * spixel);

        Polygon hex = new Polygon();
        for(int i = 0; i < 6; i++){
            hex.addPoint(
                (int)(spixel * scale * Math.sin(i / 3.0f * Math.PI + (rotate ? (System.currentTimeMillis() - _starttime) / 20: 0))),
                (int)(spixel * scale * Math.cos(i / 3.0f * Math.PI + (rotate ? (System.currentTimeMillis() - _starttime) / 20: 0)))
            );
        }

        hex.translate(x + panel.panewidth/2, y + panel.paneheight/2);
        g.setColor(inC);
        g.fill(hex);
        g.setColor(outlineC);
        g.draw(hex);
    }

    void drawWinner(Graphics g, int winner){
        Font f = new Font("Comic Sans MS", 0, spixel);
        g.setFont(f);
        FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
        String s = new String("Player " + winner + " wins!!");
        Shape text =  new TextLayout(s, f, frc).getOutline(
            AffineTransform.getTranslateInstance((panel.panewidth - g.getFontMetrics().stringWidth(s))/2, panel.paneheight/2)
        );
        g.setColor(Color.white);
        ((Graphics2D)g).fill(text);
        g.setColor(Color.black);
        ((Graphics2D)g).draw(text);
    }

    public void onMouse(int button, int x, int y) {
        if (winner != 0) return;
        int[] mouseloc = mouseToHex(x, y);
        if(board.takeTurn(mouseloc[1] + board.size/2, mouseloc[0] + board.size/2)) winner = board.checkWin();
    }

    public void reset() {
        winner = 0;
        board.reset();
    }

    public void resize() {
        spixel = panel.shortest() / (2 * (board.size + 1));
    }

    int[] mouseToHex(int x, int y){
        x -= panel.panewidth/2;
        y -= panel.paneheight/2;

        int q = (int)Math.round((sqrtof3over3 * x -  1.0/3 * y) / spixel),
            r = (int)Math.round((2./3 * y) / spixel);
        if(board.oob(q + board.size/2, r + board.size/2)) return null;
        
        return new int[]{q, r};
    }
}
