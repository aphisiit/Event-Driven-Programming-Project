package event.project.core;

import static playn.core.PlayN.*;

import event.project.characters.Zombie;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 23/3/2559.
 */
public class GameScreen extends Screen {
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
    private final ImageLayer coinLayer;
    private Zombie zombie;

    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean showDedugDraw = true;
    private DebugDrawBox2D debugDrawBox2D;

    private int index = 0;
    public static int count = 0;
    private int score = 0;
    private List<Zombie> zombieArrayList;// = new ArrayList<Zombie>();
    public static HashMap<Body,String> bodies = new HashMap<Body, String>();
    private String debugString = "";
    private String getDebugStringCoin = "";

    private World world;


    public GameScreen(final ScreenStack ss){
        this.ss = ss;

        graphics().rootLayer().clear();
        zombieArrayList = new ArrayList<Zombie>();

        Vec2 gravity = new Vec2(0.0f,9.8f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        Image bgImage = assets().getImage("images/bg.png");
        bg = graphics().createImageLayer(bgImage);

        Image backImage= assets().getImage("images/back_button.png");
        backButton = graphics().createImageLayer(backImage);

        Image coin = assets().getImage("images/coin.png");
        coinLayer = graphics().createImageLayer(coin);
        coinLayer.setTranslation(320,240f);

        backButton.setTranslation(10,10);
        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top()); // <- Pop Screen
            }
        });

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();
                if(bodies.get(a) != null){
                    score += 10;
                    debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                    getDebugStringCoin = "Score : " + score;
                    b.applyForce(new Vec2(80f,-100f),b.getPosition());
                }
                System.out.println("debugString : " + debugString);
                System.out.println("getDebugStringCoin : " + getDebugStringCoin);
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });

        mouse().setListener(new Mouse.Adapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                zombieArrayList.add(index, new Zombie(world, event.x(), event.y()));
                //layer.add(zombieArrayList.get(index).layer());
                index++;

                for (int i = 0; i < index; i++) {
                    //    this.layer.add(zombieArrayList.get(i).layer());
                    graphics().rootLayer().add(zombieArrayList.get(i).layer());
                }
            }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(backButton);
        this.layer.add(coinLayer);

        if (showDedugDraw) {
            CanvasImage image = graphics().createImage(
                    (int) (width / GameScreen.M_PER_PIXEL),
                    (int) (height / GameScreen.M_PER_PIXEL));
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
            debugDrawBox2D.setCamera(0, 0, 1f / GameScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDrawBox2D);
        }

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height / 1.25f), new Vec2(width, height / 1.25f));
        ground.createFixture(groundShape, 0.0f);

        Body coinBody = world.createBody(new BodyDef());
        CircleShape coinShape = new CircleShape();
        coinShape.setRadius(0.45f);
        coinShape.m_p.set(12.5f, 9.5f);
        coinBody.createFixture(coinShape, 0.0f);
    }

    public void update(int delta){
        super.update(delta);
        world.step(0.033f,10,10);
        for (int i = 0; i < index ; i++)
            zombieArrayList.get(i).update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(showDedugDraw){
            debugDrawBox2D.getCanvas().clear();
            debugDrawBox2D.getCanvas().setFillColor(Color.rgb(0,0,0));
            debugDrawBox2D.getCanvas().drawText(debugString,100f,50f);
            debugDrawBox2D.getCanvas().drawText(getDebugStringCoin,100f,100f);
            world.drawDebugData();
        }
        for (int i = 0; i < index; i++)
            zombieArrayList.get(i).paint(clock);
    }
}
