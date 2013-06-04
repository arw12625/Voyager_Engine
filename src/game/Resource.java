/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Andy
 */
public interface Resource extends GameObject{
    
    public boolean load();
    public void release();
    
}
