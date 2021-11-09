package BasicLogic.Tetris;

public class Tetrimino {
    public point pos;
    public point[] offsets;
    public int type;
    public int rotation;

    public Tetrimino(point p, point[] ps, int t){
        pos = p;
        offsets = ps;
        type = t;
    }

    public Tetrimino(Tetrimino t){
        pos = new point(t.pos.x, t.pos.y);
        offsets = t.offsets;
        type = t.type;
    }

    public pointi[] getPoints(){
        pointi[] worldPoints = new pointi[4];
        for(int i = 0; i < 4; i++){
            worldPoints[i] = new pointi(new point(offsets[i].rotated(rotation).x + pos.x, offsets[i].rotated(rotation).y + pos.y));
        }
        return worldPoints;
    }
}