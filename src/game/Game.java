/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import graphics.GraphicsManager;
import input.InputManager;
import resource.ResourceManager;
import update.UpdateManager;
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
            ResourceManager resourceManager, GameObjectManager gameObjectManager) {

        Game.title = title;
        Game.updateManager = updateManager;
        Game.graphicsManager = graphicsManager;
        Game.inputManager = inputManager;
        Game.resourceManager = resourceManager;
        Game.gameObjectManager = gameObjectManager;

        managers = new ArrayList<Manager>();

        gameObjectManager.create();
        updateManager.create();
        graphicsManager.create();
        inputManager.create();
        resourceManager.create();
        resourceManager.start();

    }

    public static void run() {

        updateManager.start();
        while (!quit) {

            resourceManager.hackyUpdate();
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

    public static void setPlayer(Player p) {
        Game.player = p;
    }

    public static void addGameObject(GameObject obj) {
        if (obj instanceof Manager) {
            managers.add((Manager) obj);
        }
        gameObjectManager.add(obj);
        resourceManager.add(obj);
        updateManager.add(obj);
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