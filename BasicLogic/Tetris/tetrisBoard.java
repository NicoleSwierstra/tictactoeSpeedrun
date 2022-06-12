package BasicLogic.Tetris;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Graphics.Animation;
import Graphics.AnimationInterface;

public class tetrisBoard {
    public final int WIDTH = 10, HEIGHT = 20;
    public int ppixel;
    
    public int[][] board;
    public List<Integer> next;
    public Tetrimino current;
    public int stored = 0;
    public boolean canstore = true;
    boolean playing = true;
    public int level;
    public int lines;
    public int score;

    public tetrisBoard(){
        board = new int[HEIGHT][WIDTH];
        next = new ArrayList<Integer>();
        fillNext();
        current = pushNext();
        new Thread(()->{timingThread();}).start();
    }

    Tetrimino pushNext(){
        canstore = true;
        Tetrimino t = new Tetrimino(tetrisInfo.baseTetriminos[next.remove(0)]);
        if(next.size() < 7) fillNext();

        System.out.println("Stored: " + stored + "Next: " + next.get(0));

        return t;
    }

    void fillNext(){
        int[] nextFew = {1, 2, 3, 4, 5, 6, 7};
        Random rnd = ThreadLocalRandom.current();
        for(int i = 0; i < 7; i++){
            int i1 = rnd.nextInt(7), i2 = rnd.nextInt(7);
            int tmp = nextFew[i2];
            nextFew[i2] = nextFew[i1];
            nextFew[i1] = tmp;
        }
        for(int i = 0; i < 7; i++){
            next.add(nextFew[i]);
        }
    }

    public synchronized boolean update(){
        boolean m = MoveCurrent(0);
        if(!m) {
            drawCurrentToBoard();
            current = pushNext();
            checkLines();
            return false;
        }
        return true;
    }

    public void checkLines(){
        for(int y = 0; y < HEIGHT; y++){
            if(checkLine(y)){
                int startingline = y;
                int number = 0;
                while(checkLine(y++)) number++;
                
                int n = number;

                System.out.println("start:" + startingline + " number:" + n);
                Animation.AddAnimation((t, g)->{
                    g.setColor(new Color(1.0f, 1.0f, 1.0f, Math.round((t * 2) % 1)));
                    g.fillRect(0, (19 - startingline - (n - 1)) * ppixel, 10 * ppixel, ppixel * n);
                }, (t, g) -> {
                    for(int i = 0; i < n; i++){
                        for(int l = startingline; l < HEIGHT - 1; l++){
                            for(int x = 0; x < WIDTH; x++){
                                board[l][x] = board[l+1][x];
                            }
                        }
                    }
                }, 0.2f);

                if(n == 4) Animation.AddAnimation((t, g) -> {
                    g.setColor(Color.getHSBColor(t, 1.0f, 0.5f));
                    g.setFont(new Font("Comic Sans MS", Font.BOLD, ppixel));
                    g.drawString("TETRIS!!!", ppixel * 4, ppixel * 10);
                }, 1.0f);

                lines += number;
                break;
            }
        }
        level = lines/10;
    }

    public boolean checkLine(int line){
        for(int i = 0; i < WIDTH; i++){
            if(board[line][i] == 0) return false; 
        }
        return true;
    }

    //0 down, 1 left (x-), 2 right (x+)
    public boolean MoveCurrent(int type){
        boolean valid = true;
        pointi[] points = current.getPoints();
        int xoffset = type == 0 ? 0 : type == 1 ? -1 : 1, yoffset = type == 0 ? -1 : 0;
        for(int i = 0; i < 4; i++){
            if(oob(points[i].x + xoffset, points[i].y + yoffset)) {valid = false; break;}
            if(points[i].y + yoffset > 19) continue;
            if(board[points[i].y + yoffset][points[i].x + xoffset] != 0) valid = false;
        }
        if(valid){
            current.pos.x += xoffset;
            current.pos.y += yoffset;
        }
        return valid;
    }

    public void RotateCurrent(boolean CW){
        boolean valid = true;
        current.rotation = current.rotation + (CW ? 1 : 3);
        current.rotation %= 4;
        pointi[] points = current.getPoints();
        for(int i = 0; i < 4; i++){
            if(points[i].y > 20) continue;
            if(oob(points[i].x, points[i].y)) {valid = false; break;}
            if(points[i].y >= 20) continue;
            if(board[points[i].y][points[i].x] != 0) valid = false;
        }
        if(!valid){
            current.rotation = current.rotation + (CW ? 3 : 1);
            current.rotation %= 4;
        }
    }

    public void SlamCurrent(){
        while(update()){
            pointi[] animpoints = current.getPoints();

            Animation.AddAnimation((t, g) -> {
                for(pointi p : animpoints){
                    g.setColor(new Color(1.0f, 1.0f, 1.0f, t*t));
                    g.fillRect(p.x * ppixel, (19-p.y) * ppixel, ppixel, ppixel);
                }
            }, 0.15f);
        }
    }

    public void StoreCurrent(){
        if(!canstore) return; 
        canstore = false;
        int t = stored > 0 ? stored : pushNext().type;
        stored = current.type;
        current = new Tetrimino(tetrisInfo.baseTetriminos[t]);
    }

    void drawCurrentToBoard(){
        pointi[] points = current.getPoints();
        for(pointi p : points){
            board[p.y][p.x] = current.type;
        }
    }

    public void timingThread(){
        while(playing){
            update();
            try {
                Thread.sleep(tetrisInfo.timings[level]);
            } catch (InterruptedException e) {
            }
        }
    }

    boolean oob(int x, int y){
        return x < 0 || y < 0 || x >= WIDTH; //no height check
    }
}
