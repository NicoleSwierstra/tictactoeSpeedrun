package BasicLogic.Minesweeper;

import java.util.Arrays;
import java.util.Random;

public class Board {
    final static int EMPTY = 0;
    public final static int MINE = 9;

    //width, height, mines
    final static int[] EASY   = {  9,  9, 10 };
    final static int[] NORMAL = { 15, 13, 40 };
    final static int[] EXPERT = { 30, 25, 99 }; 
    final static int[][] GEN = {EASY, NORMAL, EXPERT};

    boolean firstMove = false;
    public int mines, width, height;
    public int[][] board;
    public boolean[][] boardCovered;
    public boolean[][] boardFlagged;
    

    public Board(int type){
        width = GEN[type][0];
        height = GEN[type][1];
        mines = GEN[type][2];

        board = new int[height][width];
        boardCovered = new boolean[height][width];
        boardFlagged = new boolean[height][width];

        generateBoard();
        print();
    }

    //places mines
    void generateBoard(){
        firstMove = false;
        Random r = new Random();
        for(int i = 0; i < mines; i++){
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            if (board[y][x] == MINE) {i--; continue;}
            board[y][x] = MINE;
            System.out.println(x + ", " + y);
        }
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if (board[y][x] == EMPTY) board[y][x] = findNum(x, y);
                boardCovered[y][x] = true;
            }
        }
    }

    int findNum(int x, int y){
        int n = 0;
        //TODO: fix this dear lord
        if(x > 0){
            n += (board[y][x-1] == MINE) ? 1 : 0;
            if(y > 0)
                n += (board[y-1][x-1] == MINE) ? 1 : 0;
            if(y < height - 1)
                n += (board[y+1][x-1] == MINE) ? 1 : 0;
        }
        if(x < width - 1){
            n += (board[y][x+1] == MINE) ? 1 : 0;
            if(y > 0)
                n += (board[y-1][x+1] == MINE) ? 1 : 0;
            if(y < height - 1)
                n += (board[y+1][x+1] == MINE) ? 1 : 0;
        }
        if(y > 0)
            n += (board[y-1][x] == MINE) ? 1 : 0;
        if(y < height - 1)
            n += (board[y+1][x] == MINE) ? 1 : 0;
        
        return n;
    }

    //returns true if not a mine
    public boolean remove(int x, int y){
        if(boardFlagged[y][x]) return true;
        propagate(x, y);
        if (board[y][x] == MINE) return false;
        else return true;
    }

    public void flag(int x, int y){
        if (!boardCovered[y][x]) return;
        boardFlagged[y][x] = !boardFlagged[y][x];
    }

    //for propagating the stuff
    void propagate(int x, int y){
        if(oob(x,y)) return;
        if(!boardCovered[y][x]) return;
        boardCovered[y][x] = false;
        if(board[y][x] == MINE || board[y][x] != EMPTY) return;
        propagate(x-1, y-1);
        propagate(  x, y-1);
        propagate(x+1, y-1);
        propagate(x-1,   y);
        propagate(x+1,   y);
        propagate(x-1, y+1);
        propagate(  x, y+1);
        propagate(x+1, y+1);
    }

    public void print(){
        for(int y = 0; y < height; y++){
            String s = getC(0, y);
            for(int x = 1; x < width; x++)
                s += "|" + getC(x, y);
            System.out.println(s);
        }
    }

    String getC(int x, int y){
        if(boardFlagged[y][x]) return "\033[32;40m" + "F" + "\033[0;0m";
        if(boardCovered[y][x]) return " ";
        if(board[y][x] == EMPTY) return "􏿾";
        return "" + board[y][x];
    }

    //if the x, y board coordinates are out of bounds
    public boolean oob(int x, int y){
        return x < 0 || y < 0 || x >= width || y >= height;
    }
}