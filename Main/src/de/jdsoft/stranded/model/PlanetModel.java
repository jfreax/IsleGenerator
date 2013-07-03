package de.jdsoft.stranded.model;


import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.map.planet.TileManager;
import de.jdsoft.stranded.map.planet.entity.Tile;

import java.util.List;

public class PlanetModel implements Disposable {
//    private final Texture textureHeightmap;
    TileManager tileManager;
    final int mapWidth = 512;
    final int mapHeight = 512;

    public Texture texture;
    public Texture heightmapTexture;
    public Pixmap heightmap;
    private Model planetModel;

    private static PlanetModel planet = null;

    static public ModelInstance create() {
        if( planet == null ) {
            planet = new PlanetModel();
        }

        ModelInstance ret = new ModelInstance(planet.planetModel);
        ret.userData = planet;

        return ret;
    }


    static public void disposeAll() {
        planet.dispose();
    }


    private PlanetModel() {

        // Create new tilemanager and create new random map
        tileManager = new TileManager(mapWidth, mapHeight);

        // Create texture
        computeTexture();

        // Create height map
        computeHeightMap();


        // Create planet mesh
        final Vector3 planetModelSize = new Vector3(1.f, 1.f, 1.f);
        final long attr = VertexAttributes.Usage.Position
                | VertexAttributes.Usage.ColorPacked
                | VertexAttributes.Usage.TextureCoordinates
                | VertexAttributes.Usage.Normal;

        //planetModel = SphereBuilder.createNew(texture, heightmap, "0", attr, planetModelSize.x, planetModelSize.y, planetModelSize.z, 100, 50);

//        textureHeightmap = new Texture(heightmap);
        final Material planetMaterial = new Material( "planet",
                  new TextureAttribute(TextureAttribute.Diffuse, texture)
                , new FloatAttribute(FloatAttribute.Shininess, 0.f)
                , new BlendingAttribute(GL10.GL_ONE, GL10.GL_ZERO)
        );

        // Planet mesh
        Mesh mesh = SphereBuilder.createNewMesh("0", attr, planetModelSize.x, planetModelSize.y, planetModelSize.z, 100, 50, heightmap);


        // Create mesh for planet effect
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshBuilder meshBuilder = new MeshBuilder();
        meshBuilder.begin(attr);
        meshBuilder.part("atmosphere", GL10.GL_TRIANGLES);
        meshBuilder.rect(
                -planetModelSize.x, -planetModelSize.y, 0.f,
                planetModelSize.x, -planetModelSize.y, 0.f,
                planetModelSize.x,  planetModelSize.y, 0.f,
                -planetModelSize.x,  planetModelSize.y, 0.f,
                0.f, 0.f, 1.f
        );
        Mesh effectMesh = meshBuilder.end();

        // Build model
        modelBuilder.begin();
        modelBuilder.part("planet", mesh, GL10.GL_TRIANGLES, planetMaterial);
        modelBuilder.part("atmosphere", effectMesh, GL10.GL_TRIANGLES, new Material("atmosphere") );
        planetModel = modelBuilder.end();

    }

    @Override
    public void dispose() {
        texture.dispose();
        heightmap.dispose();
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

        //pixmap = BlurUtils.blur(pixmap, 1, 1, true);

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

        //heightmap = BlurUtils.blur(heightmap, 3, 2, true);
        heightmapTexture = new Texture(heightmap, true);
    }

    public void setRenderHeightMap() {
        TextureAttribute textureAttribute = (TextureAttribute)planet.planetModel.getMaterial("planet").get(TextureAttribute.Diffuse);
        textureAttribute.textureDescription.set(heightmapTexture,
                GL10.GL_LINEAR, GL10.GL_LINEAR,
                GL10.GL_REPEAT, GL10.GL_REPEAT);
    }

    public void setRenderTexture() {
        TextureAttribute textureAttribute = (TextureAttribute)planet.planetModel.getMaterial("planet").get(TextureAttribute.Diffuse);
        textureAttribute.textureDescription.set(texture,
                GL10.GL_LINEAR, GL10.GL_LINEAR,
                GL10.GL_REPEAT, GL10.GL_REPEAT);
    }
}
