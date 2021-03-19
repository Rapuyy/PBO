package FP;

import java.applet.Applet;      
import java.applet.AudioClip;

public class Sound {
	public static final AudioClip SCORE = Applet.newAudioClip(Sound.class.getResource("test.wav"));
	public static final AudioClip OPENIGN = Applet.newAudioClip(Sound.class.getResource("pat.wav"));
	public static final AudioClip SAD = Applet.newAudioClip(Sound.class.getResource("sad.wav"));
}
