/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import game.*;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Andy
 */
public class GameStateManager extends Manager implements update.Updateable {

    boolean paused;
    static final int toggleKey = Keyboard.KEY_GRAVE;
    static final int exitKey = Keyboard.KEY_ESCAPE;
    static GameStateManager instance;

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    @Override
    public void create() {
        super.create();
        input.InputManager.getInstance().put(toggleKey);
        input.InputManager.getInstance().put(exitKey);
    }

    @Override
    public boolean update(int delta) {
        if (input.InputManager.getInstance().get(exitKey).isPressed()) {
            Game.quit();
        }
        if (input.InputManager.getInstance().get(toggleKey).isPressed()) {
            togglePause();
        }
        
        return false;
    }

    public void togglePause() {
        paused = !paused;
        script.Console.getInstance().setEnabled(paused);
    }

    @Override
    public void remove(GameObject obj) {
    }
}
