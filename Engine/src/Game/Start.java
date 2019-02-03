package Game;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import CoreEngine.CoreEngine;
import MathUtil.Vector3f;
import Model.Vertex;
import Util.Util;


public class Start {
	
	public static void main(String[] args) {
		
		CoreEngine new_game = CoreEngine.get_engine();
		new_game.init_engine(800, 600, false);
		new_game.init_game(new TestGame2(), true, 60, false);
	}

}
