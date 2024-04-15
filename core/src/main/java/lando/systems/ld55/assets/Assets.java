package lando.systems.ld55.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import lando.systems.ld55.Config;
import lando.systems.ld55.entities.GamePiece;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

public class Assets implements Disposable {

    public enum Load { ASYNC, SYNC }

    public static final String prefsName = "ld55_summoning";
    public Preferences preferences;

    public boolean initialized;

    public SpriteBatch batch;
    public ShapeDrawer shapes;
    public GlyphLayout layout;
    public AssetManager mgr;
    public TextureAtlas atlas;
    public I18NBundle strings;
    public InputPrompts inputPrompts;
    public Particles particles;

    public BitmapFont font;
    public BitmapFont smallFont;
    public BitmapFont largeFont;
    public BitmapFont fontAbandoned;
    public BitmapFont fontAbandonedMed;
    public BitmapFont fontZektonSmall;
    public BitmapFont fontTreasureMap;

    public Texture pixel;
    public Texture gdx;
    public Texture title;
    public Texture noiseTexture;
    public Texture whitePixel;
    public Texture levelLayout;
    public Texture introBackground;
    public Texture parchment;

    public TextureRegion pixelRegion;
    public TextureRegion closeButton;
    public TextureRegion cardTexture;

    public Animation<TextureRegion> cherry;
    public Animation<TextureRegion> yoda;
    public Animation<TextureRegion> osha;
    public Animation<TextureRegion> asuka;
    public Animation<TextureRegion> obi;
    public Animation<TextureRegion> spawnEvilIdle;
    public Animation<TextureRegion> spawnEvilActive;
    public Animation<TextureRegion> spawnGoodIdle;
    public Animation<TextureRegion> spawnGoodActive;
    public Animation<TextureRegion> candle;
    public Animation<TextureRegion> candleEvil;
    public List<Animation<TextureRegion>> numbers;

    // random shit
    public Animation<TextureRegion> babe1;
    public Animation<TextureRegion> babe2;
    public Animation<TextureRegion> organGrinder;

    public Array<Array<Animation<TextureRegion>>> king = new Array<>();
    public Array<Array<Animation<TextureRegion>>> pawn = new Array<>();;
    public Array<Array<Animation<TextureRegion>>> knight = new Array<>();
    public Array<Array<Animation<TextureRegion>>> bishop = new Array<>();
    public Array<Array<Animation<TextureRegion>>> rook = new Array<>();
    public Array<Array<Animation<TextureRegion>>> queen = new Array<>();

    public Array<Animation<TextureRegion>> thrones = new Array<>();

    public ShaderProgram portalShader;

    public Array<ShaderProgram> randomTransitions;
    public ShaderProgram starWarsShader;
    public ShaderProgram blindsShader;
    public ShaderProgram fadeShader;
    public ShaderProgram radialShader;
    public ShaderProgram doomShader;
    public ShaderProgram pixelizeShader;
    public ShaderProgram doorwayShader;
    public ShaderProgram crosshatchShader;
    public ShaderProgram rippleShader;
    public ShaderProgram heartShader;
    public ShaderProgram stereoShader;
    public ShaderProgram circleCropShader;
    public ShaderProgram cubeShader;
    public ShaderProgram simpleZoomShader;
    public ShaderProgram dreamyShader;
    public ShaderProgram flameShader;
    public ShaderProgram cooldownShader;
    public ShaderProgram influencerShader;
    public ShaderProgram goalShader;
    public ShaderProgram fogOfWarShader;
    public ShaderProgram plasmaShader;
    public ShaderProgram fogObjectShader;
    public ShaderProgram starfieldShader;
    public ShaderProgram minimapShader;
    public ShaderProgram wormholeShader;
    public ShaderProgram gridShader;

    public Sound coin;
    public Sound idleClick;
    public Sound click;
    public Sound lockIn;
    public Sound select;
    public Sound metalTap;
    public Sound pew;
    public Sound errorThud;
    public Sound errorBuzz;
    public Sound levelUp;
    public Sound hornFanfare;
    public Sound hit1;
    public Sound hit2;
    public Sound hit3;
    public Sound hit4;
    public Sound hit5;
    public Sound hit6;

    public Music introMusic;
    public Music mainMusic;
    public Music outroMusic;

    public enum Patch {
        debug, panel, metal, glass, outline,
        glass_green, glass_yellow, glass_red, glass_blue, glass_dim, glass_active;
        public NinePatch ninePatch;
        public NinePatchDrawable drawable;
    }

