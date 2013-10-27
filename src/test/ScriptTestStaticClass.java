/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Tanner <thobson125@gmail.com>
 */
public class ScriptTestStaticClass {
    public static int rollDice() {
        return 4;
    }
    
    public static Runnable returnRunnable() {
        return new Runnable() {
            public void run() {
                System.out.println("wow");
            }
        };
    }
}
