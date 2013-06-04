/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import script.Console;
import game.Game;
import game.InputManager;
import game.Manager;
import org.lwjgl.input.Keyboard;
import update.Entity;

/**
 *
 * @author Andy
 */
public class GameStateManager implements Manager, Entity {

    boolean paused;
    static final int toggleKey = Keyboard.KEY_GRAVE;
    static final int exitKey = Keyboard.KEY_ESCAPE;
    static GameStateManager instance;

    public static Manager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    @Override
    public void create() {
        InputManager.getInstance().put(toggleKey);
        InputManager.getInstance().put(exitKey);
    }

    @Override
    public void destroy() {
    }

    @Override
    public String getName() {
        return "Game State Manager";
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
}
