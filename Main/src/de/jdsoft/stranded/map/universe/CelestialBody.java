package de.jdsoft.stranded.map.universe;


import com.badlogic.gdx.math.Vector3;

public interface CelestialBody {

    public void update(float delta);

    public void setPosition( Vector3 position );
    public void getPosition(Vector3 out);

    public void translate( Vector3 vector );
    public void translate( float x, float y, float z );
    public void rotate( Vector3 axis, float angle );
}
