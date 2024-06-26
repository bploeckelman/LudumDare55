package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.ui.Button;
import lando.systems.ld55.utils.typinglabel.TypingLabel;

public class CreditScreen extends BaseScreen {

    private final TypingLabel titleLabel;
    private final TypingLabel themeLabel;
    private final TypingLabel leftCreditLabel;
    private final TypingLabel rightCreditLabel;
    private final TypingLabel thanksLabel;
    private final TypingLabel disclaimerLabel;

    private final Animation<TextureRegion> catAnimation;
    private final Animation<TextureRegion> dogAnimation;
    private final Animation<TextureRegion> kittenAnimation;
    private final TextureRegion background;

    private final String title = "{GRADIENT=purple;cyan}Board to Death: Monsters and Monarchs{ENDGRADIENT}";
    private final String theme = "Made for Ludum Dare 55: Summoning";

    private final String thanks = "{GRADIENT=purple;cyan}Thank you for playing our game!{ENDGRADIENT}";
    private final String developers = "{COLOR=gray}Developed by:{COLOR=white}\n {GRADIENT=white;gray}Brian Ploeckelman{ENDGRADIENT} \n {GRADIENT=white;gray}Doug Graham{ENDGRADIENT} \n {GRADIENT=white;gray}Brian Rossman{ENDGRADIENT} \n {GRADIENT=white;gray}Jeffrey Hwang{ENDGRADIENT}";
    private final String artists = "{COLOR=gray}Art by:{COLOR=white}\n {GRADIENT=white;gray}Matt Neumann{ENDGRADIENT}\n {GRADIENT=white;gray}Luke Bain{ENDGRADIENT}\n";
    private final String emotionalSupport = "{COLOR=cyan}Emotional Support:{COLOR=white}\n  Asuka, Osha, Cherry \n       Obi, and Yoda";
    private final String music = "{COLOR=gray}Music and Writing:{COLOR=white}\n {GRADIENT=white;gray}Pete V{ENDGRADIENT}\n";
    private final String libgdx = "Made with {COLOR=red}<3{COLOR=white}\nand {RAINBOW}LibGDX{ENDRAINBOW}";
    private final String disclaimer = "{GRADIENT=black;gray}Disclaimer:{ENDGRADIENT}  {GRADIENT=gold;yellow}{JUMP=.27} No babies were harmed in the making of this game{ENDJUMP}{ENDGRADIENT}";

    private float accum = 0f;
    private boolean showPets = false;

    private Button afterCreditsButton;

