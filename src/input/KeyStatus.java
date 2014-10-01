/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

/**
 *
 * @author Andy
 */
public class KeyStatus {

    boolean pressed, oldpressed;

    public KeyStatus(boolean pressed) {
        this.oldpressed = false;
        this.pressed = pressed;
    }

    public boolean isUp() {
        return !this.pressed;
    }

    public boolean isDown() {
        return this.pressed;
    }

    public boolean isPressed() {
        boolean tmp = oldpressed;
        oldpressed = pressed;
        return !tmp && pressed;
    }

    public void update(boolean pressed) {
        this.oldpressed = this.pressed;
        this.pressed = pressed;
    }

    public String toString() {
        return "{" + this.oldpressed + ", " + this.pressed + "}";
    }

}