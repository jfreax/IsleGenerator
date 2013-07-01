package de.jdsoft.stranded.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class GlobalInput implements InputProcessor {

    private final InputMultiplexer inputMulti;

    public GlobalInput() {
        super();

        inputMulti = new InputMultiplexer();
        inputMulti.addProcessor(this);
        Gdx.input.setInputProcessor(inputMulti);
    }

    public void addProcessor(InputProcessor processor) {
        inputMulti.addProcessor(processor);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
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
