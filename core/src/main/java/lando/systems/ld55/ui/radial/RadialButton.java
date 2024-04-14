package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld55.Main;

public class RadialButton {

    public static float MAX_RADIUS = 40f;

    public String text;
    public TextureRegion textureRegion;
    public Vector2 centerPosition;
    public float radius;
    private BitmapFont font;
    private GlyphLayout layout;


    public RadialButton(TextureRegion region, String text) {
        this.text = text;
        this.textureRegion = region;
        this.centerPosition = new Vector2();
        this.radius = 0;
        font = Main.game.assets.smallFont;
        layout = Main.game.assets.layout;
    }

    public void update(Vector2 centerPosition, float sizePercent, float dt) {
        this.centerPosition.set(centerPosition);
        this.radius = MAX_RADIUS * sizePercent;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.CORAL);
        batch.draw(textureRegion, centerPosition.x - radius, centerPosition.y - radius, radius*2f, radius * 2f);
        batch.setColor(Color.WHITE);

        font.getData().setScale(radius/MAX_RADIUS * .8f);
        layout.setText(font, text, Color.WHITE, radius + radius, Align.center, true);
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
