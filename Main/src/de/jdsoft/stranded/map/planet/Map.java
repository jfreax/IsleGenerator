package de.jdsoft.stranded.map.planet;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;
import de.jdsoft.stranded.render.shader.PlanetShaderProvider;

import java.util.LinkedList;
import java.util.List;

public class Map implements Disposable {

    private final ModelBatch modelBatch;
    private final Lights lights;
    private List<Planet> planets;
    private float time = 0.0f;


    public Map() {
        planets = new LinkedList<Planet>();
        modelBatch = new ModelBatch(new PlanetShaderProvider());

        // Test light source, later, suns should be the light sources
        lights = new Lights();
        lights.ambientLight.set(0.1f, 0.1f, 0.1f, 1f);
        lights.add(new DirectionalLight().set(1.f, 1.f, 1.f, -1f, -0.8f, -0.2f));

    }

    public Planet createPlanet() {
        Planet planet = new Planet();
        planets.add(planet);

        return planet;
    }

    public void render( float delta, Camera cam ) {
        time += delta;

        // Render all suns
        // TODO

        // Render all planets
        for( Planet planet : planets ) {
            planet.setTime(time);

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
}
