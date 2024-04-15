package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.entities.GamePiece;

public class RadialTooltip {

    RadialButton button;
    Rectangle bounds;
    Rectangle damageBounds;
    Rectangle movementBounds;
    Assets assets;
    private float accum;

    public RadialTooltip() {
        assets = Main.game.assets;
        bounds = new Rectangle(500, 200, 400, 400);
        damageBounds = new Rectangle(bounds.x +30, bounds.y + 20, bounds.width/2f - 60, bounds.height/2f - 60);
        movementBounds = new Rectangle(bounds.x + bounds.width/2f + 30, bounds.y + 20, bounds.width/2f - 60, bounds.height/2f -60);
        accum = 0;
    }

    public void update(float dt) {
        accum += dt;
    }

    public void render(SpriteBatch batch) {
        if (button instanceof RadialSummonButton) {
            GamePiece.Type type = ((RadialSummonButton) button).pieceType;
            Assets.NinePatches.glass_blue.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
            Animation<TextureRegion> charAnimation = assets.getAnimation(type, GamePiece.Owner.Player);
            assets.font.draw(batch, "Summon "+ type.name(), bounds.x, bounds.y + bounds.height - 10, bounds.width, Align.center, true);
            if (charAnimation != null){
                TextureRegion keyframe = charAnimation.getKeyFrame(accum);
                batch.draw(keyframe, bounds.x + bounds.width/2 - 40, bounds.y + bounds.height - 120, 80, 80);
            }
            assets.font.getData().setScale(.6f);
            assets.font.draw(batch, "Cost to summon: "+ type.actionsToSpawn, bounds.x, bounds.y + bounds.height - 130, bounds.width, Align.center, true);
            assets.font.draw(batch, "Actions available: "+ ((RadialSummonButton) button).board.gameScreen.actionManager.playerActionsAvailable, bounds.x, bounds.y + bounds.height - 160, bounds.width, Align.center, true);
            assets.font.getData().setScale(1f);
            renderDamage(batch, damageBounds);
            renderMovement(batch, movementBounds);
        }

    }

    public void setRadialButton(RadialButton button) {
        this.button = button;
    }

    private void renderDamage(SpriteBatch batch, Rectangle rect) {
        assets.font.getData().setScale(.5f);
        assets.font.draw(batch, "Damage", rect.x, rect.y + rect.height + 20, rect.width, Align.center, true);
//        batch.draw(assets.pixelRegion, rect.x, rect.y, rect.width, rect.height);
        assets.font.getData().setScale(1f);
        float tileSize = rect.width/7f;

    }

    private void renderMovement(SpriteBatch batch, Rectangle rect) {
        assets.font.getData().setScale(.5f);
        assets.font.draw(batch, "Movement", rect.x, rect.y + rect.height + 20, rect.width, Align.center, true);
//        batch.draw(assets.pixelRegion, rect.x, rect.y, rect.width, rect.height);
        assets.font.getData().setScale(1f);
    }
}
