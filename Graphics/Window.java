package Graphics;

import javax.swing.*;

import Graphics.Renderers.*;
import Graphics.Renderers.Renderer;

import java.awt.*;
import java.awt.event.*;

public class Window {
    public class GraphicPanel extends JComponent implements Runnable {

        private static final long serialVersionUID = 1L;

        public int panewidth = 720;

        public int paneheight = 720;

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

            new Animation();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0.05f, 0.04f, 0.1f));
            g.fillRect(0, 0, panewidth, paneheight);
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point o = this.getLocationOnScreen();
            
            renderer.Render(g, (int)(p.getX() - o.getX()), (int)(p.getY() - o.getY()));
            Animation.Update(0.0166666f, g);
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
    Thread renderThread;
    Renderer r;

    public Window(){
        mainwindow = new JFrame("MINIGAME EXTRAVAGANZA");
        mainwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g = new GraphicPanel();
        r = new MainMenu(g, this);
        g.renderer = r;
        setupInput();
        mainwindow.add(g);
        mainwindow.pack();
        mainwindow.setVisible(true);
        start();
    }

    void start(){
        new Thread(g).start();
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
        Window w = this;
        g.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == 'r'){
                    r.reset();
                }
                else if(e.getKeyChar() == 'm'){
                    setScene(new MainMenu(g, w));
                }
            }
            @Override public void keyPressed(KeyEvent e) {
                if(r instanceof TetrisRenderer){
                    ((TetrisRenderer)r).onKey(e.getKeyCode());
                }
            }
            @Override public void keyReleased(KeyEvent e) {}
        });
    }

    void setScene(Renderer renderer){
        r = renderer;
        g.renderer = r;
        r.resize();
    }
}
