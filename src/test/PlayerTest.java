/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import game.Game;
import game.GameObjectManager;
import graphics.SkySphere;
import graphics.ThreeDGraphicsManager;
import graphics.ThreeDModel;
import input.InputManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import physics.*;
import resource.FontManager;
import resource.ResourceManager;
import resource.TextureManager;
import script.Console;
import script.ScriptManager;
import sound.SoundManager;
import update.UpdateManager;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class PlayerTest {

    public static void main(String[] args) {

        UpdateManager updateManager = UpdateManager.getInstance();
        ThreeDGraphicsManager graphicsManager = ThreeDGraphicsManager.getInstance();
        InputManager inputManager = InputManager.getInstance();
        ResourceManager resourceManager = ResourceManager.getInstance();
        GameObjectManager gameObjectManager = GameObjectManager.getInstance();
        ScriptManager scriptManager = ScriptManager.getInstance();

        Game.create("THE GAME", updateManager, graphicsManager, inputManager, resourceManager, gameObjectManager, scriptManager);

        FontManager.getInstance().create();
        TextureManager.getInstance().create();
        SoundManager.getInstance().create();
        ThreeDPhysicsManager.getInstance().create();
        DebugMessages.getInstance().create();
        GameStateManager.getInstance().create();

        PhysicalPlayer player = new PhysicalPlayer();
        player.create();
        Game.setPlayer(player);
        graphicsManager.setViewPoint(player.getViewPoint());

        Console.getInstance().create();
        ScriptManager.getInstance().loadAndExecute("Default.js");
        ScriptManager.getInstance().loadAndExecute("PlayerTest.js");

        
        
        Game.run();
        Game.destroy();
    }
}
