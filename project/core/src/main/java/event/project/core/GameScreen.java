package event.project.core;

import static playn.core.PlayN.*;

import event.project.characters.Zombie;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

/**
 * Created by Administrator on 23/3/2559.
 */
public class GameScreen extends Screen {
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
    private Zombie zombie;
    private int mouse_x;
    private int mouse_y;

    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean showDedugDraw = true;
    private DebugDrawBox2D debugDrawBox2D;


    private World world;


    public GameScreen(final ScreenStack ss){
        this.ss = ss;

        Image bgImage = assets().getImage("images/bg.png");
        bg = graphics().createImageLayer(bgImage);

        Image backImage= assets().getImage("images/back_button.png");
        backButton = graphics().createImageLayer(backImage);

        backButton.setTranslation(10,10);
        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top()); // <- Pop Screen
            }
        });

        Vec2 gravity = new Vec2(0.0f,9.8f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0,height/1.25f), new Vec2(width,height/1.25f));
        ground.createFixture(groundShape,0.0f);
        mouse().setListener(new Mouse.Adapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                mouse_x = (int)event.x();
                mouse_y = (int)event.y();
            }
        });

    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(backButton);

        zombie = new Zombie(world,mouse_x,mouse_y);

        this.layer.add(zombie.layer());

        if(showDedugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width / GameScreen.M_PER_PIXEL),
                    (int)(height / GameScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDrawBox2D = new DebugDrawBox2D();
            debugDrawBox2D.setCanvas(image);
            debugDrawBox2D.setFlipY(false);
            debugDrawBox2D.setStrokeAlpha(150);
            debugDrawBox2D.setFillAlpha(75);
            debugDrawBox2D.setStrokeWidth(2.0f);
            debugDrawBox2D.setFlags(DebugDraw.e_shapeBit |
                                    DebugDraw.e_jointBit |
                                    DebugDraw.e_aabbBit);
            debugDrawBox2D.setCamera(0,0,1f/GameScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDrawBox2D);
        }

    }
    public void update(int delta){
        super.update(delta);
        world.step(0.033f,10,10);
        zombie.update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(showDedugDraw){
            debugDrawBox2D.getCanvas().clear();
            world.drawDebugData();
        }
        zombie.paint(clock);
    }
}
