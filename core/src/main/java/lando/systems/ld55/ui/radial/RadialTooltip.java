package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.entities.Direction;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.Pattern;

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
            assets.font.setColor(Color.WHITE);
            GamePiece.Type type = ((RadialSummonButton) button).pieceType;
            TileOverlayAssets.panelBlue.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
//            Assets.NinePatches.glass_blue.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
            Animation<TextureRegion> charAnimation = assets.getAnimation(type, GamePiece.Owner.Player);
            assets.font.draw(batch, "Summon "+ type.name(), bounds.x, bounds.y + bounds.height - 10, bounds.width, Align.center, true);
            if (charAnimation != null){
                TextureRegion keyframe = charAnimation.getKeyFrame(accum);
                batch.draw(keyframe, bounds.x + bounds.width/2 - 40, bounds.y + bounds.height - 120, 80, 80);
            }
            assets.font.getData().setScale(.5f);
            assets.font.draw(batch, "Cost to summon: "+ type.actionsToSpawn, bounds.x, bounds.y + bounds.height - 125, bounds.width, Align.center, true);
            assets.font.draw(batch, "Actions available: "+ ((RadialSummonButton) button).board.gameScreen.actionManager.playerActionsAvailable, bounds.x, bounds.y + bounds.height - 150, bounds.width, Align.center, true);
            assets.font.draw(batch, "Health: "+ type.defaultMaxHealth, bounds.x, bounds.y + bounds.height - 175, bounds.width, Align.center, true);
            assets.font.getData().setScale(1f);
            renderDamage(batch, damageBounds, type);
            renderMovement(batch, movementBounds, type);
        }

    }

    public void setRadialButton(RadialButton button) {
        this.button = button;
    }

    private void renderDamage(SpriteBatch batch, Rectangle rect, GamePiece.Type type) {
        assets.font.getData().setScale(.5f);
        assets.font.draw(batch, "Damage", rect.x, rect.y + rect.height + 20, rect.width, Align.center, true);
//        batch.draw(assets.pixelRegion, rect.x, rect.y, rect.width, rect.height);
        assets.font.getData().setScale(1f);
        float tileSize = rect.width/7f;
        Pattern damagePattern = null;
        switch(type){
            case Pawn:
                damagePattern = Pattern.PAWN_ATK;
                break;
            case Knight:
                damagePattern = Pattern.KNIGHT_ATK;
                break;
            case Bishop:
                damagePattern = Pattern.BISHOP_ATK;
                break;
            case Rook:
                damagePattern = Pattern.ROOK_ATK;
                break;
            case Queen:
                damagePattern = Pattern.QUEEN_ATK;
                break;
        }
        for (int y = 0; y < 7; y++){
            for (int x = 0; x < 7; x++){
                batch.setColor(.9f, .9f, .9f, .6f);
                Assets.NinePatches.debug.draw(batch, rect.x + (tileSize * x), rect.y + (tileSize *y), tileSize, tileSize);
                batch.setColor(Color.WHITE);
                char ch = damagePattern.vals[y][x];
                if (ch == ' ') continue;
                if (ch == 'x'){
                    batch.setColor(Color.BLUE);
                } else {
                    int damage = 1;
                    if (Character.isDigit(ch)) {
                        damage = Character.digit(ch, 10);
                    }
                    batch.setColor(TileOverlayAssets.getColorForDamageAmount(damage));
                }
                batch.draw(assets.pixelRegion, rect.x + (tileSize * x), rect.y + (tileSize *y), tileSize, tileSize);


            }
        }

    }

    private void renderMovement(SpriteBatch batch, Rectangle rect, GamePiece.Type type) {
        assets.font.getData().setScale(.5f);
        assets.font.draw(batch, "Movement", rect.x, rect.y + rect.height + 20, rect.width, Align.center, true);
        assets.font.getData().setScale(1f);
        float tileSize = rect.width/7f;

        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                batch.setColor(.9f, .9f, .9f, .6f);
                Assets.NinePatches.debug.draw(batch, rect.x + (tileSize * x), rect.y + (tileSize * y), tileSize, tileSize);
                batch.setColor(Color.CLEAR);

                if (x == 3 && y == 3){
                    batch.setColor(Color.BLUE);
                } else if ((type.directionPlayer & Direction.Top) == Direction.Top && x == 3 && y > 3) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.TopRight) == Direction.TopRight && x > 3 && y > 3 && x == y) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.Right) == Direction.Right && x > 3 && y == 3) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.BottomRight) == Direction.BottomRight && x > 3 && y < 3 && x == (6-y)) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.Bottom) == Direction.Bottom && x == 3 && y < 3) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.BottomLeft) == Direction.BottomLeft && x < 3 && y < 3 && x == y) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.Left) == Direction.Left && x < 3 && y == 3) {
                    batch.setColor(Color.RED);
                } else if ((type.directionPlayer & Direction.TopLeft) == Direction.TopLeft && x < 3 && y > 3 && y == (6-x)) {
                    batch.setColor(Color.RED);
                }


                batch.draw(assets.pixelRegion, rect.x + (tileSize * x) + 1, rect.y + (tileSize *y) + 1, tileSize -2, tileSize-2);
                batch.setColor(Color.WHITE);
            }
        }
    }
}
