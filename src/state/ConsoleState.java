/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state;

import script.Console;

/**
 *
 * @author Andy
 */
public class ConsoleState extends Paused {

    static ConsoleState instance;

    public static ConsoleState getInstance() {
        if (instance == null) {
            instance = new ConsoleState();
        }
        return instance;
    }

    @Override
    public void init() {
        Console.getInstance().setEnabled(true);
    }


    @Override
    public void end() {
        Console.getInstance().setEnabled(false);
    }
}
