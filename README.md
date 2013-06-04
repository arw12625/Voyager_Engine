Voyager is a first attempt at creating a game engine utilising java, lwjgl, and slick. It serves as an experiment and exploration into the world of game programming.

As of now, Voyager has primitave support 2D & 3D graphics, physics, scripting, input, and audio.

2D Graphics - Simple interface for creating 2D graphics. Textures and transparency are supported. Fonts are implemented using the Slick Angelcode font.

3D Graphics - Support for loading 3D models in the Wavefront OBJ format 3D models are rendered using opengl vertex buffer objects. Texturing is not yet implemented.

Physics - Physical objects are governed by Newtonian physics. Collision detection and resolution are not yet implemented.

Scripting - Scripting is enabled through the use of the Rhino Javascript Engine. Scripts may be executed from the a text file or through the primitive console.

Input - Inputs are implemented with the use of lwjgl input classes. A wrapper for the mouse and keyboard add functionality.

Audio - Primitave support for audio is implemented with openal. Sound can be loaded from a wav file.

Voyager is a work in progress that will eventually support: fully capable physics engine including collision support for textured and animated models scripting utilities 2D input components AI support Functional scene graph and developer tools