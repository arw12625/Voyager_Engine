/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class DisplayManager implements Manager {

    int width, height;
    DisplayMode displayMode;
    static final int defaultWidth = 640, defaultHeight = 480;
    private final int updateTime;
    static final int defaultUpdateTime = 1000 / 60;
    private boolean closeRequested = false;
    
    static DisplayManager instance;

    public DisplayManager() {
        this.displayMode = new DisplayMode(defaultWidth, defaultHeight);
        setDisplayMode(displayMode);
        this.width = displayMode.getWidth();
        this.height = displayMode.getHeight();
        this.updateTime = defaultUpdateTime;
    }

    @Override
    public void create() {
        try {
            Display.setVSyncEnabled(true);
            Display.create();
            instance = new DisplayManager();
        } catch (LWJGLException ex) {
            Logger.getLogger(DisplayManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        DebugMessages.getInstance().write("DisplayManager created");
    }

    public static DisplayManager getInstance() {
        if(instance == null) {
            instance = new DisplayManager();
        }
        return instance;
    }
    
    @Override
    public void destroy() {
        Display.destroy();
        DebugMessages.getInstance().write("DisplayManager destroyed");
    }

    public void refresh() {
        closeRequested = Display.isCloseRequested();
        Display.update();
        Display.sync(updateTime);
        DebugMessages.getInstance().write("Display updated");
    }

    public void setDisplayMode(DisplayMode displayMode) {
        try {
            Display.setDisplayMode(displayMode);
        } catch (LWJGLException ex) {
            Logger.getLogger(DisplayManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setTitle(String title) {
        Display.setTitle(title);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    public boolean isCloseRequested() {
        return closeRequested;
    }

    @Override
    public String getName() {
        return "DisplayManager";
    }

    @Override
    public void update(int delta) {
    }

}
