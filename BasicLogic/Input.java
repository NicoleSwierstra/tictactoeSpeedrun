package BasicLogic;
import java.util.Scanner;

public class Input {
    static Scanner sc = new Scanner(System.in);
    
    static int[] getInput(int turn, int size){
        System.out.println("IT IS NOW PLAYER " + turn + "'S TURN. INPUT GRID SPACE TO MOVE");
        int[] coords = readIn();
        while(coords[0] >= size || coords[1] >= size || coords[0] < 0 || coords[1] < 0){
            System.out.println("NOT A VALID INPUT. TRY AGAIN");
            coords = readIn();
        }
        return coords;
    }

    static int[] readIn(){
        String toparse = sc.nextLine();
        int[] coords = new int[2];
        coords[0] = toparse.charAt(0) - 'a';
        coords[1] = Integer.parseInt(toparse.substring(1)) - 1;
        return coords;
    }
}
