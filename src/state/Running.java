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
public class Running extends BasicState {

    static int pause = Keyboard.KEY_P;
    static int console = Keyboard.KEY_GRAVE;
    static Running instance;

    public static Running getInstance() {
        if (instance == null) {
            instance = new Running();
        }
        return instance;
    }

    @Override
    public void init() {
        super.init();
        InputManager.getInstance().put(pause);
        InputManager.getInstance().put(console);
    }

    @Override
    public void run() {
        super.run();
        if (InputManager.getInstance().getKey(pause).isDown()) {
            game.Game.changeState(Paused.getInstance());
        } else if (InputManager.getInstance().getKey(console).isDown()) {
            
            game.Game.changeState(ConsoleState.getInstance());
        }
    }

    @Override
    public void end() {
    }
}
