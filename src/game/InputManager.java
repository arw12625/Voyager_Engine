/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class InputManager implements Manager {

    HashMap<Integer, KeyStatus> map;
    private int x, y;
    private int dx, dy;
    static InputManager instance;

    @Override
    public void create() {
        try {
            //Keyboard
            Keyboard.create();
            map = new HashMap<Integer, KeyStatus>();
            //Mouse
            Mouse.create();
            dx = 0;
            dy = 0;
        } catch (LWJGLException ex) {
            Logger.getLogger(InputManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        DebugMessages.getInstance().write("InputManager created");
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    @Override
    public void destroy() {
        Keyboard.destroy();
        Mouse.destroy();
        DebugMessages.getInstance().write("InputManager destroyed");
    }

    public void processInputs() {
        for (Integer request : map.keySet()) {
            updateKey(request);
        }
        dx = Mouse.getDX();
        dy = Mouse.getDY();
        x = Mouse.getX();
        y = Mouse.getY();

        DebugMessages.getInstance().write("Inputs processed");
    }

    public void updateKey(int key) {
        map.get(key).update(Keyboard.isKeyDown(key));
    }

    public void put(int key) {
        if (!map.containsKey(key)) {
            map.put(key, new KeyStatus(Keyboard.isKeyDown(key)));
        }
    }

    public KeyStatus get(int key) {
        return map.get(key);
    }

    public String keyBoardString() {
        return map.toString();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDX() {
        return this.dx;
    }

    public int getDY() {
        return this.dy;
    }

    public void setGrabbed(boolean isGrabbed) {
        Mouse.setGrabbed(isGrabbed);
    }

    @Override
    public String getName() {
        return "InputManager";
    }

    @Override
    public void update(int delta) {
    }
}