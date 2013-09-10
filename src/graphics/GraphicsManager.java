/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.Manager;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public abstract class GraphicsManager extends Manager {

    int width, height;
    DisplayMode displayMode;
    static final int defaultWidth = 640, defaultHeight = 480;
    private int updateTime;
    static final int defaultUpdateTime = 1000 / 60;
    private boolean closeRequested = false;

    @Override
    public void create() {
        super.create();
        this.displayMode = new DisplayMode(defaultWidth, defaultHeight);
        setDisplayMode(displayMode);
        this.width = displayMode.getWidth();
        this.height = displayMode.getHeight();
        this.updateTime = defaultUpdateTime;
        Display.setVSyncEnabled(true);
        try {
            Display.create();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
        glViewport(0, 0, width, height);
        glClearColor(0f, 0f, 0f, 0f);

    }

    @Override
    public void destroy() {
        super.destroy();
        Display.destroy();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        try {
            Display.setDisplayMode(displayMode);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setTitle(String title) {
        Display.setTitle(title);
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }
    
    public void render() {
        closeRequested = Display.isCloseRequested();
        Display.update();
        Display.sync(updateTime);
    }
}
