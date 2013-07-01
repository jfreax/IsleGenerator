package de.jdsoft.stranded.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.map.entity.Tile;
import de.jdsoft.stranded.map.TileManager;
import de.jdsoft.stranded.model.SphereBuilder;
import de.jdsoft.stranded.render.material.TimeAttribute;
import de.jdsoft.stranded.render.shader.PlanetShader;
import de.jdsoft.stranded.utils.BlurUtils;

import java.util.List;

public class DrawAsTexture implements Screen {

    private final ModelInstance testPlanetEffect;
    private final ModelBatch modelBatchPlanetEffect;
    TileManager tileManager;
    int mapWidth = 512;
    int mapHeight = 512;

    PerspectiveCamera cam;
    CameraInputController camController;

    private Texture texture;
    private Pixmap heightmap;

    private Lights lights;
    public ModelBatch modelBatch;
    public Model planetModel;

    public ModelInstance testPlanet;


    @Override
    public void show() {

    }

    public DrawAsTexture() {
        super();

        // Set camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 0f, 20f);
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


        // Create new tilemanager and create new random map
        tileManager = new TileManager(mapWidth, mapHeight);

        // Create texture
        computeTexture();

        // Create height map
        computeHeightMap();

        // Test light source
        lights = new Lights();
        lights.ambientLight.set(0.1f, 0.1f, 0.1f, 1f);
        lights.add(new DirectionalLight().set(1.f, 1.f, 1.f, -1f, -0.8f, -0.2f));


        // Create planet mesh
        Vector3 planetModelSize = new Vector3(10.f, 10.f, 10.f);
        long attr = VertexAttributes.Usage.Position
                  | VertexAttributes.Usage.ColorPacked
                  | VertexAttributes.Usage.TextureCoordinates
                  | VertexAttributes.Usage.Normal;

        planetModel = SphereBuilder.createNew(texture, heightmap, "0", attr, planetModelSize.x, planetModelSize.y, planetModelSize.z, 100, 50);


        // Create mesh for planet effect
        modelBatch = new ModelBatch(Gdx.files.internal("shader/default.vertex.glsl"), Gdx.files.internal("shader/planet.fragment.glsl"));
        modelBatchPlanetEffect = new ModelBatch(new BaseShaderProvider() {
            @Override
            protected Shader createShader (Renderable renderable) {
                return new PlanetShader();
            }
        });
        ModelBuilder modelBuilder = new ModelBuilder();
        Model planetEffectModel = modelBuilder.createRect(
                -planetModelSize.x, -planetModelSize.y, 0.f,
                 planetModelSize.x, -planetModelSize.y, 0.f,
                 planetModelSize.x,  planetModelSize.y, 0.f,
                -planetModelSize.x,  planetModelSize.y, 0.f,
                0.f, 0.f, 1.f,
                new Material(new TimeAttribute(0.5f)),
                attr
        );
//        planetModel = modelBuilder.createSphere(10.f, 10.f, 10.f, 50, 50,
//                new Material( new TextureAttribute(TextureAttribute.Diffuse, texture)),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        testPlanet = new ModelInstance(planetModel);
        testPlanetEffect = new ModelInstance(planetEffectModel);


//        testPlanet.materials.add(new Material(new TimeAttribute(0.8f)));
//        testPlanet.materials.add(new Material(new BlendingAttribute(GL10.GL_ZERO, GL10.GL_ZERO)));

    }

    float rotate = 0.f;
    float rotateY = 0.f;
    float time = 0.f;


    @Override
    public void render(float delta) {
        rotate /= 1.1f;
        rotateY /= 1.1f;

        time += delta;

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            TextureAttribute tattr = (TextureAttribute)(testPlanet.materials.first().get(TextureAttribute.Diffuse));
            tattr.textureDescription.set(texture, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_REPEAT, GL10.GL_REPEAT);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            Texture texture2 = new Texture(heightmap);
            TextureAttribute tattr = (TextureAttribute)(testPlanet.materials.first().get(TextureAttribute.Diffuse));
            tattr.textureDescription.set(texture2, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_LINEAR_MIPMAP_LINEAR, GL10.GL_REPEAT, GL10.GL_REPEAT);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotate -= 0.2f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotate += 0.2f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            rotateY -= 0.2f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            rotateY += 0.2f;
        }

        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        testPlanet.transform.rotate(Vector3.Y, rotate);
        testPlanet.transform.rotate(Vector3.X, rotateY);

        FloatAttribute flattr = (FloatAttribute)(testPlanet.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;

        TimeAttribute tattr = (TimeAttribute)(testPlanetEffect.materials.first().get(TimeAttribute.ID));
        tattr.value = time;


        modelBatchPlanetEffect.begin(cam);
        modelBatchPlanetEffect.render(testPlanetEffect);
        modelBatchPlanetEffect.end();


        //instance.userData
        modelBatch.begin(cam);
        modelBatch.render(testPlanet, lights);
        modelBatch.end();





    }

    private void computeTexture() {

        Pixmap pixmap;
        pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);

        List<Tile> tiles = tileManager.getTiles();
        for( Tile tile : tiles) {

            pixmap.setColor(tile.getColor(tileManager));

            List<Point> points = tile.getPoints();
            Point startPoint = points.get(0);
            for( int i = 1; i < points.size()-1; i++ ) {
                pixmap.fillTriangle(
                        (int)startPoint.x,      (int)startPoint.y,
                        (int)points.get(i).x,   (int)points.get(i).y,
                        (int)points.get(i+1).x, (int)points.get(i+1).y);
            }
        }

        pixmap = BlurUtils.blur(pixmap, 1, 1, true);

        // Create a texture to contain the pixmap
        //texture = new Texture(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture = new Texture(pixmap, true);

        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        //blurred.dispose();
        pixmap.dispose();

    }

    private void computeHeightMap() {

        heightmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);

        float maxHeight = tileManager.getMaxHeight();

        List<Tile> tiles = tileManager.getTiles();
        for( Tile tile : tiles) {

            float height = tile.getHeight() / maxHeight;
//            heightmap.setColor(0, 0, 0, height);
            heightmap.setColor(height, height, height, height);

            //pixmap.setColor(tile.getColor(tileManager));

            List<Point> points = tile.getPoints();
            Point startPoint = points.get(0);
            for( int i = 1; i < points.size()-1; i++ ) {
                heightmap.fillTriangle(
                        (int)startPoint.x,      (int)startPoint.y,
                        (int)points.get(i).x,   (int)points.get(i).y,
                        (int)points.get(i+1).x, (int)points.get(i+1).y);
            }
        }

        heightmap = BlurUtils.blur(heightmap, 3, 2, true);

//        texture = new Texture(blurred, true);

//        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
//        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


//        heightmap = new Texture(pixmap, true);

//        heightmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //heightmap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

//        pixmap.dispose();
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
        heightmap.dispose();
        texture.dispose();
    }

}
