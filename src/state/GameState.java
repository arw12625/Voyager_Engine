/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state;

/**
 *
 * @author Andy
 */

/*
 * GameState represents the current state of the game
 * as well as contains the exectution code for this state
 */
public abstract class GameState extends game.GameObject {

    //Execution code for when this state is in operation
    public abstract void init();
    public abstract void run();
    public abstract void end();
    
}
