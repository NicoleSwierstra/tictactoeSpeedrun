package BasicLogic.Minesweeper;

import BasicLogic.Input;

public class msGame {
    public msGame(int type){
        msBoard b = new msBoard(type);
        boolean playing = true;
        //game loop
        while (playing){
            int[] in = Input.getMSInput();
            if(in[2] == 1) b.flag(in[0], in[1]); else
            if(in[2] == 2) playing = b.remove(in[0], in[1]);
            System.out.println("\033[H\033[2J"); // clears the console
            b.print();
        }
    }
}
