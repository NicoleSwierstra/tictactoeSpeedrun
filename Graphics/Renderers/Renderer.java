package Graphics.Renderers;

import java.awt.*;

public interface Renderer {
    void Render(Graphics g, int xmouse, int ymouse);
    void onMouse(int button, int x, int y);
    void reset();
    void resize();
}
