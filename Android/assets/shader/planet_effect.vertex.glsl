attribute vec4 a_position;
uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;


attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
    v_color = a_color;

    v_texCoords = a_texCoord0;
    //gl_Position = vec4(0);
    gl_Position = (u_projTrans * u_worldTrans * a_position);
}
