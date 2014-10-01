/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state;

/**
 *
 * @author Andy
 */
public class Quit extends GameState{

    static Quit instance;
    public static Quit getInstance() {
        if (instance == null) {
            instance = new Quit();
        }
        return instance;
    }
    
    @Override
    public void init() {
    }

    @Override
    public void run() {
    }

    @Override
    public void end() {
    }
    
}
