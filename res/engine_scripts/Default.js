/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
with(resource) {
    FontManager.getInstance().create();
    TextureManager.getInstance().create();
}
sound.SoundManager.getInstance().create();
physics.ThreeDPhysicsManager.getInstance().create();
util.DebugMessages.getInstance().create();
script.Console.getInstance().create();


var i = 0;
var packageNames = ["input", "graphics", "physics", "resource", "script", "sound", "update"];
var managerNames = ["InputManager", "ThreeDGraphicsManager", "ThreeDPhysicsManager", "ResourceManager", "ScriptManager", "SoundManager", "UpdateManager"];
for(i = 0; i < packageNames.length; ++i) {
    script.ScriptUtil.makeManagerVar(packageNames[i], managerNames[i]);
}

var yolo = JavaImporter(Packages.org.lwjgl.input);

    InputManager.put(yolo.Keyboard.KEY_UP);
    InputManager.put(yolo.Keyboard.KEY_DOWN);
    InputManager.put(yolo.Keyboard.KEY_LEFT);
    InputManager.put(yolo.Keyboard.KEY_RIGHT);
    InputManager.put(yolo.Keyboard.KEY_W);
    InputManager.put(yolo.Keyboard.KEY_S);
    InputManager.put(yolo.Keyboard.KEY_A);
    InputManager.put(yolo.Keyboard.KEY_D);
    InputManager.put(yolo.Keyboard.KEY_LSHIFT);
    InputManager.put(yolo.Keyboard.KEY_SPACE);
    InputManager.setGrabbed(true);
    
function enableDebug(enable) {
    test.GameTest.enableDebug(enable);
}