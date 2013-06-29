#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
uniform sampler2D u_texture;

#ifdef GL_ES
varying mediump vec2 v_texCoords;
#else
varying vec2 v_texCoords;
#endif

void main() {
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}