import java.util.Scanner;

import Graphics.Window;

public class GameBoot {
    static void boot(){
        int type = 0;
        int arg = 0;
        // 1 = console ttt
        // 2 = console ms
        // 3 = gui ttt
        // 4 = gui ms
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Please enter the game you want to play console/gui ttt/ms size/difficulty");
            String in = sc.nextLine();
            if(in.contains("console")){
                if(in.contains("ttt")){
                    type = 1;
                }
                else if (in.contains("ms")){
                    type = 2;
                }
            }
            else if (in.contains("gui")){
                if(in.contains("ttt")){
                    type = 3;
                }
                else if (in.contains("ms")){
                    type = 4;
                }
            }
            arg = Integer.parseInt(in.split(" ")[2]);
        } while(type == 0);
        sc.close();
        switch(type){
            case 1: new BasicLogic.TicTacToe.tttGame(arg); break;
            case 2: new BasicLogic.Minesweeper.msGame(arg); break;
            case 3: new Window(1, arg); break;
            case 4: new Window(2, arg); break;
        }
    }
}
