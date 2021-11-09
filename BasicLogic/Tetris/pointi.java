package BasicLogic.Tetris;

public class pointi {
    public int x, y;
    pointi(int x, int y){
        this.x = x; 
        this.y = y;
    }

    pointi(point p){
        this.x = Math.round(p.x);
        this.y = Math.round(p.y);
    }
}
