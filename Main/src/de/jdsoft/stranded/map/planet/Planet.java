package de.jdsoft.stranded.map.planet;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;

public class Planet implements Disposable {

    final public ModelInstance planetModel;
    private float time = 0.f;
    private Matrix4 transform = new Matrix4();

    private Vector3 position;
    private Matrix4 rotation;

    private float angle = 0.1f;
    private Vector3 orbit = new Vector3(0, 0, 0);

    // Temp
    Vector3 tmpVec = new Vector3();
    private float orbitAngle = 0.f;
    private Quaternion quat;

    public Planet() {
        planetModel = PlanetModel.create();
//        planetModel.calculateTransforms();


        position = new Vector3(0.f, 0.f, 0.f);
        rotation = new Matrix4();
        quat = new Quaternion();
    }


    public void setPosition( Vector3 position ) {
        this.position = position;
    }

    public void getPosition(Vector3 out) {
        out.set(this.position);
    }

    public void translate( Vector3 vector ) {
        this.position.add(vector);
    }

    public void translate( float x, float y, float z ) {
        this.position.add(x, y, z);
    }

    public void rotate( Vector3 axis, float angle ) {
        rotation.rotate(axis, angle);
    }

    public float getRadius() {
        return planetModel.transform.getValues()[Matrix3.M00] * 0.5f;
    }


    public void update(float delta) {
        time += delta;
        orbitAngle += delta*70;
//        orbitAngle = 0;

        FloatAttribute flattr = (FloatAttribute)(planetModel.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;

//        rotate(Vector3.X, 0.51f);


        // Rotate around given center
        Vector3 axis = new Vector3(0.0f, 0.0f, 1.0f);
//        planetModel.transform.getTranslation(position);



        planetModel.transform.setToTranslation(orbit);
        planetModel.transform.rotate(axis, 0.f);
        planetModel.transform.translate(position);

        // Rotate around own axes
//        planetModel.transform.rotate(axis, orbitAngle * 0.1f);



        rotation.getRotation(quat);
//        System.out.println("Quat: " + quat);
//        quat.set(Vector3.X, orbitAngle*0.1f);
        planetModel.transform.rotate(quat);


    }

    public void dispose() {

    }

    public void setOrbit(Vector3 orbit) {
        this.orbit = orbit;
    }
}
