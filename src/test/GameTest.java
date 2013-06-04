/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import game.*;
import graphics.*;
import org.lwjgl.input.Keyboard;
import physics.ThreeDPhysicsManager;
import physics.ThreeDPlayer;
import update.ThreeDUpdateManager;
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

        ThreeDUpdateManager updateManager = ThreeDUpdateManager.getInstance();
        DisplayManager displayManager = DisplayManager.getInstance();
        ThreeDGraphicsManager graphicsManager = ThreeDGraphicsManager.getInstance();
        InputManager inputManager = InputManager.getInstance();
        SoundManager soundManager = SoundManager.getInstance();
        ThreeDPhysicsManager physicsManager = ThreeDPhysicsManager.getInstance();
        ResourceManager resourceManager = ResourceManager.getInstance();

        ThreeDPlayer player = new ThreeDPlayer();

        graphicsManager.setViewPoint(player.getViewPoint());

        Game.create("THE GAME", updateManager, displayManager, graphicsManager, inputManager, soundManager, physicsManager, resourceManager, player);

        Console c = Console.getInstance();
        ScriptManager sm = ScriptManager.getInstance();
        
        Game.addManager(FontManager.getInstance());
        Game.addManager(TextureManager.getInstance());
        Game.addManager(sm);
        Game.addManager(c);
        Game.addManager(DebugMessages.getInstance());
        Game.addManager(GameStateManager.getInstance());
        
        // graphicsManager.addGraphic2D(new HudGraphic(TextureManager.getInstance().loadTextureResource("res/fuckopengl.png"), null), -10000);

        graphics = new ArrayList<TestGraphic>();
        /*addTestGraphic();
        addTestGraphic();
        addTestGraphic();
        addTestGraphic();*/

        try {
            sm.eval("importClass(Packages.test.GameTest)");
            sm.eval("function addTri() { GameTest.addTestGraphic() }");
            sm.eval("function removeTri() { GameTest.removeTestGraphic() }");
            sm.eval("function enableDebug(enable) { GameTest.enableDebug(enable) }");
        } catch(ScriptException e) {
            e.printStackTrace();
        }
        try {
            ScriptManager.getInstance().execute(new GameScript("BLAAS", ""));
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
        
        /*Mesh m = new Mesh("palm_fix", "palm_fix");
        ResourceManager.getInstance().loadResource(m);
        System.out.println(m);
        ThreeDGraphicsManager.getInstance().addGraphic3D(new ThreeDModel(m));*/
        
        Mesh ter = new Mesh("terrain", "terrain");
        ResourceManager.getInstance().loadResource(ter);
        System.out.println(ter);
        ThreeDGraphicsManager.getInstance().addGraphic3D(new ThreeDModel(ter));
        
        ThreeDPhysicsManager.getInstance().addEntity(player);
        player.getBounds().setPosition(new Vector3f(0, 0, 60));
        InputManager.getInstance().put(Keyboard.KEY_UP);
        InputManager.getInstance().put(Keyboard.KEY_DOWN);
        InputManager.getInstance().put(Keyboard.KEY_LEFT);
        InputManager.getInstance().put(Keyboard.KEY_RIGHT);
        Mouse.setGrabbed(true);
        
        ThreeDGraphicsManager.getInstance().addGraphic3D(new SkySphere(), -1000);
        
        RigidBody rb = RigidBody.rigidBodyFromPath("palm_fix", "palm_fix");
        ThreeDGraphicsManager.getInstance().addGraphic3D(rb);
        ThreeDPhysicsManager.getInstance().addEntity(player);
        
        Game.run();
        Game.destroy();
    }

    public static void addTestGraphic() {
        TestGraphic g = new TestGraphic();
        graphics.add(g);
        ThreeDUpdateManager.getInstance().addEntity(g);
        ThreeDGraphicsManager.getInstance().addGraphic2D(g);

    }

    public static void removeTestGraphic() {
        ThreeDUpdateManager um = ThreeDUpdateManager.getInstance();
        ThreeDGraphicsManager gm = ThreeDGraphicsManager.getInstance();
        for (TestGraphic g : graphics) {
            um.removeEntity(g);
            gm.removeGraphic2D(g);
        }
        graphics.clear();
    }
    
    public static void enableDebug(boolean enable) {
        DebugMessages.getInstance().setEnabled(enable);
    }
}
