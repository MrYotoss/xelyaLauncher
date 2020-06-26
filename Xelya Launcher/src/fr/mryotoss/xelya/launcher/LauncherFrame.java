package fr.mryotoss.xelya.launcher;



import javax.swing.JFrame;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;
import re.alwyn974.openlauncherlib.util.CrashReporter;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	private  LauncherPanel launcherPanel;
	private static CrashReporter crashReporter;
	
	public LauncherFrame()
	{
		this.setTitle("Xelya Launcher");
		this.setSize(1280, 720);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage (Swinger.getResource("icon.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		this.setVisible(true);
	}

	public static void main(String[] args) 
	{
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/mryotoss/xelya/launcher/resources/");
		Launcher.XA_CRASHES_DIR.mkdirs();
		crashReporter = new CrashReporter("Xelya Launcher", Launcher.XA_CRASHES_DIR);
		
		instance = new LauncherFrame();

	}
	
	public static LauncherFrame getInstance( )
	{
		return instance;
	}
	
	public static CrashReporter getCrashReporter()
	{
		return crashReporter;
	}

	
	public LauncherPanel getLauncherPanel()
	{
		return this.launcherPanel;
	}
}
