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
import state.GameLoading;
import state.GameState;
import state.Quit;

/**
 *
 * @author Andy
 */
public class Game {

    public static String title;
    private static GameState state;
    public static UpdateManager updateManager;
    public static GraphicsManager graphicsManager;
    public static InputManager inputManager;
    public static ResourceManager resourceManager;
    public static GameObjectManager gameObjectManager;
    public static ScriptManager scriptManager;
    public static ArrayList<Manager> managers;
    public static Player player;

    public static void create(String title,
            UpdateManager updateManager,
            GraphicsManager graphicsManager, InputManager inputManager,
            ResourceManager resourceManager, GameObjectManager gameObjectManager,
            ScriptManager scriptManager) {

        Game.title = title;
        Game.state = EngineInit.getInstance();

        Game.updateManager = updateManager;
        Game.graphicsManager = graphicsManager;
        Game.inputManager = inputManager;
        Game.resourceManager = resourceManager;
        Game.gameObjectManager = gameObjectManager;
        Game.scriptManager = scriptManager;

        managers = new ArrayList<Manager>();
    }

    public static void run() {

        state.init();
        while (!(state instanceof state.Quit)) {
            state.run();
        }
        Game.destroy();
    }

    public static void destroy() {

        ArrayList<Manager> copy = (ArrayList<Manager>) managers.clone();
        for (Manager m : copy) {
            m.destroy();
        }

        System.exit(0);


    }

    public static void quit() {
        changeState(Quit.getInstance());
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
        return state instanceof EngineInit;
    }

    public static boolean isRunning() {
        return !(state instanceof state.Quit);
    }

    public static void changeState(GameState gstate) {
        state.end();
        state = gstate;
        System.out.println("New State " + state);
        state.init();
    }

    static class EngineInit extends GameState {

        static EngineInit instance;

        public static EngineInit getInstance() {
            if (instance == null) {
                instance = new EngineInit();
            }
            return instance;
        }

        @Override
        public void run() {
            if (game.Game.resourceManager.isLoading()) {
                game.Game.resourceManager.processGraphics();
                Thread.yield();
            } else {
                game.Game.changeState(GameLoading.getInstance());
            }
        }

        @Override
        public void init() {
            gameObjectManager.create();

            updateManager.create();
            updateManager.start();

            resourceManager.create();
            resourceManager.start();

            scriptManager.create();
            graphicsManager.create();
            inputManager.create();
            
            update.UpdateManager.getInstance().setPaused(false);
        }

        @Override
        public void end() {
        }
    }
}
