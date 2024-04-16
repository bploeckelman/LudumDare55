package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.TileOverlayAssets;

public class RadialButton {

    public static float MAX_RADIUS = 35f;

    public Color iconEnabledColor = new Color(.1f,.6f,.2f,1);
    public Color iconDisabledColor = new Color(.4f, .4f, .4f, .81f); // alpha .31 maybe
    public Color iconMetaColor = Color.WHITE.cpy();
    public Color backgroundEnabledColor = Color.LIGHT_GRAY.cpy();

    public String text;
    public float iconRadiusScale = 0.9f;
    public float iconOffsetX = 0f;
    public float iconOffsetY = 0f;
    public TextureRegion icon;
    public TextureRegion iconMeta;
    public TextureRegion iconCost;
    public NinePatch background;
    public NinePatch backgroundEnabled;
    public NinePatch backgroundDisabled;
    public NinePatch backgroundHovered;
    public Vector2 centerPosition;
    public float radius;
    public boolean hovered;
    private BitmapFont font;
    private GlyphLayout layout;
    public boolean enabled;
    public int pointsUsed = 0;


    public RadialButton(NinePatch background, TextureRegion icon, String text, boolean enabled) {
        this.backgroundEnabled = background;
        this.backgroundDisabled = TileOverlayAssets.panelWhite;
//        this.backgroundDisabled = TileOverlayAssets.panelRed;
        this.backgroundHovered = TileOverlayAssets.panelBlue;
        this.background = backgroundEnabled;
        this.icon = icon;
        this.text = text;
        this.centerPosition = new Vector2();
        this.radius = 0;
        this.font = Main.game.assets.fontZektonSmall;
//        this.font = Main.game.assets.smallFont;
        this.layout = Main.game.assets.layout;
        this.enabled = enabled;
        this.hovered = false;
    }

    public void update(Vector2 centerPosition, float sizePercent, float dt) {
        this.centerPosition.set(centerPosition);
        this.radius = MAX_RADIUS * sizePercent;
    }

    public void render(SpriteBatch batch) {
        if (!enabled) {
            // NOTE(brian) - should probably always be TileOverlayAssets.panelWhite for tinting
            background = backgroundDisabled;
            batch.setColor(Color.DARK_GRAY);
        } else if (hovered) {
            background = backgroundHovered;
        } else {
            // NOTE(brian) - should probably always be TileOverlayAssets.panelWhite for tinting
            background = backgroundEnabled;
//            batch.setColor(Color.LIGHT_GRAY);
            batch.setColor(backgroundEnabledColor);
        }
        background.draw(batch,
            centerPosition.x - radius,
            centerPosition.y - radius,
            radius * 2f, radius * 2f);
        batch.setColor(Color.WHITE);

        // very dumb, for special case handling that should be in the button subclasses
        var summonBtn = (this instanceof RadialSummonButton) ? ((RadialSummonButton) this) : null;

        // draw cost overlay icon thing
        // TODO(brian) - needs more magic numbers
        if (iconCost != null) {
            var margin = 5f;
            var metaRadius = radius * 0.33f;

            // draw background
//            var panel = TileOverlayAssets.panelWhite;
//            batch.setColor(Color.LIGHT_GRAY);
//            panel.draw(batch,
//                centerPosition.x - radius + 2,
//                centerPosition.y - radius + 2,// + 2.5f * metaRadius,
//                3 * metaRadius, 2.5f * metaRadius);
//            batch.setColor(Color.WHITE);

            // draw $ icon
            var dollarIconRadius = radius * 0.3f;
            var dollarIcon = TileOverlayAssets.dollarOutline;
            batch.setColor(enabled ? Color.GOLD : Color.DARK_GRAY);
            batch.draw(dollarIcon,
                centerPosition.x - radius + 2,
                centerPosition.y - radius + margin + 1,
                2 * dollarIconRadius, 2 * dollarIconRadius);

            // draw dollar amount
            var dollarRadius = radius * 0.6f;
            var canPay = enabled;
            if (summonBtn != null) {
                var actionsCost = summonBtn.pieceType.actionsToSpawn;
                var actionsAvailable = summonBtn.board.gameScreen.actionManager.playerActionsAvailable;
                canPay = enabled && actionsCost <= actionsAvailable;
            }
            batch.setColor(canPay ? Color.GOLD : Color.DARK_GRAY);
            batch.draw(iconCost,
                centerPosition.x - radius - 5,
                centerPosition.y - radius + 3,
                2 * dollarRadius, 2 * dollarRadius);

            batch.setColor(Color.WHITE);
        }

        // portrait (for summon)
        var iconRadius = radius * iconRadiusScale;
        var iconColor = enabled ? iconEnabledColor : iconDisabledColor;
        var iconOffsetY2 = (summonBtn != null) ? iconRadius / 2f : iconRadius;
        batch.setColor(iconColor);
        batch.draw(icon,
            centerPosition.x - iconOffsetX - iconRadius,
            centerPosition.y - iconOffsetY - iconOffsetY2,
            iconRadius * 2f, iconRadius * 2f);
        batch.setColor(Color.WHITE);

        if (iconMeta != null) {
            var margin = 5f;
            var metaRadius = radius * 0.33f;
            batch.setColor(enabled ? iconMetaColor : Color.DARK_GRAY);
            batch.draw(iconMeta,
                centerPosition.x + radius - margin - 2 * metaRadius,
                centerPosition.y - radius + margin,
                2 * metaRadius, 2 * metaRadius);
            batch.setColor(Color.WHITE);
        }

        // TODO(brian) - rework text rendering
        float textWidth = radius + radius - 10;
        if (!text.isEmpty()) {
            var fontData = font.getData();
            fontData.setScale(radius / MAX_RADIUS);
            layout.setText(font, text, Color.WHITE, textWidth, Align.center, false);

            var scale = (radius / MAX_RADIUS * textWidth) / layout.width;
            fontData.setScale(scale);
            layout.setText(font, text, Color.WHITE, textWidth, Align.center, false);

//            font.setColor(Color.BLACK);
//            font.draw(batch, text,
//                centerPosition.x - radius + 2,
//                centerPosition.y + layout.height / 2f - 2,
//                radius + radius,
//                Align.center, true);

            font.setColor(Color.WHITE);
            font.draw(batch, text,
                centerPosition.x - radius,
                centerPosition.y + layout.height / 2f,
                radius + radius,
                Align.center, true);

            fontData.setScale(1f);
        }

        batch.setColor(Color.WHITE);
    }

    Vector2 click = new Vector2();
    public boolean inButton(float worldX, float worldY) {
        click.set(worldX, worldY);
        click.sub(centerPosition);
        return click.len2() < radius * radius;
    }

    public void onClick() {
        // Needs to be overriden
        Gdx.app.log("Radial Button", "Someone forgot to override onClick");
    }
}