    public static class NinePatches {
        public static NinePatch plain;
        public static NinePatch plain_dim;
        public static NinePatch plain_gradient;
        public static NinePatch plain_gradient_highlight_yellow;
        public static NinePatch plain_gradient_highlight_green;
        public static NinePatch plain_gradient_highlight_red;
        public static NinePatch glass;
        public static NinePatch glass_active;
        public static NinePatch glass_blue;
        public static NinePatch glass_light_blue;
        public static NinePatch glass_corner_bl;
        public static NinePatch glass_corner_br;
        public static NinePatch glass_corner_tl;
        public static NinePatch glass_corner_tr;
        public static NinePatch glass_corners;
        public static NinePatch glass_red;
        public static NinePatch glass_yellow;
        public static NinePatch glass_green;
        public static NinePatch glass_tab;
        public static NinePatch glass_dim;
        public static NinePatch metal;
        public static NinePatch metal_blue;
        public static NinePatch metal_green;
        public static NinePatch metal_yellow;
        public static NinePatch shear;
        public static NinePatch outline;
        public static NinePatch debug;
    }

    public static class Particles {
        public TextureRegion circle;
        public TextureRegion sparkle;
        public TextureRegion smoke;
        public TextureRegion ring;
        public TextureRegion dollar;
        public Animation<TextureRegion> blood;
        public TextureRegion bloodSplat;
        public TextureRegion sparks;
        public TextureRegion line;
        public Animation<TextureRegion> stars;
        public Animation<TextureRegion> twirls;
        public Animation<TextureRegion> splats;
        public Animation<TextureRegion> fires;
        public Animation<TextureRegion> magics;

    }

    public Assets() {
        this(Load.SYNC);
    }

