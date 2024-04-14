package lando.systems.ld55.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class StyleManager {

    private final Array<StylePiece> stylePieces = new Array<>();

    public void add(Animation<TextureRegion> animation, float x, float y) {
        add(animation, x, y, -1);
    }

    public void add(Animation<TextureRegion> animation, float x, float y, float animState) {
        stylePieces.add(new StylePiece(animation, x, y, animState));
    }

    public void update(float dt) {
        for (var piece : stylePieces) {
            piece.update(dt);
        }

        if (Gdx.input.justTouched()) {
            Gdx.app.log("pos", "(" + Gdx.input.getX() + ", " + Gdx.input.getY() + ")");
        }
    }

    public void render(SpriteBatch batch) {
        for (var piece : stylePieces) {
            piece.render(batch);
        }
    }
}
