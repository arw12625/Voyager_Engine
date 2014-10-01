/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var toggleKey = Keyboard.KEY_GRAVE;
var exitKey = Keyboard.KEY_ESCAPE;

        input.InputManager.getInstance().put(toggleKey);
        input.InputManager.getInstance().put(exitKey);
        if (input.InputManager.getInstance().get(exitKey).isPressed()) {
            Game.quit();
        }
        if (input.InputManager.getInstance().get(toggleKey).isPressed()) {
            togglePause();
        }