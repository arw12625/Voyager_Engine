/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public abstract class GraphicsManager implements Manager {

    public int width, height;
    
    @Override
    public void create() {
        
        width = Game.displayManager.getWidth();
        height = Game.displayManager.getHeight();
        glViewport(0, 0, width, height);
        glClearColor(0f, 0f, 0f, 0f);
        DebugMessages.getInstance().write("GraphicsManager created");

    }
    
    @Override
    public void destroy() {
        DebugMessages.getInstance().write("GraphicsManager destroyed");
    }
    
    @Override
    public String getName() {
        return "GraphicsManager";
    }
    
    @Override
    public void update(int delta) {
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public abstract void render();
    
}
