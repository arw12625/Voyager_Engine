/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

/**
 *
 * @author Andy
 */
public abstract class GraphicsResource extends Resource{

    private boolean isProcessed;

    public boolean isProcessed() {
        return isProcessed;
    }

    protected void setIsprocessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }
    
    public abstract boolean processGraphics();
    
}
