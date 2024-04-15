package lando.systems.ld55.actions;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Main;
import lando.systems.ld55.Stats;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.TileOverlayInfo;
import lando.systems.ld55.screens.GameScreen;
import lando.systems.ld55.ui.MovementBreadcrumb;

import java.util.List;

/**
 * Used to keep track of actions that are queued and allow for turns to be taken
 */
public class ActionManager {
    public static final float AttackSpeed = 1f;
    public enum Phase {CollectActions, ResolveActions, Attack}
    public static final int MIN_ACTION_POINTS = 3;
    public static final int MAX_ACTION_POINTS = 10;
    public static int ActionsPerTurn = MIN_ACTION_POINTS;


    int turnNumber;
    public int playerActionsAvailable;
    public int tempActionPoints;

    Array<ActionBase> actionQueue = new Array<>();
    public int currentAction;
    private Phase phase = Phase.CollectActions;
    private GameScreen gameScreen;
    private float attackAccum;

    private Array<GamePiece> attackingUnits = new Array<>();


    public ActionManager(GameScreen screen) {
        turnNumber = 0;
        playerActionsAvailable = ActionsPerTurn;
        tempActionPoints = playerActionsAvailable;
        phase = Phase.CollectActions;
        this.gameScreen = screen;
    }

    public void update(float dt) {
        ActionsPerTurn = MIN_ACTION_POINTS + (Stats.enemyUnitsKilled - Stats.playerUnitsKilled);
        ActionsPerTurn = MathUtils.clamp(ActionsPerTurn, MIN_ACTION_POINTS, MAX_ACTION_POINTS);
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
                handleAttacks(dt);
                break;
        }
    }

    public void render(SpriteBatch batch) {
        switch (phase){
            case CollectActions:
//                for (ActionBase action : actionQueue){
//                    if (action instanceof SpawnAction) {
//                        SpawnAction spawnAction = (SpawnAction) action;
//                        batch.draw(Main.game.assets.particles.ring, spawnAction.spawnTile.bounds.x, spawnAction.spawnTile.bounds.y, spawnAction.spawnTile.bounds.width, spawnAction.spawnTile.bounds.height);
//                    }
//                }
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

    public void tempActionPointsUsed(int value) {
        this.tempActionPoints = playerActionsAvailable - value;
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
        // TODO: Sound for end turn and action resolution starts
        phase = Phase.ResolveActions;
        currentAction = 0;
        currentAttackerPiece = 0;
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

    int currentAttackerPiece;
    Array<GamePiece> attackablePieces = new Array<>();
    private void handleAttacks(float dt) {
        if (currentAttackerPiece >= attackingUnits.size) {
            // TODO: Sound when the player can act again
            phase = Phase.CollectActions;
            playerActionsAvailable = ActionsPerTurn;
            tempActionPoints = playerActionsAvailable;
            gameScreen.gameBoard.turnTime = 0;
            return;
        }

        attackAccum += dt;

        // TODO: do the actual attacks of people
        if (attackAccum >= AttackSpeed){
            Gdx.app.log("ActionManager", "Making an attack for index: " + currentAttackerPiece);
            GamePiece attackPiece = attackingUnits.get(currentAttackerPiece);
            if (attackPiece.isDead()) {
                currentAttackerPiece++;
                return;
            }
            int maxDamage = 0;
            attackablePieces.clear();
            List<TileOverlayInfo> attackTiles = gameScreen.gameBoard.getAttackTileOverlays(attackPiece.currentTile, attackPiece.pattern);
            for (GamePiece p : gameScreen.gameBoard.gamePieces){
                if (p.owner != attackPiece.owner && !p.isDead()){
                    for (TileOverlayInfo tileInfo : attackTiles){
                        if (tileInfo.tile == p.currentTile && maxDamage < tileInfo.damageAmount){
                            attackablePieces.add(p);
                            maxDamage = tileInfo.damageAmount;
                        }
                    }
                }
            }
            currentAttackerPiece++;
            if (attackablePieces.size > 0){
                attackAccum = 0;
                final GamePiece damagedPiece = attackablePieces.random();
                attackPiece.attack(damagedPiece);

                int finalMaxDamage = maxDamage;
                Timeline.createSequence()
                    .pushPause(.2f)
                    .push(Tween.call(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            damagedPiece.takeDamage(finalMaxDamage, gameScreen.gameBoard);
                            damagedPiece.bleed();
                            Main.game.audioManager.playSound(AudioManager.Sounds.combat_hit, .7f); //placeholder sound
                        }
                    }))
                    .start(Main.game.tween);
            }

        }


    }

    Array<Array<MovementBreadcrumb>> breadcrumbLists = new Array<>();
    public Array<Array<MovementBreadcrumb>> getActiveMoveLists() {
        breadcrumbLists.clear();
        for (ActionBase action : actionQueue) {
            if (action instanceof MoveAction){
                var moveAction = (MoveAction) action;
                moveAction.walkBreadCrumbChain();
                breadcrumbLists.add(moveAction.breadcrumbArray);
            }
        }
        return breadcrumbLists;
    }

    /**
     * Resets the action queue after all actions have been resolved
     */
    private void resetActionQueue() {
        Gdx.app.log("ActionManager", "Finished with Resolving actions, moving to Attack Phase");
        turnNumber++;
        // TODO: Sound when it moves to Attack phase
        phase = Phase.Attack;
        for (int i = actionQueue.size-1; i >= 0; i--){
            ActionBase action = actionQueue.get(i);
            action.reset();
            if (action.isCompleted()) {
                actionQueue.removeIndex(i);
            }
        }
        attackingUnits.clear();
        for (GamePiece playerPiece : gameScreen.gameBoard.gamePieces) {
            if (playerPiece.owner == GamePiece.Owner.Player){
                attackingUnits.add(playerPiece);
            }
        }
        for (GamePiece enemyPiece : gameScreen.gameBoard.gamePieces) {
            if (enemyPiece.owner == GamePiece.Owner.Enemy) {
                attackingUnits.add(enemyPiece);
            }
        }
    }
}
