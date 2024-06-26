package lando.systems.ld55.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.utils.events.EventType;
import lando.systems.ld55.utils.events.Events;

public class TitleScreenUI {
    float x;
    float y;
    float buttonWidth;
    float buttonHeight;
    Rectangle startGameBound;
    Rectangle settingsBound;
    Rectangle creditBound;
    ButtonOrientation buttonOrientation;
    Button startGameButton;
    Button settingsButton;
    Button creditButton;
    BitmapFont font;

    public enum ButtonOrientation {
        VERTICAL,
        HORIZONTAL
    }
    public TitleScreenUI(float x, float y, float buttonWidth, float buttonHeight, BitmapFont font) {
        this(x, y, buttonWidth, buttonHeight, font, ButtonOrientation.VERTICAL);
    }
    public TitleScreenUI(float x, float y, float buttonWidth, float buttonHeight, BitmapFont font, ButtonOrientation buttonOrientation) {
        var MARGIN = 10f;
        this.x = x;
        this.y = y;
        this.font = font;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        startGameBound = new Rectangle(x, y, buttonWidth, buttonHeight);
        if (buttonOrientation == ButtonOrientation.VERTICAL) {
            settingsBound = new Rectangle(x, y - (buttonHeight + MARGIN), buttonWidth, buttonHeight);
            creditBound = new Rectangle(x, y - (buttonHeight + MARGIN) * 2, buttonWidth, buttonHeight);
        } else {
            settingsBound = new Rectangle(x + (buttonWidth + MARGIN), y, buttonWidth, buttonHeight);
            creditBound = new Rectangle(x + (buttonWidth + MARGIN) * 2, y, buttonWidth, buttonHeight);
        }
        startGameButton = new Button(startGameBound, "Play!", Assets.NinePatches.glass_blue, TileOverlayAssets.panelGreen, font);
        settingsButton = new Button(settingsBound, "Settings", Assets.NinePatches.glass_yellow, TileOverlayAssets.panelGreen, font);
        creditButton = new Button(creditBound, "Credits", Assets.NinePatches.glass_red, TileOverlayAssets.panelGreen, font);

        startGameButton.setOnClickAction(() -> Events.get().dispatch(EventType.TRANSITION_TO_GAME));
        settingsButton.setOnClickAction(() -> Events.get().dispatch(EventType.SHOW_SETTINGS));
        creditButton.setOnClickAction(() -> Events.get().dispatch(EventType.TRANSITION_TO_CREDITS));
    }

    public void update(float x, float y) {

        if (Gdx.input.justTouched()) {
            if (startGameBound.contains(x, y)) {
                startGameButton.onClick();
            } else if (settingsBound.contains(x, y)) {
                settingsButton.onClick();
            } else if (creditBound.contains(x, y)) {
                creditButton.onClick();
            } else {
                Events.get().dispatch(EventType.MEANINGLESS_CLICK, x, y);
            }
        }

        startGameButton.update(x, y);
        settingsButton.update(x, y);
        creditButton.update(x, y);
    }

    public void draw(SpriteBatch batch) {
        startGameButton.draw(batch);
        settingsButton.draw(batch);
        creditButton.draw(batch);
    }
}