    public Assets(Load load) {
        initialized = false;

        preferences = Gdx.app.getPreferences(prefsName);

        // create a single pixel texture and associated region
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        {
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            pixmap.drawPixel(1, 0);
            pixmap.drawPixel(0, 1);
            pixmap.drawPixel(1, 1);
            pixel = new Texture(pixmap);
        }
        pixmap.dispose();
        pixelRegion = new TextureRegion(pixel);

        batch = new SpriteBatch();
        shapes = new ShapeDrawer(batch, pixelRegion);
        layout = new GlyphLayout();

        mgr = new AssetManager();
        {
            mgr.load("sprites/sprites.atlas", TextureAtlas.class);
            mgr.load("ui/uiskin.json", Skin.class);
            mgr.load("i18n/strings", I18NBundle.class);

            mgr.load("images/title-image_00.png", Texture.class);
            mgr.load("images/libgdx.png", Texture.class);
            mgr.load("images/noise.png", Texture.class);
            mgr.load("images/pixel.png", Texture.class);
            mgr.load("images/level-layout.png", Texture.class);
            mgr.load("images/intro-background.png", Texture.class);
            mgr.load("images/parchment.png", Texture.class);

            mgr.load("fonts/outfit-medium-20px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-40px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-80px.fnt", BitmapFont.class);

            mgr.load("audio/sounds/coin.ogg", Sound.class);
            mgr.load("audio/sounds/idle-click.ogg", Sound.class);
            mgr.load("audio/sounds/lock-in.ogg", Sound.class);
            mgr.load("audio/sounds/select.ogg", Sound.class);
            mgr.load("audio/sounds/metal-tap.ogg", Sound.class);
            mgr.load("audio/sounds/pew.ogg", Sound.class);
            mgr.load("audio/sounds/click1.ogg", Sound.class);
            mgr.load("audio/sounds/level-up.wav", Sound.class);
            mgr.load("audio/sounds/error1.ogg", Sound.class);
            mgr.load("audio/sounds/error-buzz.ogg", Sound.class);
            mgr.load("audio/sounds/horn-fanfare.ogg", Sound.class);
            mgr.load("audio/sounds/hit1.ogg", Sound.class);
            mgr.load("audio/sounds/hit2.ogg", Sound.class);
            mgr.load("audio/sounds/hit3.ogg", Sound.class);
            mgr.load("audio/sounds/hit4.ogg", Sound.class);
            mgr.load("audio/sounds/hit5.ogg", Sound.class);
            mgr.load("audio/sounds/hit6.ogg", Sound.class);

            mgr.load("audio/music/intro-music.ogg", Music.class);
            mgr.load("audio/music/main-music.ogg", Music.class);
            mgr.load("audio/music/outro-music.ogg", Music.class);
        }

        if (load == Load.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1;

        atlas = mgr.get("sprites/sprites.atlas");

        cherry = new Animation<>(.1f, atlas.findRegions("pets/cat"), Animation.PlayMode.LOOP);
        yoda = new Animation<>(.1f, atlas.findRegions("pets/ross-dog"), Animation.PlayMode.LOOP);
        osha = new Animation<>(.1f, atlas.findRegions("pets/kitten"), Animation.PlayMode.LOOP);
        asuka = new Animation<>(.1f, atlas.findRegions("pets/dog"), Animation.PlayMode.LOOP);
        obi = new Animation<>(.1f, atlas.findRegions("pets/white-lab-dog"), Animation.PlayMode.LOOP);
        spawnEvilIdle = new Animation<>(0.2f, atlas.findRegions("stage/spawn-evil-idle/spawn-evil-idle"), Animation.PlayMode.LOOP);
        spawnEvilActive = new Animation<>(0.2f, atlas.findRegions("stage/spawn-evil-active/spawn-evil-active"), Animation.PlayMode.LOOP_PINGPONG);
        spawnGoodIdle = new Animation<>(0.15f, atlas.findRegions("stage/spawn-idle/spawn-idle"), Animation.PlayMode.LOOP);
        spawnGoodActive = new Animation<>(0.15f, atlas.findRegions("stage/spawn-idle/spawn-idle"), Animation.PlayMode.LOOP_PINGPONG);
        candle = new Animation<>(0.15f, atlas.findRegions("stage/candlestick-idle/candlestick-idle"), Animation.PlayMode.LOOP);
        candleEvil = new Animation<>(0.15f, atlas.findRegions("stage/candlestick-evil-idle/candlestick-evil-idle"), Animation.PlayMode.LOOP);

        babe1 = new Animation<TextureRegion>(0.1f, atlas.findRegions("optional/babe-a-wave"), Animation.PlayMode.LOOP);
        babe2 = new Animation<TextureRegion>(0.1f, atlas.findRegions("optional/babe-b-wave"), Animation.PlayMode.LOOP);
        organGrinder = new Animation<TextureRegion>(0.2f, atlas.findRegions("optional/organ-grinder"), Animation.PlayMode.LOOP);

        numbers = new ArrayList<>();
        for (int i = 0; i <= 9; ++i) {
            var anim = new Animation<TextureRegion>(0.1f, atlas.findRegions("particles/font-points-" + i));
            numbers.add(anim);
        }

        // characters
        addCharacterImages(king, "king");
        addCharacterImages(pawn, "peasant");
        addCharacterImages(knight, "knight");
        addCharacterImages(bishop, "wizard");
        addCharacterImages(rook, "archer");
        addCharacterImages(queen, "queen");

        thrones.add(new Animation<>(0.1f, atlas.findRegions("stage/throne-idle"), Animation.PlayMode.LOOP));
        thrones.add(new Animation<>(0.1f, atlas.findRegions("stage/throne-evil-idle"), Animation.PlayMode.LOOP));

        // Initialize asset helpers

        // TextureRegions
        closeButton = atlas.findRegion("pixel");
        cardTexture = atlas.findRegion("icons/kenney-board-game/dice_empty");

        // String replacement
        strings = mgr.get("i18n/strings", I18NBundle.class);

        // Misc references
        title = mgr.get("images/title-image_00.png");
        gdx = mgr.get("images/libgdx.png");
        whitePixel = mgr.get("images/pixel.png");
        noiseTexture = mgr.get("images/noise.png");
        noiseTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        levelLayout = mgr.get("images/level-layout.png");
        introBackground = mgr.get("images/intro-background.png");
        parchment = mgr.get("images/parchment.png");

        // Fonts
        smallFont = mgr.get("fonts/outfit-medium-20px.fnt");
        smallFont.setUseIntegerPositions(false);
        font      = mgr.get("fonts/outfit-medium-40px.fnt");
        font.setUseIntegerPositions(false);
        largeFont = mgr.get("fonts/outfit-medium-80px.fnt");
        largeFont.setUseIntegerPositions(false);

        var ttfParameterLarge = new FreeTypeFontGenerator.FreeTypeFontParameter() {{ size = 40; }};
        var ttfGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Abandoned-Bold.ttf"));
        fontAbandoned = ttfGenerator.generateFont(ttfParameterLarge);
        var ttfParameterMed = new FreeTypeFontGenerator.FreeTypeFontParameter() {{ size = 30; }};
        fontAbandonedMed = ttfGenerator.generateFont(ttfParameterMed);

        ttfGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Treamd.ttf"));
        fontTreasureMap = ttfGenerator.generateFont(ttfParameterLarge);
        ttfGenerator.dispose();

        var ttfParameterSmall = new FreeTypeFontGenerator.FreeTypeFontParameter() {{size = 40; borderWidth = 0f; shadowOffsetX = 2; shadowOffsetY = 2;}};
        ttfGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/zekton.ttf"));
        fontZektonSmall = ttfGenerator.generateFont(ttfParameterSmall);
        fontZektonSmall.setUseIntegerPositions(false);
        ttfGenerator.dispose();

        inputPrompts = new InputPrompts(this);

        particles = new Particles();
        particles.circle  = atlas.findRegion("particles/circle");
        particles.ring    = atlas.findRegion("particles/ring");
        particles.smoke   = atlas.findRegion("particles/smoke");
        particles.sparkle = atlas.findRegion("particles/sparkle");
        particles.dollar  = atlas.findRegion("particles/dollars");
        particles.blood   = new Animation<>(.1f, atlas.findRegions("particles/blood/particle-blood"), Animation.PlayMode.LOOP);
        particles.bloodSplat = atlas.findRegion("particles/blood/particle-blood-splat");
        particles.sparks  = atlas.findRegion("particles/kenney/spark");
        particles.stars   = new Animation<>(.1f, atlas.findRegions("particles/kenney/star"), Animation.PlayMode.LOOP);
        particles.splats   = new Animation<>(.1f, atlas.findRegions("particles/splats/splat"), Animation.PlayMode.LOOP);
        particles.twirls  = new Animation<>(.1f, atlas.findRegions("particles/kenney/twirl"), Animation.PlayMode.LOOP);
        particles.fires  = new Animation<>(.1f, atlas.findRegions("particles/kenney/fire"), Animation.PlayMode.LOOP);
        particles.magics  = new Animation<>(.1f, atlas.findRegions("particles/kenney/magic"), Animation.PlayMode.LOOP);
        particles.line    = atlas.findRegion("particles/line");

        // Transition shaders
        blindsShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/blinds.frag");
        fadeShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/dissolve.frag");
        radialShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/radial.frag");
        doomShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/doomdrip.frag");
        pixelizeShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/pixelize.frag");
        doorwayShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/doorway.frag");
        crosshatchShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/crosshatch.frag");
        rippleShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/ripple.frag");
        simpleZoomShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/simplezoom.frag");
        heartShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/heart.frag");
        stereoShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/stereo.frag");
        circleCropShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/circlecrop.frag");
        cubeShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/cube.frag");
        dreamyShader = loadShader("shaders/transitions/default.vert", "shaders/transitions/dreamy.frag");

        randomTransitions = new Array<>();
        randomTransitions.addAll(
            blindsShader, fadeShader, radialShader, doomShader, pixelizeShader, doorwayShader, crosshatchShader,
            rippleShader, simpleZoomShader, heartShader, stereoShader, circleCropShader, cubeShader, dreamyShader);


        portalShader = loadShader("shaders/default.vert", "shaders/portal.frag");
        gridShader = loadShader("shaders/default.vert", "shaders/grid.frag");

        // initialize patch values
        Patch.debug.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/debug"), 2, 2, 2, 2);
        Patch.panel.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/panel"), 15, 15, 15, 15);
        Patch.glass.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/glass"), 8, 8, 8, 8);
        Patch.glass_green.ninePatch  = new NinePatch(atlas.findRegion("ninepatch/glass-green"), 8, 8, 8, 8);
        Patch.glass_yellow.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"), 8, 8, 8, 8);
        Patch.glass_red.ninePatch  = new NinePatch(atlas.findRegion("ninepatch/glass-red"), 8, 8, 8, 8);
        Patch.glass_blue.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-blue"), 8, 8, 8, 8);
        Patch.glass_dim.ninePatch    = new NinePatch(atlas.findRegion("ninepatch/glass-dim"), 8, 8, 8, 8);
        Patch.glass_active.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-active"), 8, 8, 8, 8);
        Patch.metal.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/metal"), 12, 12, 12, 12);

        Patch.debug.drawable        = new NinePatchDrawable(Patch.debug.ninePatch);
        Patch.panel.drawable        = new NinePatchDrawable(Patch.panel.ninePatch);
        Patch.glass.drawable        = new NinePatchDrawable(Patch.glass.ninePatch);
        Patch.glass_green.drawable  = new NinePatchDrawable(Patch.glass_green.ninePatch);
        Patch.glass_yellow.drawable = new NinePatchDrawable(Patch.glass_yellow.ninePatch);
        Patch.glass_dim.drawable    = new NinePatchDrawable(Patch.glass_dim.ninePatch);
        Patch.glass_active.drawable = new NinePatchDrawable(Patch.glass_active.ninePatch);
        Patch.metal.drawable        = new NinePatchDrawable(Patch.metal.ninePatch);
        Patch.glass_red.drawable    = new NinePatchDrawable(Patch.glass_red.ninePatch);
        Patch.glass_blue.drawable   = new NinePatchDrawable(Patch.glass_blue.ninePatch);

        NinePatches.plain_dim                       = new NinePatch(atlas.findRegion("ninepatch/plain-dim"),               12, 12, 12, 12);
        NinePatches.plain_gradient                  = new NinePatch(atlas.findRegion("ninepatch/plain-gradient"),           2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_yellow = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-yellow"), 2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_green  = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-green"), 2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_red    = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-red"), 2,  2,  2,  2);
        NinePatches.glass                           = new NinePatch(atlas.findRegion("ninepatch/glass"),                   12, 12, 12, 12);
        NinePatches.glass_blue                      = new NinePatch(atlas.findRegion("ninepatch/glass-blue"),              12, 12, 12, 12);
        NinePatches.glass_light_blue                = new NinePatch(atlas.findRegion("ninepatch/glass"),                   12, 12, 12, 12);
        NinePatches.glass_active                    = new NinePatch(atlas.findRegion("ninepatch/glass-active"),            12, 12, 12, 12);
        NinePatches.glass_corner_bl                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-bl"),         12, 12, 12, 12);
        NinePatches.glass_corner_br                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-br"),         12, 12, 12, 12);
        NinePatches.glass_corner_tl                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-tl"),         12, 12, 12, 12);
        NinePatches.glass_corner_tr                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-tr"),         12, 12, 12, 12);
        NinePatches.glass_corners                   = new NinePatch(atlas.findRegion("ninepatch/glass-corners"),           12, 12, 12, 12);
        NinePatches.glass_red                       = new NinePatch(atlas.findRegion("ninepatch/glass-red"),               12, 12, 12, 12);
        NinePatches.glass_yellow                    = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"),            12, 12, 12, 12);
        NinePatches.glass_green                     = new NinePatch(atlas.findRegion("ninepatch/glass-green"),             12, 12, 12, 12);
        NinePatches.glass_tab                       = new NinePatch(atlas.findRegion("ninepatch/glass-tab"),               12, 12, 22, 12);
        NinePatches.glass_dim                       = new NinePatch(atlas.findRegion("ninepatch/glass-dim"),               12, 12, 22, 12);
        NinePatches.metal                           = new NinePatch(atlas.findRegion("ninepatch/metal"),                   12, 12, 12, 12);
        NinePatches.metal_blue                      = new NinePatch(atlas.findRegion("ninepatch/metal-blue"),              12, 12, 12, 12);
        NinePatches.metal_green                     = new NinePatch(atlas.findRegion("ninepatch/metal-green"),             12, 12, 12, 12);
        NinePatches.metal_yellow                    = new NinePatch(atlas.findRegion("ninepatch/metal-yellow"),            12, 12, 12, 12);
        NinePatches.shear                           = new NinePatch(atlas.findRegion("ninepatch/shear"),                   75, 75, 12, 12);
        NinePatches.outline                         = new NinePatch(atlas.findRegion("ninepatch/outline"),                 3,   3,  3,  3);
        NinePatches.debug                         = new NinePatch(atlas.findRegion("ninepatch/debug"),                 3,   3,  3,  3);

        TileOverlayAssets.populate(atlas);

        // Audio
        coin = mgr.get("audio/sounds/coin.ogg", Sound.class);
        idleClick = mgr.get("audio/sounds/idle-click.ogg", Sound.class);
        click = mgr.get("audio/sounds/click1.ogg", Sound.class);
        lockIn = mgr.get("audio/sounds/lock-in.ogg", Sound.class);
        select = mgr.get("audio/sounds/select.ogg", Sound.class);
        metalTap = mgr.get("audio/sounds/metal-tap.ogg", Sound.class);
        pew = mgr.get("audio/sounds/pew.ogg", Sound.class);
        levelUp = mgr.get("audio/sounds/level-up.wav", Sound.class);
        errorThud = mgr.get("audio/sounds/error1.ogg", Sound.class);
        errorBuzz = mgr.get("audio/sounds/error-buzz.ogg", Sound.class);
        hornFanfare = mgr.get("audio/sounds/horn-fanfare.ogg", Sound.class);
        hit1 = mgr.get("audio/sounds/hit1.ogg", Sound.class);
        hit2 = mgr.get("audio/sounds/hit2.ogg", Sound.class);
        hit3 = mgr.get("audio/sounds/hit3.ogg", Sound.class);
        hit4 = mgr.get("audio/sounds/hit4.ogg", Sound.class);
        hit5 = mgr.get("audio/sounds/hit5.ogg", Sound.class);
        hit6 = mgr.get("audio/sounds/hit6.ogg", Sound.class);

        introMusic = mgr.get("audio/music/intro-music.ogg", Music.class);
        mainMusic = mgr.get("audio/music/main-music.ogg", Music.class);
        outroMusic = mgr.get("audio/music/outro-music.ogg", Music.class);

        initialized = true;
        return 1;
    }

    @Override
    public void dispose() {
        mgr.dispose();
        batch.dispose();
        pixel.dispose();
    }

    public static ShaderProgram loadShader(String vertSourcePath, String fragSourcePath) {
        ShaderProgram.pedantic = true;
        var shaderProgram = new ShaderProgram(
            Gdx.files.internal(vertSourcePath),
            Gdx.files.internal(fragSourcePath));
        var log = shaderProgram.getLog();

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("LoadShader", "compilation failed:\n" + log);
            throw new GdxRuntimeException("LoadShader: compilation failed:\n" + log);
        } else if (Config.Debug.shaders) {
            Gdx.app.debug("LoadShader", "ShaderProgram compilation log: " + log);
        }

        return shaderProgram;
    }

