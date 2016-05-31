package event.project.characters;

import event.project.core.GameScreen;
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

import java.util.concurrent.TimeUnit;

/**
 * Created by aphis on 02-Apr-16.
 */
public class Zombie2 {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private Body body;
    public int countATTK = 0;

    public enum State{
        IDLE,WALK,DIE;
    }
    public State state = State.IDLE;
    public int e = 0;
    public int offset = 4;

    public Zombie2(final World world,final float x,final float y){
        //walk(walk);

        sprite = SpriteLoader.getSprite("images/zombie2.json");
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

        GameScreen.bodies.put(body, "zombie"); //_" + GameScreen.count);
        GameScreen.count++;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(56 * GameScreen.M_PER_PIXEL / 2,
                sprite.layer().height() * GameScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.0f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }
    public void update(int delta) {
        if (hasLoaded == false) {
            return;
        }

        System.out.println("offset =  " + offset);

        e = e + delta;
        if (e > 150) {
            switch (state) {
                case IDLE:
                    offset = 0;
                    break;
                case WALK:
                    offset = 4;
                    break;
                case DIE:
                    offset = 8;
                    if (spriteIndex == 11) {
                        state = State.IDLE;
                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                body.getPosition().x / GameScreen.M_PER_PIXEL - 10,
                body.getPosition().y / GameScreen.M_PER_PIXEL
        );
        //sprite.layer().setRotation(body.getAngle());
    }
    public void walk(boolean walk)
    {
        if(walk) {
            state = State.WALK;
            body.applyForce(new Vec2(-10f, 0f), body.getPosition());
        }else{
            state = State.IDLE;
        }

    }
    public Body getBody() {
        return body;
    }
}
