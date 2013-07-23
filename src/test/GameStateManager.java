/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import input.InputManager;
import update.UpdateManager;
import game.*;
import script.Console;
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
        InputManager.getInstance().put(toggleKey);
        InputManager.getInstance().put(exitKey);
    }

    @Override
    public void update(int delta) {
        if (InputManager.getInstance().get(exitKey).isPressed()) {
            Game.quit();
        }
        if (InputManager.getInstance().get(toggleKey).isPressed()) {
            togglePause();
        }
    }

    public void togglePause() {
        paused = !paused;
        Console.getInstance().setEnabled(paused);
    }

    @Override
    public void remove(GameObject obj) {
    }
}
