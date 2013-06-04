/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author Andy
 */
public class Game {

    public static String title;
    public static UpdateManager updateManager;
    public static DisplayManager displayManager;
    public static GraphicsManager graphicsManager;
    public static InputManager inputManager;
    public static SoundManager soundManager;
    public static PhysicsManager physicsManager;
    public static ResourceManager resourceManager;
    public static ArrayList<Manager> managers;
    public static Player player;
    private static boolean quit;

    public static void create(String title,
            UpdateManager updateManager, DisplayManager displayManager,
            GraphicsManager graphicsManager, InputManager inputManager,
            SoundManager soundManager, PhysicsManager physicsManager,
            ResourceManager resourceManager, Player player) {
        Game.title = title;
        Game.updateManager = updateManager;
        Game.displayManager = displayManager;
        Game.graphicsManager = graphicsManager;
        Game.inputManager = inputManager;
        Game.soundManager = soundManager;
        Game.physicsManager = physicsManager;
        Game.resourceManager = resourceManager;

        managers = new ArrayList<Manager>();
        managers.add(updateManager);
        updateManager.create();
        
        addManager(displayManager);
        addManager(graphicsManager);
        addManager(inputManager);
        addManager(soundManager);
        addManager(physicsManager);
        addManager(resourceManager);
        

        Game.player = player;
        updateManager.addEntity(player);

    }

    public static void run() {

        updateManager.start();
        while (!quit) {

            graphicsManager.render();
            displayManager.refresh();
            if (displayManager.isCloseRequested()) {
                quit();
            }

        }
    }

    public static void destroy() {

        for (Manager m : managers) {
            m.destroy();
        }

        System.exit(0);


    }

    public static void quit() {
        quit = true;
    }

    public static boolean isRunning() {
        return !quit;
    }

    public static void addManager(Manager m) {
        managers.add(m);
        updateManager.addEntity(m);
        m.create();
    }

    public static void removeManager(Manager m) {
        m.destroy();
        managers.remove(m);
    }

    public static Player getPlayer() {
        return player;
    }
}