/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.Manager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public abstract class GraphicsManager extends Manager {

    DisplayMode displayMode;
    private DisplayMode desiredDisplayMode;
    static final int defaultWidth = 640, defaultHeight = 480;
    private int updateTime;
    static final int defaultUpdateTime = 1000 / 60;
    private boolean closeRequested = false;

    @Override
    public void create() {
        super.create();
        this.displayMode = new DisplayMode(defaultWidth, defaultHeight);
        this.desiredDisplayMode = displayMode;
        applyDisplayMode();
        this.updateTime = defaultUpdateTime;
        Display.setVSyncEnabled(true);
        try {
            Display.create();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
        glViewport(0, 0, displayMode.getWidth(), displayMode.getHeight());
        glClearColor(0f, 0f, 0f, 0f);

    }

    @Override
    public void destroy() {
        super.destroy();
        Display.destroy();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.desiredDisplayMode = displayMode;
    }

    public void setTitle(String title) {
        Display.setTitle(title);
    }

    public int getWidth() {
        return displayMode.getWidth();
    }

    public int getHeight() {
        return displayMode.getHeight();
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public void render() {
        closeRequested = Display.isCloseRequested();
        Display.update();
        Display.sync(updateTime);
        if (!desiredDisplayMode.equals(displayMode)) {
            displayMode = desiredDisplayMode;
            applyDisplayMode();
            glViewport(0, 0, getWidth(), getHeight());
        }
    }

    private void applyDisplayMode() {
        try {
            Display.setDisplayMode(displayMode);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }
}
