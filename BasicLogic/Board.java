package BasicLogic;
import java.util.*;

public class Board{
    // y, x
    public int[][] board;
    public int size;

    public Board(int size){
        this.size = size;
        board = new int[size][size];
    }

    void clearBoard(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = 0;
            }
        }
    }

    //returns false if invalid
    public boolean takeTurn(int x, int y, int type){
        if(board[y][x] != 0) return false;
        board[y][x] = type;
        return true;
    }

    public int checkWin(){
        if (winType(1)) return 1;
        else if (winType(2)) return 2;
        return 0;
    }

    //god i hope this works
    boolean winType(int type){
        //rows
        for(int i = 0; i < size; i++){
            int rows = 0;
            for(int j = 0; j < size; j++){
                rows += (board[i][j] == type)?1:0;
            }
            if(rows == size) return true;
        }
        //columns
        for(int i = 0; i < size; i++){
            int columns = 0;
            for(int j = 0; j < size; j++){
                columns += (board[j][i] == type)?1:0;
            }
            if(columns == size) return true;
        }
        //diagonal 1
        int d1 = 0;
        for(int i = 0; i < size; i++){
            d1 += (board[i][i] == type)?1:0;
        }
        if (d1==size) return true;
        //diagonal 2
        int d2 = 0;
        for(int i = 0; i < size; i++){
            d2 += (board[i][size-i-1] == type)?1:0;
        }
        if (d2==size) return true;
        return false;
    }

    void printBoard(){
        for(int y = 0; y < size; y++){
            String s = ""+getC(board[y][0]);
            for(int x = 1; x < size; x++)
                s += "|" + getC(board[y][x]);
            System.out.println(s);
        }
    }

    char getC(int i){
        switch(i){
            case 0: return ' ';
            case 1: return 'X';
            case 2: return 'Y';
        }
        return ' ';
    }
}