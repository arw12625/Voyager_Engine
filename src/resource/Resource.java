/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.GameObject;

/**
 *
 * @author Andy
 */
public abstract class Resource extends GameObject {

    private boolean isLoaded;

    public boolean isLoaded() {
        return isLoaded;
    }

    protected void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    protected abstract boolean load();
    
}
