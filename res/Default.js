/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Mouse.setGrabbed(true);

FontManager.getInstance().create();
TextureManager.getInstance().create();
SoundManager.getInstance().create();
ThreeDPhysicsManager.getInstance().create();
DebugMessages.getInstance().create();
GameStateManager.getInstance().create();
Console.getInstance().create();

InputManager.getInstance().put(Keyboard.KEY_UP);
InputManager.getInstance().put(Keyboard.KEY_DOWN);
InputManager.getInstance().put(Keyboard.KEY_LEFT);
InputManager.getInstance().put(Keyboard.KEY_RIGHT);
InputManager.getInstance().put(Keyboard.KEY_W);
InputManager.getInstance().put(Keyboard.KEY_S);
InputManager.getInstance().put(Keyboard.KEY_A);
InputManager.getInstance().put(Keyboard.KEY_D);
InputManager.getInstance().put(Keyboard.KEY_LSHIFT);
Mouse.setGrabbed(true);


function enableDebug(enable) {
    GameTest.enableDebug(enable);
}

ScriptManager.getInstance().loadStartupScripts();