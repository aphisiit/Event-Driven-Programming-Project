package event.project.characters;

import event.project.core.GameScreen;
import event.project.item.Bullet;
import event.project.sprite.Sprite;
import event.project.sprite.SpriteLoader;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aphis on 02-Apr-16.
 */
public class Boy {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private Body body;
    private boolean hasGun = false;
    public static boolean checkJump = false;

    public enum State{
        IDLE,RUN,JUMP,ATTK,IDLEG,RUNG,JUMPG,ATTKG
    }
    public enum CheckKey{
        RIGHT,LEFT
    }
    private CheckKey checkKey = CheckKey.RIGHT;
    public State state; // = State.IDLE;
    public int e = 0;
    public int offset = 4;

    private World world;
    private GameScreen gameScreen = new GameScreen();

    public List<Bullet> bulletList;


    public Boy(final World world, final float x, final float y, boolean hasGun,char sex){
        this.hasGun = hasGun;
        this.world = world;
        bulletList = new ArrayList<Bullet>();

        if(!hasGun)
            state = State.IDLE;
        else
            state = State.IDLEG;

        if(sex == 'F')
            sprite = SpriteLoader.getSprite("images/Girl.json");
        else
            sprite = SpriteLoader.getSprite("images/test.json");

        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite sprite) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x , y);

                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL * x,
                        GameScreen.M_PER_PIXEL * y);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!",cause);
            }
        });
    }
    public Layer layer(){
        return sprite.layer();
    }
    private Body initPhysicsBody(World world,float x,float y){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        GameScreen.bodies.put(body, "Boy");
        //GameScreen.count++;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.layer().width() * GameScreen.M_PER_PIXEL / 2,
                sprite.layer().height() * GameScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.75f;
        fixtureDef.restitution = 0.0f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }
    public void update(int delta){
        if(hasLoaded == false){
            return;
        }

        e = e + delta;
        if (e > 150) {
            switch (state) {
                case IDLE:
                    offset = 0;
                    break;
                case RUN:
                    offset = 4;
                    break;
                case ATTK:
                    offset = 8;
                    break;
                case JUMP:
                    offset = 12;
                    break;
                case IDLEG:
                    offset = 16;
                    break;
                case RUNG:
                    offset = 20;
                    break;
                case ATTKG:
                    offset = 24;
                    break;
                case JUMPG:
                    offset = 28;
                    break;
            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);

            sprite.setSprite(spriteIndex);
            e = 0;
        }

        if(hasGun == false){
            PlayN.keyboard().setListener(new Keyboard.Adapter(){
                @Override
                public void onKeyDown(Keyboard.Event event) {
                    if(event.key() == Key.A){
                        checkKey = CheckKey.LEFT;
                        System.out.println("LEFT " + checkKey);
                        state = State.RUN;
                        walk();
                    }else if(event.key() == Key.D){
                        checkKey = CheckKey.RIGHT;
                        System.out.println("RIGHT " + checkKey);
                        state = State.RUN;
                        walk();
                    }else if(event.key() == Key.W && checkJump == false) {
                        state = State.JUMP;
                        body.applyForce(new Vec2(0f, -500f), body.getPosition());
                        checkJump =true;
                    }else if(event.key() == Key.S){
                        if (checkKey == CheckKey.LEFT)
                            state = State.IDLE;
                        else
                            state = State.IDLE;
                        System.out.println("Jump" + checkKey);
                        body.applyForce(new Vec2(0f, 500f), body.getPosition());
                    }else if (event.key() == Key.J){
                        System.out.println("ATTK" + checkKey);
                        if(checkKey == CheckKey.LEFT)
                            state = State.ATTK;
                        else
                            state = State.ATTK;
                        body.applyForce(new Vec2(25f,0f),body.getPosition());
                        //body.applyLinearImpulse(new Vec2(0f,-500f),new Vec2(0f,-500f));
                    }
                }

                @Override
                public void onKeyUp(Keyboard.Event event) {
                    state = State.IDLE;
                }
            });


        }
        else{
//            System.out.println("Boy has Gun");
            PlayN.keyboard().setListener(new Keyboard.Adapter(){
                @Override
                public void onKeyDown(Keyboard.Event event) {
                    if(event.key() == Key.A){
                        checkKey = CheckKey.LEFT;
                        System.out.println("LEFT " + checkKey);
                        state = State.RUNG;
                        walk();
                    }else if(event.key() == Key.D){
                        checkKey = CheckKey.RIGHT;
                        System.out.println("RIGHT " + checkKey);
                        state = State.RUNG;
                        walk();
                    }else if(event.key() == Key.W && checkJump == false) {
                        state = State.JUMPG;
                        body.applyForce(new Vec2(0f, -500f), body.getPosition());
                        checkJump = true;
                    }else if(event.key() == Key.S){
                        if (checkKey == CheckKey.LEFT)
                            state = State.IDLEG;
                        else
                            state = State.IDLEG;
                        System.out.println("Jump" + checkKey);
                        body.applyForce(new Vec2(0f, 500f), body.getPosition());
                    }else if (event.key() == Key.J){
                        System.out.println("ATTK" + checkKey);
                        state = State.ATTKG;

                        Bullet bullet = new Bullet(world,(body.getPosition().x + 1.5f ) / GameScreen.M_PER_PIXEL,
                                body.getPosition().y  / GameScreen.M_PER_PIXEL);

                        gameScreen.addBullet(bullet);
                    }
                }

                @Override
                public void onKeyUp(Keyboard.Event event) {
                    state = State.IDLEG;
                }
            });
        }

    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                body.getPosition().x / GameScreen.M_PER_PIXEL - 10,
                body.getPosition().y / GameScreen.M_PER_PIXEL
        );
        switch (state){
            case RUN:
                walk();
                break;
            case RUNG:
                walk();
        }
        //sprite.layer().setRotation(body.getAngle());
    }
    public void walk(){
        switch (checkKey){
            case RIGHT:
                body.applyForce(new Vec2(10f,0f),body.getPosition());
                break;
            case LEFT:
                body.applyForce(new Vec2(-10f,0f),body.getPosition());
        }
    }

    public Body getBody() {
        return body;
    }

    public void updateHasGun(boolean e){
        hasGun = e;
    }
}
