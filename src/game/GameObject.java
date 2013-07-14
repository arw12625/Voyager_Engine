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
public abstract class GameObject {

    int id;
    static HashMap<String, Integer> instances = new HashMap<String, Integer>();

    public void create() {
        String name = this.getClass().getName();
        if (instances.containsKey(name)) {
            id = instances.get(name).intValue();
        } else {
            id = 0;
        }
        instances.put(name, id + 1);
    }

    public void destroy() {
    }
    
    public void createAndAdd() {
        create();
        Game.addGameObject(this);
    }
    
    public void removeAndDestroy() {
        Game.removeGameObject(this);
        destroy();
    }

    public String getFullName() {
        return getClass().getName() + id;
    }
    
}
