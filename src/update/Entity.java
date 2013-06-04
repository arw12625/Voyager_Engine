/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import game.GameObject;

/**
 *
 * @author Andy
 */
public interface Entity extends GameObject{
    
    public void update(int delta);
    
}
