package lando.systems.ld55.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionBase;

public class ActionItemUI {
    public ActionBase action;
    public Rectangle bounds;
    public Rectangle targetRectangle;
    public boolean highlight;
    public boolean remove;

    public ActionItemUI(ActionBase action, Rectangle bounds) {
        this.action = action;
        this.bounds = new Rectangle(bounds);
        this.targetRectangle = new Rectangle(bounds);
        this.highlight = false;
        this.remove = false;
    }

    public void update(float dt) {
        // Move Bounds
        bounds.x += (targetRectangle.x - bounds.x) * .1f;
        bounds.y += (targetRectangle.y - bounds.y) * .1f;
        bounds.width += (targetRectangle.width - bounds.width) * .1f;
        bounds.height += (targetRectangle.height - bounds.height) * .1f;
//        bounds.set(targetRectangle);
    }

    public void updateRect(Rectangle r) {
        targetRectangle.set(r);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.LIGHT_GRAY);
        if (highlight){
            batch.setColor(Color.YELLOW);
        }
        batch.draw(Main.game.assets.cardTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
    }
}
