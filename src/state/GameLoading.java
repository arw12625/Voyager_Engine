/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state;

import script.ExecutionGroup;
import script.ScriptManager;

/**
 *
 * @author Andy
 */
public class GameLoading extends BasicState {
    
    ExecutionGroup eg;
    boolean firstRun;
    static GameLoading instance;

    public static GameLoading getInstance() {
        if (instance == null) {
            instance = new GameLoading();
        }
        return instance;
    }
    
    @Override
    public void init() {
        super.init();
        String[] paths = {"engine_scripts/startinit.js", "engine_scripts/default.js", "space/GameTest.js"};
        eg = ExecutionGroup.createExecutionGroup(ScriptManager.getInstance().globalScope, paths);
        firstRun = true;
    }
    
    @Override
    public void run() {
        if(firstRun) {
            eg.execute();
            firstRun = false;
        }
        if (game.Game.resourceManager.isLoading() || eg.isRunning()) {
            super.run();
        } else {
            game.Game.changeState(Running.getInstance());
        }
    }
    
    @Override
    public void end() {
    }
}
