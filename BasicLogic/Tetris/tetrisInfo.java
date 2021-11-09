package BasicLogic.Tetris;

import java.awt.Color;

public class tetrisInfo {
    public final static int[] timings = {
        800, 717, 633, 550, 467, 383, 300, 217, 133, 100, 83, 83, 83, 67, 67, 67, 50, 50, 50, 33
    };


    public final static Color[] COLORS = {
        new Color(0, 0, 0),
        new Color(0, 255, 255), //I piece
        new Color(0, 0, 255), //J piece
        new Color(255, 127, 0), //L piece
        new Color(255, 255, 0), //O piece
        new Color(0, 255, 0), //s piece
        new Color(255, 0, 255), //t piece
        new Color(255, 0, 0) //z piece
    };

    public final static Tetrimino[] baseTetriminos = {
        new Tetrimino(new point(0,0), new point[0], 0),
        //I
        new Tetrimino(
            new point(4.5f, 20.5f), 
            new point[]{
                new point(-1.5f, 0.5f),
                new point(-0.5f, 0.5f),
                new point( 0.5f, 0.5f),
                new point( 1.5f, 0.5f),
            }, 
            1
        ),
        //J
        new Tetrimino(
            new point(5.0f, 20f),
            new point[]{
                new point(-1.0f, 1.0f),
                new point(-1.0f, 0.0f),
                new point( 0.0f, 0.0f),
                new point( 1.0f, 0.0f),
            }, 
            2
        ),
        //L
        new Tetrimino(
            new point(4.0f, 20.0f), 
            new point[]{
                new point(-1.0f, 0.0f),
                new point( 0.0f, 0.0f),
                new point( 1.0f, 0.0f),
                new point( 1.0f, 1.0f),
            }, 
            3
        ),
        //O
        new Tetrimino(
            new point(4.5f, 20.5f), 
            new point[]{
                new point(-0.5f,-0.5f),
                new point(-0.5f, 0.5f),
                new point( 0.5f,-0.5f),
                new point( 0.5f, 0.5f),
            }, 
            4
        ),
        //S
        new Tetrimino(
            new point(4.0f, 20.0f), 
            new point[]{
                new point(-1.0f, 0.0f),
                new point( 0.0f, 0.0f),
                new point( 0.0f, 1.0f),
                new point( 1.0f, 1.0f),
            },
            5
        ),
        //T
        new Tetrimino(
            new point(4.0f, 20.0f), 
            new point[]{
                new point(-1.0f, 0.0f),
                new point( 0.0f, 0.0f),
                new point( 0.0f, 1.0f),
                new point( 1.0f, 0.0f),
            }, 
            6
        ),
        //Z
        new Tetrimino(
            new point(5.0f, 20.0f), 
            new point[]{
                new point(-1.0f, 1.0f),
                new point( 0.0f, 1.0f),
                new point( 0.0f, 0.0f),
                new point( 1.0f, 0.0f),
            }, 
            7
        )
    };
}
