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
import physics.PhysicalPlayer;
import script.Console;
import util.DebugMessages;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import physics.*;
import resource.FontManager;
import resource.TextureManager;
import script.GameScript;
import script.ScriptManager;
import update.Updateable;
import util.Utilities;

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

        PhysicalPlayer player = new PhysicalPlayer();
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
            sm.execute(new GameScript(""));
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
        
        /*Mesh m = new Mesh("palm_fix", "palm_fix");
        ResourceManager.getInstance().loadResource(m);
        System.out.println(m);
        ThreeDGraphicsManager.getInstance().addGraphic3D(new ThreeDModel(m));*/
        
        final RigidBody green = RigidBody.rigidBodyFromPath("box_fix");
        green.create();
        //green.getBounds().setOrientation(Utilities.quatFromAxisAngle(new Vector3f(0, 0, 1), -(float)Math.PI));
        
        green.setPosition(new Vector3f(-0, 8f, 0));
        //green.setPosition(new Vector3f(0, 7, 0));
        physics.ThreeDPhysicsManager.getInstance().add(green);
        graphicsManager.add(green);
        ForceGenerator grav = new ForceGenerator() {

            @Override
            public void applyForce(PhysicalEntity pe) {
                pe.applyForce(new Vector3f(0, -3f* pe.getMass(), 0));
            }
        };
        green.addForceGenerator(grav);
        
        /*RigidBody ter = RigidBody.rigidBodyFromPath("terrain-fix");
        ter.create();
        physics.ThreeDPhysicsManager.getInstance().add(ter);
        graphicsManager.add(ter);*/
        Mesh ter = new Mesh("building_fix");
        ter.create();
        ThreeDModel terDisp = new ThreeDModel(ter);
        terDisp.create();
        ThreeDGraphicsManager.getInstance().add(terDisp);
        CollisionMesh cm = new CollisionMesh(ter);
        cm.create();
        physics.ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);
        
        InputManager.getInstance().put(Keyboard.KEY_UP);
        InputManager.getInstance().put(Keyboard.KEY_DOWN);
        InputManager.getInstance().put(Keyboard.KEY_LEFT);
        InputManager.getInstance().put(Keyboard.KEY_RIGHT);
        Mouse.setGrabbed(true);
        
        //player.getPhysicalEntity().addForceGenerator(grav);
        
        SkySphere s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
        s.create();
        graphicsManager.addGraphic3D(s, -100);
        
        player.getPhysicalEntity().setPosition(new Vector3f(0, 7, 0));
        
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
