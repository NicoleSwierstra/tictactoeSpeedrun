package Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import BasicLogic.*;
import BasicLogic.Minesweeper.Board;

public class Window {
    public class GraphicPanel extends JComponent implements Runnable {

        private static final long serialVersionUID = 1L;

        int panewidth = 720, paneheight = 720;
        float headerHeight = 0.1f;
        int headerOffset = (int)(headerHeight * paneheight);

        Point mouse_loc;

        MSRenderer renderer;

        GraphicPanel(MSRenderer r) {
            renderer = r;
            setPreferredSize(new Dimension(panewidth, paneheight));
            JComponent pane = this;
            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent componentEvent) {
                    headerOffset = (int)(headerHeight * paneheight);
                    paneheight = pane.getHeight() - headerOffset;
                    panewidth = pane.getWidth();
                    renderer.resize();
                }
            });
            setFocusTraversalKeysEnabled(false);
            setFocusable(true);
            //new Thread(this).run();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0.05f, 0.04f, 0.1f));
            g.fillRect(0, headerOffset, panewidth, paneheight);
            g.setColor(new Color(0.1f, 0.1f, 0.1f));
            g.fillRect(0, 0, panewidth, headerOffset);
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point o = this.getLocationOnScreen();
            renderer.renderHeader(g, headerOffset);
            g.translate(0, headerOffset);
            renderer.Render(g, (int)(p.getX() - o.getX()), (int)(p.getY() - o.getY()) - headerOffset);
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
            return Math.min(paneheight + headerOffset, panewidth);
        }
    }

    JFrame mainwindow;
    GraphicPanel g;
    MSRenderer r;

    public Window(Board b){
        mainwindow = new JFrame("Poker");
        mainwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g = new GraphicPanel(r);
        r = new MSRenderer(b, g);
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
                r.onMouse(e.getButton(), e.getX(), e.getY() - g.headerOffset);
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
    }
}
