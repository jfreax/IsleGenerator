package de.jdsoft.stranded;

import com.badlogic.gdx.Game;
import de.jdsoft.stranded.Screens.DrawAllTest;
import de.jdsoft.stranded.Screens.DrawAsTexture;


public class Main extends Game {

    @Override
    public void create() {
        //this.setScreen(new DrawAllTest());
        this.setScreen(new DrawAsTexture());

    }

    @Override
    public void render() {
        super.render();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
