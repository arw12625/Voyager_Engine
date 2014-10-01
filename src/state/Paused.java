/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state;

import input.InputManager;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Andy
 */
public class Paused extends BasicState {

    static int pause = Keyboard.KEY_ESCAPE;
    static int console = Keyboard.KEY_GRAVE;
    
    static Paused instance;
    public static Paused getInstance() {
        if (instance == null) {
            instance = new Paused();
        }
        return instance;
    }
    
    @Override
    public void run() {
        super.run();
        if(InputManager.getInstance().getKey(pause).isPressed()) {
             game.Game.changeState(Running.getInstance());
        } else if (InputManager.getInstance().getKey(console).isPressed()) {
            game.Game.changeState(ConsoleState.getInstance());
        }
    }
    
    @Override
    public void init() {
        super.run();
        InputManager.getInstance().put(pause);
        InputManager.getInstance().put(console);
    }

}
