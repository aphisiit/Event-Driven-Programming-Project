package event.project.core;

import static playn.core.PlayN.*;

import event.project.characters.Boy;
import event.project.characters.Zombie;
import event.project.characters.Zombie2;
import event.project.characters.Zombie3;
import event.project.item.Bullet;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 23/3/2559.
 */
public class GameScreen extends Screen {
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer pauseButton;
    private final ImageLayer heartLayer1;
    private final ImageLayer heartLayer2;
    private final ImageLayer heartLayer3;
    private final ImageLayer heartLayer4;
    //private final ImageLayer coinLayer;


    private Pause pauseScreen;
    private OverScreen overScreen;

    private Boy boy;
    private Zombie zombie1;
    private Zombie2 zombie2;
    private Zombie3 zombie3;


    public static char sex;

    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean showDedugDraw = true;
    private DebugDrawBox2D debugDrawBox2D;

    private int index = 0;
    private float getPositionBoy = 0.0f;
    public static int zombieAttack;
    public static int count = 0;
    private int score = 0;
    private boolean checkzombie2 = false;
    //private List<Zombie> zombieArrayList;// = new ArrayList<Zombie>();
    public static HashMap<Body,String> bodies = new HashMap<Body, String>();
    private String debugString = "";
    private String getDebugStringCoin = "";
    private boolean destroy = false;
    public static boolean pause;

    private enum Character{
        boy,zombie
    }

    private Character character;
    private World world;

    private  int shoot = 0;
    private boolean isHasGun;
    private static List<Bullet> bulletList;
    private static List<Bullet> bulletDestroy;
    public GroupLayer bulletGroup = graphics().createGroupLayer();

    public GameScreen(){
        ss = new ScreenStack();

        Image bgImage = assets().getImage("images/gameScreenBG.png");
        bg = graphics().createImageLayer(bgImage);

        Image backImage= assets().getImage("images/pauseButton.png");
        pauseButton = graphics().createImageLayer(backImage);

        Image heartImage1 = assets().getImage("images/heart1.png");
        heartLayer1 = graphics().createImageLayer(heartImage1);
        heartLayer1.setTranslation(100,10);

        Image heartImage2 = assets().getImage("images/heart2.png");
        heartLayer2 = graphics().createImageLayer(heartImage2);
        heartLayer2.setTranslation(100,10);

        Image heartImage3 = assets().getImage("images/heart3.png");
        heartLayer3 = graphics().createImageLayer(heartImage3);
        heartLayer3.setTranslation(100,10);

        Image heartImage4 = assets().getImage("images/heart4.png");
        heartLayer4 = graphics().createImageLayer(heartImage4);
        heartLayer4.setTranslation(100,10);
    }

