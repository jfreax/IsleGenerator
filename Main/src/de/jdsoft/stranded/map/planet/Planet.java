package de.jdsoft.stranded.map.planet;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;

public class Planet implements Disposable {

    final public ModelInstance planetModel;
    private float time = 0.f;
    private Matrix4 transform = new Matrix4();

    // temp. update manually!
    private Vector3 position = new Vector3(0.f, 0.f, 0.f);

    private float angle = 0.1f;
    private Vector3 orbit = new Vector3(0, 0, 0);

    // Temp
    Vector3 tmpVec = new Vector3();
    private float orbitAngle = 0.f;

    public Planet() {
        planetModel = PlanetModel.create();
//        planetModel.calculateTransforms();

    }


    public void setPosition( Vector3 position ) {
        this.position = position;
    }

    public void getPosition(Vector3 position) {
        position = this.position;
    }

    public float getRadius() {
        return planetModel.transform.getValues()[Matrix3.M00] * 0.5f;
    }


    public void update(float delta) {
        time += delta;
        orbitAngle += delta*70;

        FloatAttribute flattr = (FloatAttribute)(planetModel.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;


        // Rotate around given center
        Vector3 axis = new Vector3(0.0f, 0.0f, 1.0f);
//        planetModel.transform.getTranslation(position);


        planetModel.transform.setToTranslation(orbit);
        planetModel.transform.rotate(axis, orbitAngle);
        planetModel.transform.translate(position);

        // Rotate around own axes
        planetModel.transform.rotate(axis, orbitAngle *0.3f);


    }

    public void dispose() {

    }

    public void setOrbit(Vector3 orbit) {
        this.orbit = orbit;
    }
}
