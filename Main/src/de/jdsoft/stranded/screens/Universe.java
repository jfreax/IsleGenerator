package de.jdsoft.stranded.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.Vector3;
import de.jdsoft.stranded.map.planet.Map;
import de.jdsoft.stranded.model.PlanetModel;
import de.jdsoft.stranded.render.shader.PlanetShaderProvider;

public class Universe implements Screen {

    private final Map map;
    PerspectiveCamera cam;
    CameraInputController camController;

    @Override
    public void show() {

    }

    public Universe() {
        super();

        // Set camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 0f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // Initialize camera controler
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Enable opengl features
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
//        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_ZERO, GL20.GL_ONE);
        Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
//
//        Gdx.graphics.getGL20().glDisable(GL20.GL_DEPTH_TEST);

        //Gdx.gl.glDepthMask(false);
        //Gdx.graphics.getGL20().glDisable(GL10.GL_BLEND);


        // Enable face culling- be careful with spriteBatch, might cull sprites as well!
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

        // What faces to remove with the face culling.
        Gdx.graphics.getGL20().glCullFace(GL20.GL_FRONT);


        // Create new map
        map = new Map();
        map.createPlanet( new Vector3(3.0f, 0.f, 0.f) );
    }

    float rotate = 0.f;
    float rotateY = 0.f;
    float time = 0.f;


    @Override
    public void render(float delta) {
        rotate /= 1.1f;
        rotateY /= 1.1f;

        time += delta;

//        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
//            TextureAttribute tattr = (TextureAttribute)(testPlanet.materials.first().get(TextureAttribute.Diffuse));
//            tattr.textureDescription.set(texture, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_REPEAT, GL10.GL_REPEAT);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
//            Texture texture2 = new Texture(heightmap);
//            TextureAttribute tattr = (TextureAttribute)(testPlanet.materials.first().get(TextureAttribute.Diffuse));
//            tattr.textureDescription.set(texture2, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_REPEAT, GL10.GL_REPEAT);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            rotate -= 0.2f;
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            rotate += 0.2f;
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            rotateY -= 0.2f;
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            rotateY += 0.2f;
//        }

        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

//        testPlanet.transform.rotate(Vector3.Y, rotate);
//        testPlanet.transform.rotate(Vector3.X, rotateY);


        map.render(delta, cam);
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportHeight = height;
        cam.viewportWidth = width;

        cam.update();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        map.dispose();
    }

}
