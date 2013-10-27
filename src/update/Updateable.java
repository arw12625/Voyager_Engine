/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

/**
 *
 * @author Andy
 */
public interface Updateable {
    /* The boolean stands for if it should or shouldn't remove the object from
     * the update queue. true means "remove", false means "keep it".
     */
    public boolean update(int delta);
}
