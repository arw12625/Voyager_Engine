/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.HashMap;
import java.util.Set;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ResourceManager extends Manager {

    HashMap<String, Resource> queued;
    HashMap<String, Resource> loaded;
    static ResourceManager instance;

    @Override
    public void create() {
        super.create();
        queued = new HashMap<String, Resource>();
        loaded = new HashMap<String, Resource>();
        DebugMessages.getInstance().write("ResourceManager created");
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
        Set<String> queuedKeys = queued.keySet();
        for (String key : queuedKeys) {
            queued.get(key).destroy();
        }
        queued.clear();

        Set<String> loadedKeys = loaded.keySet();
        for (String key : loadedKeys) {
            loaded.get(key).destroy();
        }
        loaded.clear();
        DebugMessages.getInstance().write("ResourceManager destroyed");
    }

    public boolean queueResource(Resource r) {
        queued.put(r.getFullName(), r);
        DebugMessages.getInstance().write("Queued: " + r.getFullName());
        return true;
    }

    public boolean loadResource(Resource r) {
        queued.put(r.getFullName(), r);
        if (r.load()) {
            queued.remove(r);
            loaded.put(r.getFullName(), r);
            DebugMessages.getInstance().write("Loading succeeded: " + r.getFullName());
            return true;
        } else {
            DebugMessages.getInstance().write("Loading failed: " + r.getFullName());
            return false;
        }
    }

    public boolean loadQueued() {
        DebugMessages.getInstance().write("Queue loading");
        boolean loaded = true;
        Set<String> keys = queued.keySet();
        for (String key : keys) {
            loaded = loadResource(queued.get(key)) && loaded;
        }
        DebugMessages.getInstance().write("Queue loaded");
        return loaded;
    }

    @Override
    public String getFullName() {
        return "ResourceManager";
    }

    @Override
    public boolean add(GameObject obj) {
        if(obj instanceof Resource) {
            loadResource((Resource)obj);
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if(loaded.containsKey(obj.getFullName())) {
            loaded.remove(obj.getFullName());
        }
    }

}
