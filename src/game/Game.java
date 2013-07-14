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
    public static GraphicsManager graphicsManager;
    public static InputManager inputManager;
    public static ResourceManager resourceManager;
    public static GameObjectManager gameObjectManager;
    public static ArrayList<Manager> managers;
    public static Player player;
    private static boolean quit;

    public static void create(String title,
            UpdateManager updateManager,
            GraphicsManager graphicsManager, InputManager inputManager,
            ResourceManager resourceManager, GameObjectManager gameObjectManager
            ) {
        
        Game.title = title;
        Game.updateManager = updateManager;
        Game.graphicsManager = graphicsManager;
        Game.inputManager = inputManager;
        Game.resourceManager = resourceManager;
        Game.gameObjectManager = gameObjectManager;

        managers = new ArrayList<Manager>();
        
        gameObjectManager.createAndAdd();
        updateManager.createAndAdd();
        graphicsManager.createAndAdd();
        inputManager.createAndAdd();
        resourceManager.createAndAdd();

    }

    public static void run() {

        updateManager.start();
        while (!quit) {

            graphicsManager.render();
            if (graphicsManager.isCloseRequested()) {
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

    public void setPlayer(Player p) {
        this.player = p;
    }
    
    public static void addGameObject(GameObject obj) {
        if (obj instanceof Manager) {
            managers.add((Manager) obj);
        }
        for (Manager m : managers) {
            m.add(obj);
        }
    }
    
    public static void removeGameObject(GameObject obj) {
        if (managers.contains(obj)) {
            managers.remove(obj);
        }
        for (Manager m : managers) {
            m.remove(obj);
        }
    }

    public static Player getPlayer() {
        return player;
    }
}