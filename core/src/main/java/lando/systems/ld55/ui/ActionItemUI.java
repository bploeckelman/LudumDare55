package lando.systems.ld55.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.entities.GamePiece;

public class ActionItemUI {
    public ActionBase action;
    public Rectangle bounds;
    public Rectangle targetRectangle;
    public boolean highlight;
    public boolean remove;
    private final GamePiece piece;
    private float animState = 0;
    private TextureRegion portrait;

    public ActionItemUI(ActionBase action, Rectangle bounds) {
        this.action = action;
        this.bounds = new Rectangle(bounds);
        this.targetRectangle = new Rectangle(bounds);
        this.highlight = false;
        this.remove = false;
        this.piece = action.getPiece();
    }

    public void update(float dt) {
        animState += dt;

        // Move Bounds
        bounds.x += (targetRectangle.x - bounds.x) * .1f;
        bounds.y += (targetRectangle.y - bounds.y) * .1f;
        bounds.width += (targetRectangle.width - bounds.width) * .1f;
        bounds.height += (targetRectangle.height - bounds.height) * .1f;
//        bounds.set(targetRectangle);
        if (piece != null) {
            portrait = this.piece.portrait.getKeyFrame(animState);
        }
    }

    public void updateRect(Rectangle r) {
        targetRectangle.set(r);
    }

    public void render(SpriteBatch batch) {
        if (piece == null) { return; } //??

        batch.setColor(piece.owner.color);
        if (highlight){
            batch.setColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, 0.7f);
        }
        TileOverlayAssets.panelGreen.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        batch.setColor(Color.WHITE);

        float qw = bounds.width / 16;
        float qh = bounds.height / 16;
        float w = bounds.width - qw * 2;
        float h = bounds.height - qh * 2;
        float flip = piece.owner == GamePiece.Owner.Enemy ? -1f : 1f;
        batch.draw(portrait, bounds.x + qw, bounds.y + qh, w / 2, h / 2, w, h, flip, 1f, 0);


    }
}
