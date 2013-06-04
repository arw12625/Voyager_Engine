/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import org.lwjgl.Sys;
import util.DebugMessages;
import update.Entity;

/**
 *
 * @author Andy
 */
public abstract class UpdateManager extends Thread implements Manager {

    long lastTime;
    private long updateTime;
    static final int defaultUpdateTime = 1000 / 60;
    
    static UpdateManager instance;

    public UpdateManager() {
        this.updateTime = defaultUpdateTime;
        lastTime = getTime();
    }

    @Override
    public void create() {
        DebugMessages.getInstance().write("UpdateManager created");
    }

    @Override
    public void destroy() {
        try {
            join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DebugMessages.getInstance().write("UpdateManager destroyed");
    }
    
    @Override
    public void update(int delta) {
    }

    @Override
    public void run() {
        while (Game.isRunning()) {
            DebugMessages.getInstance().write("UpdateManager Running");
            long currentTime = getTime();
            long deltaTime = currentTime - lastTime;
            updateGame((int) deltaTime);
            lastTime = currentTime;
            try {
                Thread.sleep(updateTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static long getTime() {

        return (Sys.getTime() * 1000) / Sys.getTimerResolution();

    }

    public abstract void updateGame(int delta);

    public abstract void addEntity(Entity e);
    public abstract void removeEntity(Entity e);
    
}
