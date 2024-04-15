package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class RadialMenu {
    public static float TIME_TO_OPEN = .4f;
    public static float MENU_RADIUS = 90f;
    public enum MenuType {Summon, CancelMove, Move}

    private float currentProgress;
    private float targetProgress;
    private GameBoard board;
    private MenuType type;
    public GameTile tile;

    private Array<RadialButton> buttons;
    private Interpolation interpolation = Interpolation.swingOut;

    public RadialMenu(GameBoard board, GameTile tile, GamePiece piece, MenuType type) {
        buttons = new Array<>();
        targetProgress = 1f;
        currentProgress = 0;
        this.board = board;
        this.tile = tile;
        this.type = type;
        buttons.add(new RadialCloseButton(this));
        switch (type) {
            case Summon:
                buttons.add(new RadialSummonButton(board, tile, GamePiece.Type.Pawn));
                buttons.add(new RadialSummonButton(board, tile, GamePiece.Type.Knight));
                buttons.add(new RadialSummonButton(board, tile, GamePiece.Type.Rook));
                buttons.add(new RadialSummonButton(board, tile, GamePiece.Type.Bishop));
                buttons.add(new RadialSummonButton(board, tile, GamePiece.Type.Queen));
                break;
            case CancelMove:
                buttons.add(new RadialCancelMoveButton(board, piece, tile));
                break;
            case Move:
                buttons.add(new RadialConfirmMoveButton(board, piece, tile));
                break;
        }
    }

    public void exitMenu() {
        targetProgress = 0;
        if (board.selectedPiece != null) {
            board.selectedPiece.deselect(board);
        }
        board.selectedPiece = null;
    }

    public boolean menuComplete() {
        return targetProgress == 0 && targetProgress == currentProgress;
    }

    Vector2 tempVec2 = new Vector2();
    Vector2 centerVec2 = new Vector2();
    public void update(float dt) {
        board.gameScreen.actionManager.tempActionPointsUsed(0);
        float windowDelta = dt/TIME_TO_OPEN;
        if (targetProgress < currentProgress) {
            // closing
            if (targetProgress > currentProgress - windowDelta) {
                currentProgress = targetProgress;
            } else {
                currentProgress -= windowDelta;
            }
        } else if (targetProgress > currentProgress) {
            // opening
            if (targetProgress < currentProgress + windowDelta) {
                currentProgress = targetProgress;
            } else {
                currentProgress += windowDelta;
            }
        }

        centerVec2 = tile.bounds.getCenter(centerVec2);
        float dTheta = 360f / buttons.size;
        for (int i = 0; i < buttons.size; i++) {
            RadialButton b = buttons.get(i);
            tempVec2.set(1, 0);
            tempVec2.scl(interpolation.apply(currentProgress) * MENU_RADIUS);
            tempVec2.rotateDeg((-90*interpolation.apply(currentProgress)) + (i*dTheta));
            tempVec2.add(centerVec2);
            b.update(tempVec2, currentProgress, dt);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            exitMenu();
        }

        if (targetProgress == 1 && currentProgress == 1) {
            mosPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            board.gameScreen.worldCamera.unproject(mosPos);
            for (RadialButton button : buttons) {
                if (button.inButton(mosPos.x, mosPos.y)) {
                    if (button.enabled) {
                        board.gameScreen.actionManager.tempActionPointsUsed(button.pointsUsed);
                    }
                }
            }
        }

    }

    Vector3 mosPos = new Vector3();
    public boolean handleClick(float screenX, float screenY) {
        if (currentProgress != 1f) return false;
        mosPos.set(screenX, screenY, 0);
        board.gameScreen.worldCamera.unproject(mosPos);

        for (RadialButton button : buttons) {
            if (!button.enabled) continue;

            if (button.inButton(mosPos.x, mosPos.y)){
                button.onClick();
                exitMenu();
                return true;
            }
        }

        // should we close?
        mosPos.sub(tile.center.x, tile.center.y, 0);
        if (mosPos.len2() > MENU_RADIUS * MENU_RADIUS) {
            exitMenu();
            return true;
        }


        return true;
    }

    public void render(SpriteBatch batch) {
        for (RadialButton button : buttons) {
            button.render(batch);
        }
    }
}
