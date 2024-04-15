package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.audio.AudioManager;

public class AfterCreditScreen extends BaseScreen {

    private static float textScale = 1.0f;
    float accum = 0;
    PerspectiveCamera perspectiveCamera;
    GlyphLayout layout;
    BitmapFont font;
    Rectangle skipRect;
    Rectangle speedRect;
    boolean done;

    // Last day Magic numbers
    Rectangle skipButtonRect = new Rectangle(Config.Screen.window_width - 300, 620, 150, 40);

    String text = "Goomba Simulator 2\n\n" +
        "Game Jam the game™\n\n" +
        "Deja vu like game - sum mo(r)Ning\n\n" +
        "The next next generation Ai requires blood sacrifice.. what will you do to help humanity?\n\n" +
        "Bible hero. Pray in time to the beat of that sweet gospel music. Can I get an amen?!! Al-leh-luh-ahhh\n\n" +
        "Parent Sim- summon your teenager Before the Crack of noon\n\n" +
        "Reverse of paperboy the Summoning. Slap away the subpènas\n\n" +
        "Summoning school, teach (or learn) how to draw the correct sigils to summon specific types of monsters, could play as a student and after beating a level you go back to class a learn a new type of creature to summon, could play as a teacher and you have to grade students as they move through each level (in lanes, so you're watching several move through at a time), could play as the entity that spawns summons based on the sigils used, maybe a little 'paper's please'-like\n\n" +
        "Cooking or crafting game where you have to use the same resource pool to summon both the ingredients you need to make stuff and temporary helper creatures to help crank out a larger volume of the cooked / crafted items being requested, somewhere in the ballpark of the overcooked games, but more aimed towards resource mgmt, do I spend points to create items or to create monsters, maybe any items are fine (rather than specific things being desired each round) but the player is scored on the quantity and quality of items produced, lots of opportunity for balatro style multipliers\n\n" +
        "Peglin - Like that pachinko game Jeffrey was talking about where there's monster fighting in a narrow strip at the top of the viewport but the main gameplay area is a puzzle/pachinko/match-3 type of game where the score or matches that the player makes summon different creatures to fight in the 'world' at the top of the screen. Reverse tower defense-like  game happening on the top of the viewport?\n\n" +
        "puzzles and dragons (wizardry-ish and match 3)\n\n" +
        "Spreadsheets the game!\n\n" +
        "Mega man style game (or maybe closer to kirby / little nemo) where the player can have one summoned creature at a time, and that creature provides some mobility changing ability, flying, high jumping, floating, fast running, etc… and the player has to manage which creature they need to progress through a given level - rip off a boy and his blob\n\n" +
        "Player draws creatures to summon them, and we try to rig a skeleton to whatever janky shit people draw and make it work in the game… if we could have a couple different types of rigs for like no-peds, uni-peds, bipeds, etc… might be possible to get decent looking behavior for some categories of creature that people might draw (infer which to use based on some image analysis algo, finding 'appendages' via pixel paths that don't create a cycle / loop back in to the main bulk of pixels\n\n" +
        "Other riffs on the 'player draws a thing' game feature\n\n" +
        "uniracers\n\n" +
        "Battle bots style game, but you can only summon a certain number of parts and you have to assemble based on that, maybe it also applies to behaviors, you can only summon a conditional, two loops, and a retreat, or to make it simpler they can summon a limited number of simple instructions like 'move forward', 'turn 90 ccw', 'attack front', etc…and they have to program a fighting robot based on whatever they happen to roll\n\n" +
        "RoboRally\n\n" +
        "Let's throw in some random banter with whatever the player summons, they can lament their shitty lives of being pulled away from a nice dinner to appear in the material (lol, 'meat'erial) realm and fight for the player\n\n" +
        "Reverse summoning, the player is a human who gets summoned into the astral plane and has to battle for their spirit master\n\n" +
        "RTS, summon creatures and fight, we have Fog of War now\n\n" +
        "Hunting 2 starring Poulterguy (Sega Genesis)\n\n" +
        "Mechanic: people are in a house just doing their thing and you have to summon ghosts to scare em, somehow, beetlejuice the game\n\n" +
        "Summon balls, ball of duty: summoner edition ??you can summon my balls?? yes\n\n" +
        "Slummoning:Call Forth the Riff-Raff\n\n" +
        "Summ Dog Million Hairs- have to vacuum up your summoned dog familiar's fur \n\n" +
        "Card game where the cards are called monnings. Add up\n\n" +
        "Grid based puzzle game where you as character run around placing beacons/planting flags to call respective npcs to move to that square - do this at appropriate times to move them out of path of bus/jumping shark attack/etc/\n\n" +
        "Mother chip, data bit kids, pikman style\n\n" +
        "Jury Summons: how can you get out of jury duty?\n\n" +
        "Jury Summons: how can you get INTO the jury?\n\n" +
        "JK Summons: no game idea - just like the pun\n\n" +
        "Richard Summons\n\n" +
        "Summ-synonyms - Evoke, invoke, conjure, elicit, induce, call forth..call fifth.\n\n" +
        "Sorcerer summoning demons/helpers/etc.\n\n" +
        "Witch summoning familiar\n\n" +
        "Midwives of Hell: help birth baby demons for your army Woe vs. Raid\n\n" +
        "Wol ves Raid (wolves are raiding you)\n\n" +
        "Platformer - play as king where every action calls out a lil servant dude(s) i.e. jumping summons kneeling servant to spring you upwards/attacking calls out a knight who swings a sword/etc.\n\n" +
        "A 2D platformer where the player can summon platforms and obstacles to traverse the levels.\n\n" +
        "Summon a portal to your childhood to relive memories to solve puzzles\n\n"
        ;


