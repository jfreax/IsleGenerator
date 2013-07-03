package de.jdsoft.stranded.map;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.input.GlobalInput;
import de.jdsoft.stranded.map.universe.CelestialBody;
import de.jdsoft.stranded.map.universe.Planet;
import de.jdsoft.stranded.model.PlanetModel;
import de.jdsoft.stranded.render.shader.PlanetShaderProvider;

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
    public Planet createPlanet( Vector3 position, Vector3 orbit, Vector3 orbitAxis, float speed) {
        Planet planet = createPlanet(position);
        planet.setOrbit(orbit);
        planet.setSpeed(speed);


        return planet;
    }

    /**
     * Create a new planet that orbits another celestial body
     * @param position
     * @param orbit
     * @return
     */
    public Planet createPlanet( Vector3 position, CelestialBody orbit, float speed) {
        Planet planet = createPlanet(position);
        planet.setOrbit(orbit);
        planet.setSpeed(speed);

        return planet;
    }

    public void render( float delta, Camera cam ) {
        time += delta;


        // Render all suns
        // TODO

        // Render all planets
//        for( int i = planets.size()-1; i >= 0; i--) {
//            planets.get(i).update(delta);
//
//            modelBatch.begin(cam);
//            modelBatch.render(planets.get(i).planetModel, lights);
//            modelBatch.end();
//        }
        for( Planet planet : planets ) {
            planet.update(delta);

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

        switch ( keycode ) {
            case Input.Keys.H:
                for( Planet planet : planets ) {
                    PlanetModel model = (PlanetModel)planet.planetModel.userData;
                    model.setRenderHeightMap();
                }
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch ( keycode ) {
            case Input.Keys.H:
                for( Planet planet : planets ) {
                    PlanetModel model = (PlanetModel)planet.planetModel.userData;
                    model.setRenderTexture();
                }
                break;
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
                System.out.println("Intersect! " + planet + " at " + intersectPosition);
                lastIntersectionActionPos = intersectPosition;
                intersectedWith = planet;
                planet.isDragged = true;
                planet.translate(1.0f, 0, 0);
                return true;
            }
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        oldDragged.set(-1, -1);

        if( intersectedWith instanceof Planet ) {
            ((Planet)intersectedWith).isDragged = false;
        }
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
                            planet.translate(delta.x, delta.y, delta.z);
                        } else {
                            delta.sub(intersection);
                            planet.rotate(Vector3.X, 0.51f);
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
