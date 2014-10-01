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
public class BasicState extends GameState {

    static int exit = Keyboard.KEY_ESCAPE;
    
    @Override
    public void init() {
        InputManager.getInstance().put(exit);
    }

    @Override
    public void run() {
        game.Game.resourceManager.processGraphics();
        game.Game.graphicsManager.render();
        if (game.Game.graphicsManager.isCloseRequested() || InputManager.getInstance().getKey(exit).isDown()) {
            game.Game.quit();
        }
    }

    @Override
    public void end() {
    }
}
