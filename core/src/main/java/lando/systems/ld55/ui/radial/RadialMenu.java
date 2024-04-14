package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GameTile;

import javax.imageio.stream.ImageOutputStream;

public class RadialMenu {
    public static float TIME_TO_OPEN = .4f;
    public static float MENU_RADIUS = 90f;
    public enum MenuType {Summon, CancelMove}

    private float currentProgress;
    private float targetProgress;
    private GameBoard board;
    private MenuType type;
    private GameTile tile;

    private Array<RadialButton> buttons;
    private Interpolation interpolation = Interpolation.swingOut;

    public RadialMenu(GameBoard board, GameTile tile, MenuType type) {
        buttons = new Array<>();
        targetProgress = 1f;
        currentProgress = 0;
        this.board = board;
        this.tile = tile;
        this.type = type;
        buttons.add(new RadialCloseButton(this));
        switch (type) {
            case Summon:
                break;
            case CancelMove:
                buttons.add(new RadialCloseButton(this));
                buttons.add(new RadialCloseButton(this));
                break;
        }
    }

    public void exitMenu() {
        targetProgress = 0;
    }

    public boolean menuComplete() {
        return targetProgress == 0 && targetProgress == currentProgress;
    }

    Vector2 tempVec2 = new Vector2();
    Vector2 centerVec2 = new Vector2();
    public void update(float dt) {
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
    }

    Vector3 mosPos = new Vector3();
    public boolean handleClick(float screenX, float screenY) {
        if (currentProgress != 1f) return false;
        mosPos.set(screenX, screenY, 0);
        board.gameScreen.worldCamera.unproject(mosPos);

        for (RadialButton button : buttons) {
            if (button.inButton(mosPos.x, mosPos.y)){
                button.onClick();
                return true;
            }
        }

        // should we close?
        mosPos.sub(tile.center.x, tile.center.y, 0);
        if (mosPos.len2() > MENU_RADIUS * MENU_RADIUS) {
            exitMenu();
            return true;
        }


        return false;
    }

    public void render(SpriteBatch batch) {
        for (RadialButton button : buttons) {
            button.render(batch);
        }
    }
}