    public CreditScreen() {
        super();

        assets.fontZektonSmall.setColor(Color.WHITE);
        assets.fontZektonSmall.getData().setScale(1f);

        titleLabel = new TypingLabel(assets.fontZektonSmall, title, 0f, Config.Screen.window_height - 20f);
        titleLabel.setWidth(Config.Screen.window_width);
        titleLabel.setFontScale(1f);

        themeLabel = new TypingLabel(assets.fontAbandonedMed, theme, 0f, Config.Screen.window_height - 80f);
        themeLabel.setWidth(Config.Screen.window_width);
        themeLabel.setFontScale(1f);

        leftCreditLabel = new TypingLabel(assets.fontZektonSmall, developers.toLowerCase() + "\n\n" + emotionalSupport.toLowerCase() + "\n\n", 75f, Config.Screen.window_height / 2f + 135f);
        leftCreditLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        leftCreditLabel.setLineAlign(Align.left);
        leftCreditLabel.setFontScale(.85f);

        background = assets.pixelRegion;

        rightCreditLabel = new TypingLabel(assets.fontZektonSmall, artists.toLowerCase() + "\n" + music.toLowerCase() + "\n" + libgdx.toLowerCase(), Config.Screen.window_width / 2 + 75f, Config.Screen.window_height / 2f + 135f);
        rightCreditLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        rightCreditLabel.setLineAlign(Align.left);
        rightCreditLabel.setFontScale(.85f);

        thanksLabel = new TypingLabel(assets.fontZektonSmall, thanks, 0f, 105f);
        thanksLabel.setWidth(Config.Screen.window_width);
        thanksLabel.setLineAlign(Align.center);
        thanksLabel.setFontScale(.85f);

        disclaimerLabel = new TypingLabel(assets.fontZektonSmall, disclaimer, 0f, 50f);
        disclaimerLabel.setWidth(Config.Screen.window_width);
        thanksLabel.setLineAlign(Align.center);
        disclaimerLabel.setFontScale(.5f);

        catAnimation = assets.cherry;
        dogAnimation = assets.asuka;
        kittenAnimation = assets.osha;
        afterCreditsButton = new Button(new Rectangle(worldCamera.viewportWidth - 300f, 0f, 300, 50), "Scrapped Ideas", Assets.NinePatches.glass_yellow, Assets.NinePatches.glass, assets.fontZektonSmall);

        Main.game.audioManager.playMusic(AudioManager.Musics.outroMusic);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    @Override
    public void update(float dt) {
        if (exitingScreen) { return; }
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        windowCamera.unproject(mousePos);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isTouched()) {
            if (afterCreditsButton.getBounds().contains(mousePos.x, mousePos.y)) {
                game.setScreen(new AfterCreditScreen());
                exitingScreen = true;
                return;
            }
            var allDone = titleLabel.hasEnded() && themeLabel.hasEnded() && leftCreditLabel.hasEnded() && rightCreditLabel.hasEnded() && thanksLabel.hasEnded() && disclaimerLabel.hasEnded();
            Gdx.app.log("CreditScreen", "allDone: " + allDone);
            if (allDone) {
                game.setScreen(new TitleScreen());
                exitingScreen = true;
                return;
            } else {
                titleLabel.skipToTheEnd();
                themeLabel.skipToTheEnd();
                leftCreditLabel.skipToTheEnd();
                rightCreditLabel.skipToTheEnd();
                thanksLabel.skipToTheEnd();
                disclaimerLabel.skipToTheEnd();
                showPets = true;
                return;
            }
        }
        accum += dt;
        titleLabel.update(dt);
        themeLabel.update(dt);
        leftCreditLabel.update(dt);
        rightCreditLabel.update(dt);
        thanksLabel.update(dt);
        disclaimerLabel.update(dt);
        afterCreditsButton.update(mousePos.x, mousePos.y);
    }

    @Override
    public void renderFrameBuffers(SpriteBatch batch) {

    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(.0f, .0f, .1f, 1f);

        batch.enableBlending();
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            //batch.draw(background, 0, 0, Config.Screen.window_width, Config.Screen.window_height);

            batch.setColor(0f, 0f, 0f, .6f);
            batch.draw(assets.pixelRegion, 25f, 130f, Config.Screen.window_width / 2f - 50f, 400f);
            batch.draw(assets.pixelRegion, Config.Screen.window_width / 2f + 25f, 130f, Config.Screen.window_width / 2f - 50f, 400f);

            batch.setColor(Color.WHITE);
            titleLabel.render(batch);
            themeLabel.render(batch);
            leftCreditLabel.render(batch);
            rightCreditLabel.render(batch);
            thanksLabel.render(batch);
            disclaimerLabel.render(batch);
            if (accum > 7.5 || showPets) {
                TextureRegion cherryTexture = assets.cherry.getKeyFrame(accum);
                TextureRegion asukaTexture = assets.asuka.getKeyFrame(accum);
                TextureRegion oshaTexture = assets.osha.getKeyFrame(accum);
                batch.draw(oshaTexture, 450f, 175f);
                batch.draw(asukaTexture, 500f, 175f);
                batch.draw(cherryTexture, 550f, 175f);
            }
            if (accum > 8.5 || showPets) {
                TextureRegion obiTexture = assets.obi.getKeyFrame(accum);
                TextureRegion yodaTexture = assets.yoda.getKeyFrame(accum);
                batch.draw(obiTexture, 475f, 125f);
                batch.draw(yodaTexture, 525f, 125f);
            }
            batch.setColor(Color.WHITE);
            afterCreditsButton.draw(batch);
        }
        batch.end();
    }

}
