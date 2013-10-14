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

        FontManager.getInstance().create();
        TextureManager.getInstance().create();
        SoundManager.getInstance().create();
        ThreeDPhysicsManager.getInstance().create();
        ScriptManager.getInstance().create();
        DebugMessages.getInstance().create();
        GameStateManager.getInstance().create();

        VerticalPlayer player = new VerticalPlayer();
        player.create();
        Game.setPlayer(player);
        graphicsManager.setViewPoint(player.getViewPoint());

        Console.getInstance().create();
        ScriptManager.getInstance().loadAndExecute("GameTest.js");

        ForceGenerator grav = new ForceGenerator() {

            @Override
            public void applyForce(PhysicalEntity pe) {
                pe.applyForce(new Vector3f(0, -8f * pe.getMass(), 0));
            }
        };

        WavefrontModel ter = new WavefrontModel("teapot_fix");
        ter.create();
        ThreeDModel terDisp = new ThreeDModel(ter);
        terDisp.create();
        ThreeDGraphicsManager.getInstance().add(terDisp);
        CollisionMesh cm = new CollisionMesh(ter.getObjects());
        //System.out.println(ter.getObjects().get(0).getBounds());
        cm.create();
        physics.ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);

        player.getPhysicalEntity().setPosition(new Vector3f(0, 7, 0));
        //player.getPhysicalEntity().addForceGenerator(grav);

        final RigidBody green = RigidBody.rigidBodyFromPath("box_fix");
        green.create();
        graphicsManager.add(green);
        //green.getBounds().setOrientation(Utilities.quatFromAxisAngle(new Vector3f(0, 0, 1), -(float)Math.PI));
        green.setPosition(new Vector3f(-20, 15f, 7));
        physics.ThreeDPhysicsManager.getInstance().add(green);
        green.addForceGenerator(grav);

        InputManager.getInstance().put(Keyboard.KEY_UP);
        InputManager.getInstance().put(Keyboard.KEY_DOWN);
        InputManager.getInstance().put(Keyboard.KEY_LEFT);
        InputManager.getInstance().put(Keyboard.KEY_RIGHT);
        Mouse.setGrabbed(true);

        SkySphere s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
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
