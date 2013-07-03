package de.jdsoft.stranded.map.universe;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;

public class Planet implements Disposable, CelestialBody {

    final public ModelInstance planetModel;
    private float time = 0.f;

    private Vector3 position;
    private Matrix4 rotation;

    private Vector3 orbit = new Vector3(0, 0, 0);

    // Temp
    Vector3 tmpVec1 = new Vector3();
    private float orbitAngle = 0.f;
    private Quaternion quat;
    private CelestialBody orbitObject = null;
    public boolean isDragged = false;
    private float speed = 10.0f;

    public Planet() {
        super();

        planetModel = PlanetModel.create();

        position = new Vector3(0.f, 0.f, 0.f);
        rotation = new Matrix4();
        quat = new Quaternion();
    }

    @Override
    public void setPosition( Vector3 position ) {
        this.position = position;
//        planetModel.transform.setTranslation(position);
    }

    @Override
    public void getPosition(Vector3 out) {

        planetModel.transform.getTranslation(out);
//        out.set(position);
    }

    @Override
    public void translate( Vector3 vector ) {
        this.position.add(vector);
    }

    @Override
    public void translate( float x, float y, float z ) {
        this.position.add(x, y, z);
    }

    @Override
    public void rotate( Vector3 axis, float angle ) {
        rotation.rotate(axis, angle);
    }

    public float getRadius() {
        return planetModel.transform.getValues()[Matrix3.M00] * 0.5f;
    }


    @Override
    public void update(float delta) {
        time += delta;
        orbitAngle += delta*speed;

        // Set time attribute for shader
        FloatAttribute flattr = (FloatAttribute)(planetModel.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;

        // Rotate around given center
        Vector3 axis = new Vector3(0.0f, 0.2f, 0.5f);
        System.out.println(this + ": " + position);


        Vector3 bla = new Vector3(position);

        if( this.orbitObject != null ) {
            orbitObject.getPosition(tmpVec1);
            planetModel.transform.setToTranslation(tmpVec1);
//            bla.add(tmpVec1);
        } else {
            planetModel.transform.setToTranslation(orbit);
//            bla.add(orbit);
        }
        planetModel.transform.rotate(axis, orbitAngle); // == rotate to
//        position.rotate(axis, delta*speed);
        planetModel.transform.translate(bla);

        // Rotate around own axes
//        rotation.getRotation(quat);
//        planetModel.transform.rotate(quat);
    }

    public void dispose() {

    }

    public void setOrbit(Vector3 orbit) {
        this.orbit = orbit;
    }

    public void setOrbit(CelestialBody orbit) {
        this.orbitObject = orbit;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
