/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import update.Updateable;

/**
 *
 * @author Andy
 */
public abstract class Manager extends GameObject {
    
    public abstract boolean add(GameObject obj);
    public abstract void remove(GameObject obj);
    
}
