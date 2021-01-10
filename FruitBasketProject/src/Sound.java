
import java.applet.Applet;
import java.applet.AudioClip;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eylül
 */
public class Sound {
        public static final AudioClip BALL = Applet.newAudioClip(Sound.class.getResource("pokemonn.wav"));
	public static final AudioClip GAMEOVER = Applet.newAudioClip(Sound.class.getResource("ending.wav"));
	public static final AudioClip BACKGROUND = Applet.newAudioClip(Sound.class.getResource("background.wav"));
}
