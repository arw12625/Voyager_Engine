/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.Game;
import game.GameObject;
import game.StandardManager;
import java.util.HashMap;
import java.util.LinkedList;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ResourceManager extends StandardManager implements Runnable {

    LinkedList<Resource> queuedResources;
    LinkedList<GraphicsResource> queuedGraphics;
    HashMap<String, Resource> loaded;
    private Thread resourceThread;
    static ResourceManager instance;

    @Override
    public void create() {
        super.create();
        queuedResources = new LinkedList<Resource>();
        queuedGraphics = new LinkedList<GraphicsResource>();
        loaded = new HashMap<String, Resource>();
        resourceThread = new Thread(this);
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    @Override
    public void destroy() {
        super.destroy();
        queuedResources.clear();
        queuedGraphics.clear();
        loaded.clear();
    }

    private boolean loadResource(Resource r) {
        if (r.load()) {
            if (r instanceof GraphicsResource) {
                queuedGraphics.add((GraphicsResource) r);
            } else {
                loaded.put(r.getFullName(), r);
                DebugMessages.getInstance().write("Loading succeeded: " + r.getFullName());
            }
            r.setIsLoaded(true);
            return true;
        } else {
            DebugMessages.getInstance().write("Loading failed: " + r.getFullName());
            return false;
        }
    }

    @Override
    public boolean add(GameObject obj) {
        if (obj instanceof Resource) {
            util.DebugMessages.getInstance().write("ADDING To Resource Queue" + obj);
            queuedResources.add((Resource) obj);
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if (loaded.containsKey(obj.getFullName())) {
            loaded.remove(obj.getFullName());
        }
    }

    @Override
    public void run() {
        while (Game.isRunning()) {
            while (isLoading()) {
                loadResource(queuedResources.pop());
            }
            Thread.yield();
        }
    }

    public void processGraphics() {
        while (isGraphicsProcessing()) {
            GraphicsResource g = queuedGraphics.pop();
            if (g.processGraphics()) {
                loaded.put(g.getFullName(), g);
                g.setIsprocessed(true);
            }
        }
    }

    public boolean isLoading() {
        return !queuedResources.isEmpty();
    }

    public void start() {
        resourceThread.start();
    }

    private boolean isGraphicsProcessing() {
        return !queuedGraphics.isEmpty();
    }
}