package Graphics;

import javax.swing.*;

import BasicLogic.TicTacToe.tttBoard;
import BasicLogic.Minesweeper.msBoard;

import java.awt.*;
import java.awt.event.*;

public class Window {
    public class GraphicPanel extends JComponent implements Runnable {

        private static final long serialVersionUID = 1L;

        int panewidth = 720, paneheight = 720;

        Point mouse_loc;

        Renderer renderer;

        GraphicPanel() {
            setPreferredSize(new Dimension(panewidth, paneheight));
            JComponent pane = this;
            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent componentEvent) {
                    paneheight = pane.getHeight();
                    panewidth = pane.getWidth();
                    renderer.resize();
                }
            });
            setFocusTraversalKeysEnabled(false);
            setFocusable(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0.05f, 0.04f, 0.1f));
            g.fillRect(0, 0, panewidth, paneheight);
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point o = this.getLocationOnScreen();
            
            renderer.Render(g, (int)(p.getX() - o.getX()), (int)(p.getY() - o.getY()));
        }

        public void run() {
            long end;
            while(true) {
                end = System.nanoTime() + 16666666;
                this.repaint();
                while(System.nanoTime() < end);
            }
        }

        public int shortest(){
            return Math.min(paneheight, panewidth);
        }
    }

    JFrame mainwindow;
    GraphicPanel g;
    Renderer r;

    public Window(int type, int arg){
        mainwindow = new JFrame("Poker");
        mainwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g = new GraphicPanel();
        r = type == 1 ?
            new TTTRenderer(new tttBoard(arg), g) :
            new MSRenderer(new msBoard(arg), g);
        g.renderer = r;
        setupInput();
        mainwindow.add(g);
        mainwindow.pack();
        mainwindow.setVisible(true);
        start();
    }

    void start(){
        g.run();
    }

    void setupInput(){
        g.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                r.onMouse(e.getButton(), e.getX(), e.getY());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        g.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == 'r'){
                    r.reset();
                }
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
    }
}
