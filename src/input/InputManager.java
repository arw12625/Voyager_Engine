/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import game.GameObject;
import game.Manager;
import java.util.HashMap;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Andy
 */
public class InputManager extends Manager {

    HashMap<Integer, KeyStatus> map;
    private int x, y;
    private int dx, dy;
    private int dWheel;
    static InputManager instance;

    @Override
    public void create() {
        super.create();
        try {
            //Keyboard
            Keyboard.create();
            map = new HashMap<Integer, KeyStatus>();
            //Mouse
            Mouse.create();
            dx = 0;
            dy = 0;
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    @Override
    public void destroy() {
        super.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    public synchronized void processInputs() {
        updateKeyBoard();
        dx = Mouse.getDX();
        dy = Mouse.getDY();
        dWheel = Mouse.getDWheel();
        x = Mouse.getX();
        y = Mouse.getY();

        util.DebugMessages.getInstance().write("Inputs processed");
    }

    public void updateKeyBoard() {
        while (Keyboard.next()) {
            updateKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
        }
    }

    public void updateKey(int key, boolean down) {
        if (map.containsKey(key)) {
            map.get(key).update(down);
        }
    }

    public void put(int key) {
        if (!map.containsKey(key)) {
            map.put(key, new KeyStatus(Keyboard.isKeyDown(key)));
        }
    }

    public KeyStatus getKey(int key) {
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

    public boolean isMouseButtonDown(int button) {
        return Mouse.isButtonDown(button);
    }

    public int getDWheel() {
        return this.dWheel;
    }

    @Override
    public void remove(GameObject obj) {
    }
}