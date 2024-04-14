package lando.systems.ld55.actions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ActionBase {

    /**
     * Override this to know when it could be removed from the queue
     * @return whether this action is complete and can be removed from the queue
     */
    public abstract boolean isCompleted();

    /**
     * Used to know if the turn has ben completed
     * @return
     */
    public abstract boolean doneTurn();

    public abstract void update(float dt);

    /**
     * Used to reset the action after the queue is cleared
     */
    public abstract void reset();

    public abstract void render(SpriteBatch batch);
}
