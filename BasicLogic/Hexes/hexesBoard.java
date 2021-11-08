package BasicLogic.Hexes;

import java.util.ArrayList;
import java.util.List;

class spawnPoints{
    //{r, q} (for some reason idk)
    public static int size = 5;
    public final static int[] sizes = {5, 7, 9, 11, 13};
    private static int[][] spCoords = {{size-1, size/2}, {size-1, 0}, {size/2, 0}, {0, size/2}, {0, size-1}, {size/2, size-1}};
    private static int[][] spIndex = {
        {}, // 0 players
        {0}, // 1 player
        {0,3}, // 2 players
        {0,2,4}, // 3 players
        {0,1,3,4}, // 4 players
        {0,1,2,4,5}, // 5 players
        {0,1,2,3,4,5} // 6 players
    };

    public static int resize(int players) {
        size = sizes[players - 2];
        spCoords = new int[][]{{size-1, size/2}, {size-1, 0}, {size/2, 0}, {0, size/2}, {0, size-1}, {size/2, size-1}};
        return size;
    }

    public static int[][] getStartCoords(int players) {
        List<int[]> l = new ArrayList<int[]>();
        for(int i : spIndex[players]) l.add(spCoords[i]);
        return l.toArray(new int[l.size()][2]);
    }
}

class directions{
    //{q, r}
    public static int[][] angleCoords = {{1, 0}, {0, 1}, {1, -1}, {-1, 0}, {0, -1}, {-1, 1}};
}

public class hexesBoard {
    //see the hex axis 
    public int board[][];
    public int lastMoves[][];
    int playerNum;
    public int size;
    public int turn;
    public boolean playing[];

    public hexesBoard(int pNum){
        size = spawnPoints.resize(pNum);
        playerNum = pNum;
        reset();
    }

    public void reset(){
        turn = 0;
        playing = new boolean[playerNum];
        for(int i = 0; i < playerNum; i++) playing[i] = true;
        lastMoves = new int[playerNum][2];
        board = new int[size][size];
        for(int q = 0; q < size; q++){
            for(int r = 0; r < size; r++){
                if(q + r > (size-1) + (size/2) || r + q < (size-1) - (size/2)) 
                    board[q][r] = -1;
            }
        }

        int[][] spawns = spawnPoints.getStartCoords(playerNum);
        for(int i = 0; i < spawns.length; i++) {
            board[spawns[i][1]][spawns[i][0]] = i+1;
            lastMoves[i][1] = spawns[i][0];
            lastMoves[i][0] = spawns[i][1];
        }
    }

    //if the move is valid, does the move. else does nothing
    boolean takeTurn(int dir){
        if(dir < 0 || dir > 5) return false;
        int q = directions.angleCoords[dir][1] + lastMoves[turn][0],
            r = directions.angleCoords[dir][0] + lastMoves[turn][1];
        
        if(oob(q, r)) return false;
        if(board[q][r] != 0) return false;
        
        board[q][r] = turn + 1;
        lastMoves[turn][0] = q;
        lastMoves[turn][1] = r;
        checkAllPlayerMoves();
        advanceTurn();
        return true;
    }

    //if the move is valid, does the move. else does nothing
    public boolean takeTurn(int q, int r){
        if(!validTurn(q, r)) return false;
        board[q][r] = turn + 1;
        lastMoves[turn][0] = q;
        lastMoves[turn][1] = r;
        checkAllPlayerMoves();
        advanceTurn();
        return true;
    }

    void advanceTurn(){
        turn++;
        turn %= playerNum;
        if(!playing[turn]) advanceTurn();
    }

    public void checkAllPlayerMoves(){
        for (int i = 0; i < playerNum; i++) {
            if(!playing[i]) continue;
            playing[i] = hasOkNeighbor(lastMoves[i][0], lastMoves[i][1], i);
        }
    }

    //checks to see if the number of playing players is just 1 and if so returns that player (+1), else returns 0
    public int checkWin(){
        int winner = 0;
        int nPlayers = 0;
        for(int i = 0; i < playerNum; i++){
            if(playing[i]){
                winner = i+1;
                nPlayers++;
            }
        }
        return nPlayers == 1 ? winner : 0;
    }

    public void print(){
        for(int q = 0; q < size; q++){
            for(int i = 0; i < Math.abs(q - 5); i++){
                System.out.print("   ");
            }
            for(int r = 0; r < size; r++){
                if(board[q][r] >= 0) System.out.print("  " + board[q][r] + "    ");
            }
            System.out.println("\n");
        }
    }

    public boolean isAdjacent(int q1, int r1, int q2, int r2){
        int dq = q1 - q2;
        int dr = r1 - r2;
        if (Math.abs(dq) > 1 || Math.abs(dr) > 1 || Math.abs(dq + dr) > 1)
            return false;
        return true;
    }

    //checks validity of a turn for the current player
    public boolean validTurn(int q, int r) {
        if(!isAdjacent(lastMoves[turn][0], lastMoves[turn][1], q, r)) return false;
        if(board[q][r] == turn + 1) return false;
        for(int[] lm : lastMoves){
            if(lm[0] == q && lm[1] == r) return false;
        }
        return true;
    }

    boolean hasOkNeighbor(int q, int r, int type){
        if(oob(q, r)) return false;
        for(int i = 0; i < 6; i++){
            int dq = directions.angleCoords[i][0] + q,
                dr = directions.angleCoords[i][1] + r;
            System.out.println(type + " angle:" + i + ", (" + q + "," + r + "), (" + dq + "," + dr + ")");
            if(oob(dq, dr)) continue;
            int t = board[dq][dr] - 1;
            System.out.println("type: " + t);
            if(t == -1) return true;
            if ((dq != lastMoves[t][0] || dr != lastMoves[t][1]) && t != type) return true;
        }
        return false;
    }

    public boolean oob(int q, int r){
        if (q < 0 || r < 0 || q >= size || r >= size) return true;
        return board[q][r] == -1;
    }
}