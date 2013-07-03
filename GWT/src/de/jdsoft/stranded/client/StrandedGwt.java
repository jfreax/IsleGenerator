package de.jdsoft.stranded.client;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import de.jdsoft.stranded.Stranded;

public class StrandedGwt extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(320, 480);
        return config;
    }

    @Override
    public ApplicationListener getApplicationListener () {
        return new Stranded();
    }
}
