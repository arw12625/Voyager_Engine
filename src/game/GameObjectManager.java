/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.HashMap;

/**
 *
 * @author Andy
 */
public class GameObjectManager extends StandardManager {

    HashMap<String, GameObject> objects;
    static GameObjectManager instance;
    
    @Override
    public void create() {
        objects = new HashMap<String, GameObject>();
        super.create();
    }
    
    public static GameObjectManager getInstance() {
        if(instance == null) {
            instance = new GameObjectManager();
        }
        return instance;
    }

    @Override
    public boolean add(GameObject obj) {
        objects.put(obj.getFullName(), obj);
        return true;
    }

    @Override
    public void remove(GameObject obj) {
        if(objects.containsKey(obj.getFullName())) {
            objects.remove(obj.getFullName());
        }
    }

}
