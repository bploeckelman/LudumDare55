package lando.systems.ld55.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Main;

/**
 * Used to keep track of actions that are queued and allow for turns to be taken
 */
public class ActionManager {
    public enum Phase {CollectActions, ResolveActions, Attack}
    private final static int ActionsPerTurn = 3;

    int turnNumber;
    public int playerActionsAvailable;

    Array<ActionBase> actionQueue = new Array<>();
    int currentAction;
    private Phase phase = Phase.CollectActions;


    public ActionManager() {
        turnNumber = 0;
        playerActionsAvailable = ActionsPerTurn;
        phase = Phase.CollectActions;
    }

    public void update(float dt) {
        switch (phase){
            case CollectActions:
                // Idle
                break;
            case ResolveActions:
                if (currentAction >= actionQueue.size){
                    resetActionQueue();
                } else {
                    // we need to resolve this action
                    if (actionQueue.get(currentAction).doneTurn()){
                        Gdx.app.log("ActionManager", "Finished with Action");
                        currentAction++;
                    } else {
                        actionQueue.get(currentAction).update(dt);
                    }
                }
                break;
            case Attack:
                // Do Attacks
                handleAttacks();
                break;
        }
    }

    public void render(SpriteBatch batch) {
        switch (phase){
            case CollectActions:
                for (ActionBase action : actionQueue){
                    if (action instanceof SpawnAction) {
                        SpawnAction spawnAction = (SpawnAction) action;
                        batch.draw(Main.game.assets.particles.ring, spawnAction.spawnTile.bounds.x, spawnAction.spawnTile.bounds.y, spawnAction.spawnTile.bounds.width, spawnAction.spawnTile.bounds.height);
                    }
                }
                break;
            case ResolveActions:
                if (currentAction < actionQueue.size) {
                    actionQueue.get(currentAction).render(batch);
                }
                break;
            case Attack:
                // TODO things to show the attacking
                break;
        }
    }

    public void addAction(ActionBase action) {
        actionQueue.add(action);
    }

    public void removeAction(ActionBase action) {
        action.getPiece().currentAction = null;
        actionQueue.removeValue(action, true);
    }

    /**
     * This moves the game from collection actions to resolving them
     * Add Enemy actions to the queue before calling this
     */
    public void endTurn() {
        if (phase != Phase.CollectActions) {
            Gdx.app.log("ActionManager", "Calling end turn when not in the right phase");
            return;
        }
        Gdx.app.log("ActionManager", "Moving to Action Resolution Phase");
        phase = Phase.ResolveActions;
        currentAction = 0;
    }

    public int actionsRemaining() {
        return playerActionsAvailable;
    }

    public Array<ActionBase> getActionQueue() {
        return actionQueue;
    }

    public Phase getCurrentPhase() {
        return phase;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    private void handleAttacks() {
        // TODO: do the actual attacks of people
        phase = Phase.CollectActions;
    }

    /**
     * Resets the action queue after all actions have been resolved
     */
    private void resetActionQueue() {
        Gdx.app.log("ActionManager", "Finished with Resolving actions, moving to Attack Phase");
        turnNumber++;
        phase = Phase.Attack;
        playerActionsAvailable = ActionsPerTurn;
        for (int i = actionQueue.size-1; i >= 0; i--){
            ActionBase action = actionQueue.get(i);
            action.reset();
            if (action.isCompleted()) {
                actionQueue.removeIndex(i);
            }
        }
    }
}