    public AfterCreditScreen() {
        layout = new GlyphLayout();
        Assets assets = Main.game.assets;
        font = assets.font;
        font.getData().setScale(textScale);
        layout.setText(font, text, Color.WHITE, worldCamera.viewportWidth, Align.center, true);
        font.getData().setScale(1f);
        done = false;


        perspectiveCamera = new PerspectiveCamera(90, 1280, 800);
        perspectiveCamera.far=10000;
        perspectiveCamera.position.set(640, 0, 500);
        perspectiveCamera.lookAt(640, 400, 0);
        perspectiveCamera.update();
        skipRect = new Rectangle(windowCamera.viewportWidth - 170, 70, 150, 50);
        speedRect = new Rectangle(windowCamera.viewportWidth - 370, windowCamera.viewportHeight-70, 350, 50);
        Main.game.audioManager.playMusic(AudioManager.Musics.introMusic);
    }

    Vector3 mousePos = new Vector3();
    @Override
    public void update(float dt) {
        float speedMultiplier = 1.0f;
        if (exitingScreen) { return; }

        if (Gdx.input.isTouched()){
            mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            windowCamera.unproject(mousePos);
            if (!skipButtonRect.contains(mousePos.x, mousePos.y)) {
                speedMultiplier = 10f;
            }
        }

//        accum = MathUtils.clamp(accum, 0, layout.height);
        if (accum > layout.height) {
//            launchGame();
            done = true;
        }
        if (accum <= layout.height + 100f) {
//            launchGame();
            accum += 75*dt * speedMultiplier;
        }
        if (Gdx.input.justTouched()) {
            mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            windowCamera.unproject(mousePos);
            if (skipButtonRect.contains(mousePos.x, mousePos.y)) {
                launchGame();
            }
        }
    }

    private void launchGame() {
        if (!exitingScreen){
            exitingScreen = true;
            game.audioManager.stopMusic();
            game.setScreen(new TitleScreen(), assets.doomShader, 1f);
        }
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    @Override
    public void renderFrameBuffers(SpriteBatch batch) {

    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        batch.setProjectionMatrix(perspectiveCamera.combined);
        batch.begin();
        BitmapFont font = Main.game.assets.font;
        font.getData().setScale(textScale);
        font.setColor(.3f, .3f, .3f, 1.0f);
        font.draw(batch, text, 5, accum-5, worldCamera.viewportWidth, Align.center, true);
        font.setColor(Color.YELLOW);
        font.draw(batch, text, 0, accum, worldCamera.viewportWidth, Align.center, true);
        font.getData().setScale(1.0f);
//        batch.draw(textTexture, 0, 0, 1024, layout.height);

        batch.end();

        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        BitmapFont skipFont = assets.fontAbandoned;
        skipFont.getData().setScale(.4f);

        Assets.Patch.glass.ninePatch.draw(batch, skipButtonRect.x, skipButtonRect.y, skipButtonRect.width, skipButtonRect.height);

        if(!done) {
            assets.layout.setText(skipFont, "Return to Title", Color.WHITE, 150, Align.center, false);
            skipFont.draw(batch, assets.layout, skipButtonRect.x, skipButtonRect.y + (assets.layout.height + skipButtonRect.height)/2f);
        }
        else {
            skipButtonRect.y = 100;
            skipButtonRect.x = Config.Screen.window_width / 2 - 150f;
            skipButtonRect.width = 300;
            skipButtonRect.height = 100;
            assets.layout.setText(skipFont, "Return to Title!", Color.WHITE, 200, Align.center, false);
            skipFont.draw(batch, assets.layout, skipButtonRect.x + 50f, skipButtonRect.y + (assets.layout.height + skipButtonRect.height)/2f);
            Assets.Patch.glass.ninePatch.draw(batch, skipButtonRect.x, skipButtonRect.y, 300, 100);

        }


        skipFont.getData().setScale(1f);
        batch.end();

    }
}
