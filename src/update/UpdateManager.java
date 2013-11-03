/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import game.Game;
import game.GameObject;
import game.StandardManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.Sys;
import resource.ResourceManager;
import script.ScriptManager;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class UpdateManager extends StandardManager implements Runnable {

    long lastTime;
    final List<Updateable> entities = new ArrayList<Updateable>();
    private long updateTime;
    private Thread updateThread;
    static final int defaultUpdateTime = 1000 / 60;
    static UpdateManager instance;

    @Override
    public void create() {
        super.create();
        updateThread = new Thread(this);
        //entities = new ArrayList<>();
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            updateThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            instance = new UpdateManager();
        }
        return instance;
    }

    public void start() {
        updateThread.start();
    }

    @Override
    public void run() {
        while (Game.initializing()) {
            script.ScriptManager.getInstance().executeScripts();
            Thread.yield();
        }
        this.updateTime = defaultUpdateTime;
        lastTime = getTime() - updateTime;
        while (Game.isRunning()) {
            DebugMessages.getInstance().write("UpdateManager Running");
            long currentTime = getTime();
            long deltaTime = currentTime - lastTime;
            update((int) deltaTime);
            script.ScriptManager.getInstance().executeScripts();
            lastTime = currentTime;
            try {
                Thread.sleep(updateTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(int delta) {
        DebugMessages.getInstance().write("Delta: " + delta + "  FPS: " + (1000 / delta));
        DebugMessages.getInstance().write("Updates starting");

        input.InputManager.getInstance().processInputs();

        int i = entities.size() - 1;

        while (i >= 0) { // Subject to off-by-one
            boolean removeThisObject = entities.get(i).update(delta);
            if (removeThisObject) {
                entities.remove(i);
            }
            i--;
        }
        /*
         * Iterator<Updateable> iter = entities.iterator(); while
         * (iter.hasNext()) { try { Updateable u = iter.next(); boolean toRemove
         * = u.update(delta); if (toRemove) {
         * DebugMessages.getInstance().write("wtf?"); iter.remove(); } } catch
         * (ConcurrentModificationException e) { System.out.println("Bleh.
         * Breaking due to concurrentmodificationexception."); break; }
         *
         * }
         */

        DebugMessages.getInstance().write("Updates finished");
    }

    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    @Override
    public boolean add(GameObject obj) {
        if (obj instanceof Updateable) {
            entities.add((Updateable) obj);
            return true;
        }
        return false;
    }

    public boolean add(Updateable u) {
        entities.add(u);
        return true;
    }

    @Override
    public void remove(GameObject obj) {
        if (entities.contains(obj)) {
            entities.remove(obj);
        }
    }

    public void remove(Updateable obj) {
        if (entities.contains(obj)) {
            entities.remove(obj);
        }
    }
}
