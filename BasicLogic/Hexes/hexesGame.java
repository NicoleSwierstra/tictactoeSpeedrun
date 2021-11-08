package BasicLogic.Hexes;

import java.util.Scanner;

public class hexesGame {
    hexesBoard board;
    Scanner sc;

    public hexesGame(int players){
        board = new hexesBoard(players);
        sc = new Scanner(System.in);
    }

    public void game(){
        int winner;
        while((winner = board.checkWin()) == 0){
            //System.out.println("\033[H\033[2J"); // clears the console
            board.print();
            int dir = Integer.parseInt(sc.nextLine());
            if (!board.takeTurn(dir)) System.out.println("ERROR!!");
        }

        System.out.println("PLAYER " + winner + " WINS!!!");
    }
}
