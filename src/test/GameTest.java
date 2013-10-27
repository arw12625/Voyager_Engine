/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import physics.Mesh;
import input.InputManager;
import resource.ResourceManager;
import update.UpdateManager;
import sound.SoundManager;
import game.*;
import graphics.*;
import org.lwjgl.input.Keyboard;
import physics.ThreeDPhysicsManager;
import physics.VerticalPlayer;
import script.Console;
import util.DebugMessages;
import java.util.ArrayList;
import javax.script.ScriptException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import physics.*;
import resource.FontManager;
import resource.TextureManager;
import resource.WavefrontModel;
import script.ScriptManager;

/**
 *
 * @author Andy
 */
public class GameTest {

    static ArrayList<TestGraphic> graphics;

    public static void main(String[] args) {

        UpdateManager updateManager = UpdateManager.getInstance();
        ThreeDGraphicsManager graphicsManager = ThreeDGraphicsManager.getInstance();
        InputManager inputManager = InputManager.getInstance();
        ResourceManager resourceManager = ResourceManager.getInstance();
        GameObjectManager gameObjectManager = GameObjectManager.getInstance();

        Game.create("THE GAME", updateManager, graphicsManager, inputManager, resourceManager, gameObjectManager);

        ScriptManager.getInstance().create();
        ScriptManager.getInstance().loadAndExecute("GameTest.js");

        Game.run();
        Game.destroy();
    }

    public static void addTestGraphic() {
        TestGraphic g = new TestGraphic();
        graphics.add(g);
        g.create();

    }

    public static void removeTestGraphic() {
        for (TestGraphic g : graphics) {
            g.destroy();
        }
        graphics.clear();
    }

    public static void enableDebug(boolean enable) {
        DebugMessages.getInstance().setEnabled(enable);
    }
}
