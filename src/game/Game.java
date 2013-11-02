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
import script.ScriptManager;

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
    public static ScriptManager scriptManager;
    public static ArrayList<Manager> managers;
    public static Player player;
    private static boolean quit;
    private static boolean init;

    public static void create(String title,
            UpdateManager updateManager,
            GraphicsManager graphicsManager, InputManager inputManager,
            ResourceManager resourceManager, GameObjectManager gameObjectManager,
            ScriptManager scriptManager) {

        Game.title = title;
        Game.updateManager = updateManager;
        Game.graphicsManager = graphicsManager;
        Game.inputManager = inputManager;
        Game.resourceManager = resourceManager;
        Game.gameObjectManager = gameObjectManager;
        Game.scriptManager = scriptManager;

        managers = new ArrayList<Manager>();
        setInitializing(true);

        gameObjectManager.create();
        updateManager.create();
        graphicsManager.create();
        inputManager.create();
        resourceManager.create();
        scriptManager.create();
        resourceManager.start();
        updateManager.start();
        ScriptManager.getInstance().loadStartupScripts();

    }

    public static void run() {
        
        while(initializing()) {
            resourceManager.processGraphics();
            Thread.yield();
        }
        while (!quit) {
            resourceManager.processGraphics();
            graphicsManager.render();
            if (graphicsManager.isCloseRequested()) {
                quit();
            }

        }
        Game.destroy();
    }

    public static void destroy() {

        ArrayList<Manager> copy  = (ArrayList<Manager>) managers.clone();
        for (Manager m : copy) {
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
    
    public static boolean initializing() {
        return init;
    }
    
    public static void setInitializing(boolean init) {
        Game.init = init;
    }
}