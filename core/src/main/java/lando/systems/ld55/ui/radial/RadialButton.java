package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld55.Main;

public class RadialButton {

    public static float MAX_RADIUS = 35f;

    public String text;
    public TextureRegion icon;
    public NinePatch background;
    public Vector2 centerPosition;
    public float radius;
    private BitmapFont font;
    private GlyphLayout layout;
    public boolean enabled;


    public RadialButton(NinePatch background, TextureRegion icon, String text, boolean enabled) {
        this.text = text;
        this.background = background;
        this.icon = icon;
        this.centerPosition = new Vector2();
        this.radius = 0;
        font = Main.game.assets.fontZektonSmall;
        layout = Main.game.assets.layout;
        this.enabled = enabled;
    }

    public void update(Vector2 centerPosition, float sizePercent, float dt) {
        this.centerPosition.set(centerPosition);
        this.radius = MAX_RADIUS * sizePercent;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        background.draw(batch,centerPosition.x - radius, centerPosition.y - radius, radius*2f, radius * 2f);
        batch.setColor(1, 1, 1, 1);

        if (enabled) {
            batch.setColor(Color.LIME);
        } else {
            batch.setColor(.4f, 0, 0, 1f);
        }

        var iconRadius = radius * 0.75f;
        batch.draw(icon, centerPosition.x - iconRadius, centerPosition.y - iconRadius, iconRadius*2f, iconRadius * 2f);
        batch.setColor(Color.WHITE);

        float textWidth = radius + radius - 10;

        font.getData().setScale(radius/MAX_RADIUS);
        layout.setText(font, text, Color.WHITE, textWidth, Align.center, false);
        font.getData().setScale((radius/MAX_RADIUS * textWidth)/layout.width);
        layout.setText(font, text, Color.WHITE, textWidth, Align.center, false);
        font.draw(batch, text, centerPosition.x - radius, centerPosition.y + layout.height/2f, radius +radius, Align.center, true);
        font.getData().setScale(1f);
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
