package de.jdsoft.stranded.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.Entity.Tile;
import de.jdsoft.stranded.Map.TileManager;

import java.util.List;

public class DrawAsTexture implements Screen {

    TileManager tileManager;

    PerspectiveCamera cam;
    CameraInputController camController;

    Pixmap pixmap;
    Texture texture;
    SpriteBatch batch;
    TextureRegion region;

    private OrthographicCamera oCam;



    @Override
    public void show() {
        // Set camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0.f, 0.f, 10f);
        //cam.lookAt(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getHeight() / 2);
        cam.lookAt(0.f, 0.f, 0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // Initialize camera controler
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Enable opengl features
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);

        // Enable face culling- be careful with spriteBatch, might cull sprites as well!
        //Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

        // What faces to remove with the face culling.
        //Gdx.graphics.getGL20().glCullFace(GL20.GL_FRONT);


        // Create new tilemanager and create new random map
        int mapWidth = 512;
        int mapHeight = 512;

        tileManager = new TileManager(mapWidth, mapHeight);

        // Initialize
        pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);

        // Create a texture to contain the pixmap
        texture = new Texture(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

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

        // Blit the composited overlay to a texture
        texture.draw(pixmap, 0, 0);
        batch = new SpriteBatch();

        pixmap.dispose();
    }


    @Override
    public void render(float delta) {
        camController.update();
        cam.update();

        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        cam.viewportHeight = height;
        cam.viewportWidth = width;
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
    }

}
