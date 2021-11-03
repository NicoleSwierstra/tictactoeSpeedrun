package BasicLogic;
import java.util.Scanner;

public class Input {
    static Scanner sc = new Scanner(System.in);
    
    public static int[] getTTTInput(int turn, int size){
        System.out.println("IT IS NOW PLAYER " + turn + "'S TURN. INPUT GRID SPACE TO MOVE");
        int[] coords = readCoords(sc.nextLine());
        while(coords[0] >= size || coords[1] >= size || coords[0] < 0 || coords[1] < 0){
            System.out.println("NOT A VALID INPUT. TRY AGAIN");
            coords = readCoords(sc.nextLine());
        }
        return coords;
    }

    //x, y, type
    public static int[] getMSInput(){
        int type = 0;
        String s = sc.nextLine();
        switch(s.charAt(0)){
            case 'f': type = 1; break;
            case 'r': type = 2; break;
        }
        System.out.println(type);
        int[] coords = readCoords(s.substring(2));
        int[] xyturn = {coords[0], coords[1], type};
        return xyturn;
    }

    static int[] readCoords(String toparse){
        int[] coords = new int[2];
        coords[0] = toparse.charAt(0) - 'a';
        coords[1] = Integer.parseInt(toparse.substring(1)) - 1;
        return coords;
    }
}
