#version 120

attribute vec3 position;
attribute vec2 textCoord;

uniform mat4 T_MVP;

out vec2 textCoord0;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
	textCoord0 = textCoord;

}