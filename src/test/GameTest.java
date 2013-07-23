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
import physics.ThreeDPlayer;
import script.Console;
import util.DebugMessages;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import resource.FontManager;
import resource.TextureManager;
import script.GameScript;
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

        FontManager.getInstance().create();
        TextureManager.getInstance().create();
        SoundManager.getInstance().create();
        ThreeDPhysicsManager.getInstance().create();
        ScriptManager.getInstance().create();
        DebugMessages.getInstance().create();
        GameStateManager.getInstance().create();

        ThreeDPlayer player = new ThreeDPlayer();
        player.create();
        Game.setPlayer(player);
        graphicsManager.setViewPoint(player.getViewPoint());
        
        Console.getInstance().create();
        graphics = new ArrayList<TestGraphic>();
        /*addTestGraphic();
        addTestGraphic();
        addTestGraphic();
        addTestGraphic();*/

        ScriptManager sm = ScriptManager.getInstance();
        try {
            sm.eval("importClass(Packages.test.GameTest)");
            sm.eval("function addTri() { GameTest.addTestGraphic() }");
            sm.eval("function removeTri() { GameTest.removeTestGraphic() }");
            sm.eval("function enableDebug(enable) { GameTest.enableDebug(enable) }");
            sm.execute(new GameScript("BLAAS", ""));
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
        
        /*Mesh m = new Mesh("palm_fix", "palm_fix");
        ResourceManager.getInstance().loadResource(m);
        System.out.println(m);
        ThreeDGraphicsManager.getInstance().addGraphic3D(new ThreeDModel(m));*/
        
        Mesh ter = new Mesh("terrain", "terrain");
        ter.create();
        ResourceManager.getInstance().hackyUpdate();
        ThreeDModel modeltest = new ThreeDModel(ter);
        modeltest.create();
        graphicsManager.add(modeltest);
        
        player.getPhysicalEntity().getBounds().setPosition(new Vector3f(0, 0, 60));
        InputManager.getInstance().put(Keyboard.KEY_UP);
        InputManager.getInstance().put(Keyboard.KEY_DOWN);
        InputManager.getInstance().put(Keyboard.KEY_LEFT);
        InputManager.getInstance().put(Keyboard.KEY_RIGHT);
        Mouse.setGrabbed(true);
        
        SkySphere s = new SkySphere();
        s.create();
        graphicsManager.addGraphic3D(s, -100);
        
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