    public GameScreen(final ScreenStack ss){
        this.ss = ss;

        //overScreen = new OverScreen(ss);
        //pauseScreen = new Pause(ss);
        pause = false;

        zombieAttack = 0;

        graphics().rootLayer().clear();
        //zombieArrayList = new ArrayList<Zombie>();

        Vec2 gravity = new Vec2(0.0f,9.8f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        Image bgImage = assets().getImage("images/gameScreenBG.png");
        bg = graphics().createImageLayer(bgImage);

        Image backImage= assets().getImage("images/pauseButton.png");
        pauseButton = graphics().createImageLayer(backImage);

        //Image coin = assets().getImage("images/coin.png");
        //coinLayer = graphics().createImageLayer(coin);
        //coinLayer.setTranslation(320,240f);

        Image heartImage1 = assets().getImage("images/heart1.png");
        heartLayer1 = graphics().createImageLayer(heartImage1);
        heartLayer1.setTranslation(100,10);

        Image heartImage2 = assets().getImage("images/heart2.png");
        heartLayer2 = graphics().createImageLayer(heartImage2);
        heartLayer2.setTranslation(100,10);

        Image heartImage3 = assets().getImage("images/heart3.png");
        heartLayer3 = graphics().createImageLayer(heartImage3);
        heartLayer3.setTranslation(100,10);

        Image heartImage4 = assets().getImage("images/heart4.png");
        heartLayer4 = graphics().createImageLayer(heartImage4);
        heartLayer4.setTranslation(100,10);

        pauseButton.setTranslation(10,10);
        pauseButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new Pause(ss)); // <- Pop Screen
                pause = true;
            }
        });

        isHasGun = false;

        boy = new Boy(world,100,100,isHasGun,sex);
        //girl = new Girl(world,100,100,isHasGun);

        zombie1 = new Zombie(world,500,100);
        //zombie2.layer().setVisible(false);
        //zombie2.getBody().setActive(false);


        //zombie3 = new Zombie3(world,400,100);
        zombie1.state = Zombie.State.WALK;

        bulletList = new ArrayList<Bullet>();
        bulletDestroy = new ArrayList<Bullet>();
        zombie2 = new Zombie2(world,600f,100f);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                System.out.println("offset = " + zombie1.offset);

                for(Bullet bullet: bulletList){
                    if((contact.getFixtureA().getBody() == bullet.getBody()
                            && contact.getFixtureB().getBody() == zombie1.getBody())
                            || (contact.getFixtureA().getBody() == zombie1.getBody()
                            && contact.getFixtureB().getBody() == bullet.getBody())){
                        zombie1.countATTK++;
                        bulletDestroy.add(bullet);
                        if(zombie1.countATTK >= 2){
                            zombie1.state = Zombie.State.DIE;
                            zombie1.layer().destroy();
                            character = Character.zombie;
                            destroy = true;
                            score += 10;
                            getDebugStringCoin = "Score : " + score;
                        }
                    }
                }
                if(contact.getFixtureA().getBody() == boy.getBody() &&
                        contact.getFixtureB().getBody() == zombie1.getBody()
                        || contact.getFixtureB().getBody() == boy.getBody() &&
                        contact.getFixtureA().getBody() == zombie1.getBody()){
                    zombie1.state = Zombie.State.ATTK;
                    if(boy.state == Boy.State.ATTK){
                        System.out.println("Boy is Attack.");
                        System.out.println("Count ATTK = " + zombie1.countATTK);
                        zombie1.countATTK++;
                        debugString = "Boy is Attack Zombie.";
                        getDebugStringCoin = "Score : " + score;
                        zombie1.state = Zombie.State.HIT;
                        if(zombie1.countATTK > 2){
                            isHasGun = true;
                            zombie1.state = Zombie.State.DIE;
                            zombie1.layer().destroy();
                            character = Character.zombie;
                            destroy = true;
                            score += 10;
                            getDebugStringCoin = "Score : " + score;
                            checkzombie2 = true;
                            //zombie2.layer().setVisible(true);
                            //zombie2.getBody().setActive(true);
                        }
                    }
                    else if(zombie1.state == Zombie.State.ATTK && boy.state == Boy.State.IDLE ||
                            zombie1.state == Zombie.State.ATTK && boy.state == Boy.State.RUN){
                        System.out.println("Zombie is Attack Boy");
                        zombieAttack++;
                        System.out.println("ZombieAttack = " + zombieAttack);
                        if(zombieAttack >= 3){
                            pause = true;
                            ss.push(new OverScreen(ss));
                        }
                    }
                }
                if((bodies.get(a) == "Boy" && bodies.get(b) == "ground")
                        || (bodies.get(a) == "ground" && bodies.get(b) == "Boy")){
                    boy.checkJump = true;
                }else {
                    boy.checkJump = false;
                }

                System.out.println(bodies.get(a) + " Attack " + bodies.get(b));
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
    }

    public void addBullet(Bullet bullet){
        bulletList.add(bullet);
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(pauseButton);

        this.layer.add(zombie1.layer());
        this.layer.add(boy.layer());

        //this.layer.add(zombie3.layer());
        this.layer.add(bulletGroup);


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
        bodies.put(ground,"Ground");

        Body leftGround = world.createBody(new BodyDef());
        EdgeShape leftShape = new EdgeShape();
        leftShape.set(new Vec2(0, 0), new Vec2(0, height));
        leftGround.createFixture(leftShape, 0.0f);
        bodies.put(leftGround,"Left Side");

        Body rightGround = world.createBody(new BodyDef());
        EdgeShape rightShape = new EdgeShape();
        rightShape.set(new Vec2(width, 0), new Vec2(width, height));
        rightGround.createFixture(rightShape, 0.0f);
        bodies.put(leftGround,"Left Side");

        //Body coinBody = world.createBody(new BodyDef());
        //CircleShape coinShape = new CircleShape();
        //coinShape.setRadius(0.45f);
        //coinShape.m_p.set(12.5f, 9.5f);
        //coinBody.createFixture(coinShape, 0.0f);

        //bodies.put(coinBody,"coin");
    }

    public void update(int delta){
        if(pause == false) {
            super.update(delta);
            //for (int i = 0; i < index ; i++)
            //    zombieArrayList.get(i).update(delta);
            zombie1.update(delta);
            boy.update(delta);
            if(checkzombie2 == true) {
                this.layer.add(zombie2.layer());
                zombie2.update(delta);
            }
            //zombie3.update(delta);
            world.step(0.033f, 10, 10);

            if (destroy == true) {
                switch (character) {
                    case zombie:
                        world.destroyBody(zombie1.getBody());
                        break;
                    case boy:
                        world.destroyBody(boy.getBody());
                        break;
                }
            }

            if (zombieAttack == 0) {
                this.layer.add(heartLayer1);
            } else if (zombieAttack == 1) {
                heartLayer1.destroy();
                this.layer.add(heartLayer2);
            } else if (zombieAttack == 2) {
                heartLayer2.destroy();
                this.layer.add(heartLayer3);
            } else if (zombieAttack == 3) {
                heartLayer3.destroy();
                this.layer.add(heartLayer4);
            }

            for(Bullet bullet: bulletList){
                bullet.update(delta);
            }
            for (Bullet bullet: bulletList){
                bulletGroup.add(bullet.layer());
            }

            boy.updateHasGun(isHasGun);

            while(!bulletDestroy.isEmpty()){
                bulletDestroy.get(0).getBody().setActive(false);
                bulletList.get(0).layer().destroy();
                bulletList.remove(0);
                world.destroyBody(bulletDestroy.remove(0).getBody());
            }
        }
        else{

        }
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

        zombie1.paint(clock);
        boy.paint(clock);
        if(checkzombie2 == true) {
            zombie2.paint(clock);
        }
        //zombie3.paint(clock);

        for(Bullet bullet: bulletList){
            bullet.paint(clock);
        }
    }
}
