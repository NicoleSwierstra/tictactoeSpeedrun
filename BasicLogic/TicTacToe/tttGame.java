package BasicLogic.TicTacToe;

import BasicLogic.Input;

public class tttGame {
    boolean turn;
    tttBoard board;

    public tttGame(int size){
        board = new tttBoard(size);
        game();
    }

    void game(){
        board.clearBoard();
        
        while(board.checkWin() == 0){
            board.printBoard();
            int[] coords = Input.getTTTInput(turn ? 2 : 1, board.size);
            while(board.takeTurn(coords[0], coords[1], turn ? 2 : 1) != true){
                System.out.println("INVALID MOVE! TRY AGAIN!");
                coords = Input.getTTTInput(turn ? 2 : 1, board.size);
            }
            turn = !turn;
        }

        System.out.println("PLAYER " + board.checkWin() + " WINS!!!");
    }
}
