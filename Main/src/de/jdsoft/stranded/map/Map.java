package de.jdsoft.stranded.map;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.input.GlobalInput;
import de.jdsoft.stranded.map.planet.Planet;
import de.jdsoft.stranded.model.PlanetModel;
import de.jdsoft.stranded.render.shader.PlanetShaderProvider;

import javax.microedition.khronos.opengles.GL10;
import java.util.LinkedList;
import java.util.List;

public class Map implements InputProcessor, Disposable {

    private final ModelBatch modelBatch;
    private final Lights lights;
    private final PerspectiveCamera cam;
    private List<Planet> planets;
    private float time = 0.0f;


    public Map(GlobalInput globalInput, PerspectiveCamera cam) {
        this.cam = cam;
        globalInput.addProcessor(this);

        planets = new LinkedList<Planet>();
        modelBatch = new ModelBatch(new PlanetShaderProvider());

        // Test light source, later, suns should be the light sources
        lights = new Lights();
        lights.ambientLight.set(0.1f, 0.1f, 0.1f, 1f);
        lights.add(new DirectionalLight().set(1.f, 1.f, 1.f, -1f, -0.8f, -0.2f));

    }

    public Planet createPlanet( Vector3 position ) {
        Planet planet = new Planet();
        planet.setPosition(position);

        planets.add(planet);

        return planet;
    }

    /**
     * Creates new planet orbiting 'orbit' position
     * @param position
     * @param orbit
     * @return
     */
    public Planet createPlanet( Vector3 position, Vector3 orbit) {
        Planet planet = createPlanet(position);
        planet.setOrbit(orbit);

        return planet;
    }

    public void render( float delta, Camera cam ) {
        time += delta;


        // Render all suns
        // TODO

        // Render all planets
        boolean first = true;
        for( Planet planet : planets ) {
            planet.update(delta);

//            planet.planetModel.transform.rotate(Vector3.X, 1.7f);

            modelBatch.begin(cam);
            modelBatch.render(planet.planetModel, lights);
            modelBatch.end();
        }

        // Moons, Stations, Ships...
    }

    @Override
    public void dispose() {
        for( Planet planet : planets ) {
            planet.dispose();
        }

        PlanetModel.disposeAll();
    }

    @Override
    public boolean keyDown(int keycode) {
        for( Planet planet : planets ) {
            PlanetModel model = (PlanetModel)planet.planetModel.userData;

            TextureAttribute texture = (TextureAttribute)planet.planetModel.materials.first().get(TextureAttribute.Diffuse);
            texture.textureDescription.set(model.texture, GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_TEXTURE_WRAP_S, GL10.GL_TEXTURE_WRAP_S);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for( Planet planet : planets ) {
            PlanetModel model = (PlanetModel)planet.planetModel.userData;

            TextureAttribute texture = (TextureAttribute)planet.planetModel.materials.first().get(TextureAttribute.Diffuse);
            texture.textureDescription.set(model.heightmapTexture, GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_TEXTURE_WRAP_S, GL10.GL_TEXTURE_WRAP_S);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // Temp vector
    Vector3 intersectPosition = new Vector3();
    Vector3 intersection = new Vector3();
    Vector3 unprojected = new Vector3();
    Vector3 delta = new Vector3();
    Object intersectedWith = null;
    int buttonClickedLast = 0;

    // Position of mouse when object was clicked/dragged/unclicked
    Vector3 lastIntersectionActionPos = new Vector3();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        buttonClickedLast = button;

        Ray pickRay = cam.getPickRay(screenX, screenY);
        intersectedWith = null;

        for( Planet planet : planets ) {
            planet.getPosition(intersectPosition);
            if( Intersector.intersectRaySphere(pickRay, intersectPosition, planet.getRadius(), intersection) ) {
                System.out.println("Intersect!");
                lastIntersectionActionPos = intersectPosition;
                intersectedWith = planet;
                return true;
            }
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        oldDragged.set(-1, -1);
        return false;
    }


    Vector2 oldDragged = new Vector2(-1.f, 1.f);

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if( oldDragged.x == -1) {
            oldDragged.set( screenX, screenY );
        }

        boolean ret = false;

        if( intersectedWith != null ) {
            if ( intersectedWith instanceof Planet) {
                Planet planet = (Planet)intersectedWith;

                planet.getPosition(intersectPosition);
                Ray pickRay = cam.getPickRay(screenX, screenY);
                // TODO maybe use a simple camera facing plane
                if( Intersector.intersectRaySphere(pickRay, intersectPosition, planet.getRadius(), intersection) ) {
                    Ray pickRayLast = cam.getPickRay(oldDragged.x, oldDragged.y);
                    if( Intersector.intersectRaySphere(pickRayLast, intersectPosition, planet.getRadius(), delta) ) {


                        if( buttonClickedLast == Input.Buttons.RIGHT ) {
                            delta.sub(intersection);
                            planet.planetModel.transform.translate(-delta.x, -delta.y, -delta.z);
                        } else {
                            delta.sub(intersection);
                            System.out.println(delta);
                            planet.planetModel.transform.rotate(Vector3.X, 0.1f);

                        }
                        ret = true;
                    }
                }

            }
        }

        oldDragged.set( screenX, screenY );
        return ret;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