    public float getMusicVolume() {
        return preferences.getFloat("music", .5f);
    }

    public float getSoundVolume() {
        return preferences.getFloat("sound", .85f);
    }

    public void storeMusicVolume(float level) {
        preferences.putFloat("music", level);
        preferences.flush();
    }

    public void storeSoundVolume(float level) {
        preferences.putFloat("sound", level);
        preferences.flush();
    }

    private void addCharacterImages(Array<Array<Animation<TextureRegion>>> array, String name) {
        // 0 - good, 1 - evil
        array.add(getAnimGroup(name, "good"));
        array.add(getAnimGroup(name, "evil"));
    }

    private Array<Animation<TextureRegion>> getAnimGroup(String name, String alignment) {
        var animGroup = new Array<Animation<TextureRegion>>();
        var idleImage = "characters/" + alignment + "/" + name + "/" + name + "-idle";
        var actionImage = "characters/" + alignment + "/" + name + "/" + name + "-action";
        var portrait = "characters/" + alignment + "/" + name + "/" + name + "-portrait";
        animGroup.add(new Animation<>(.15f, atlas.findRegions(idleImage), Animation.PlayMode.LOOP));
        animGroup.add(new Animation<>(.15f, atlas.findRegions(actionImage), Animation.PlayMode.NORMAL));
        animGroup.add(new Animation<>(.15f, atlas.findRegions(portrait), Animation.PlayMode.LOOP));

        return animGroup;
    }

    public Animation<TextureRegion> getAnimation(GamePiece.Type type, GamePiece.Owner owner) {
        var alignment = owner == GamePiece.Owner.Player ? 0 : 1;
        switch (type) {
            case Pawn:
                return pawn.get(alignment).get(0);
            case Knight:
                return knight.get(alignment).get(0);
            case Bishop:
                return bishop.get(alignment).get(0);
            case Rook:
                return rook.get(alignment).get(0);
            case Queen:
                return queen.get(alignment).get(0);
        }
        return null;
    }

    public Animation<TextureRegion> getPortrait(GamePiece.Type type, GamePiece.Owner owner) {
        var alignment = owner == GamePiece.Owner.Player ? 0 : 1;
        switch (type) {
            case Pawn:
                return pawn.get(alignment).get(2);
            case Knight:
                return knight.get(alignment).get(2);
            case Bishop:
                return bishop.get(alignment).get(2);
            case Rook:
                return rook.get(alignment).get(2);
            case Queen:
                return queen.get(alignment).get(2);
        }
        return null;
    }
}
