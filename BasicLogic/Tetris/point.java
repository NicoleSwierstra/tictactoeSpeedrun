package BasicLogic.Tetris;

class point {
    public float x, y;
    point(float x, float y){
        this.x = x; this.y = y;
    }

    point rotated(int rot){
        rot %= 4;
        double r = rot * 0.5f * Math.PI;
        return new point(
            (float)(x * Math.cos(r) - y * Math.sin(r)),
            (float)(y * Math.cos(r) + x * Math.sin(r))
        );
    }
}