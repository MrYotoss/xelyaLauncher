package fr.mryotoss.xelya.launcher;

import java.io.File;
import java.util.Arrays;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.openlauncherlib.external.ExternalLaunchProfile;
import re.alwyn974.openlauncherlib.external.ExternalLauncher;
import re.alwyn974.openlauncherlib.minecraft.AuthInfos;
import re.alwyn974.openlauncherlib.minecraft.GameFolder;
import re.alwyn974.openlauncherlib.minecraft.GameInfos;
import re.alwyn974.openlauncherlib.minecraft.GameTweak;
import re.alwyn974.openlauncherlib.minecraft.GameType;
import re.alwyn974.openlauncherlib.minecraft.GameVersion;
import re.alwyn974.openlauncherlib.minecraft.MinecraftLauncher;





public class Launcher 
{
	public static final GameVersion XA_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos XA_INFOS = new GameInfos("Xelya", XA_VERSION, new GameTweak[] {GameTweak.FORGE});
	public static final File XA_DIR = XA_INFOS.getGameDir();
	public static final File XA_CRASHES_DIR = new File(XA_DIR, "crashes");
	
	private static AuthInfos authInfos;
	private static Thread updateThread;
	

	
	public static void auth(String username, String password) throws AuthenticationException
	{
		authInfos = new AuthInfos(username, "sry", "nope");
	}
		
	public static void update() throws Exception
	{
		SUpdate su = new SUpdate("http://xelya.craftaserv.fr/launcher/", XA_DIR);
		su.addApplication(new FileDeleter());
		updateThread = new Thread()
				{
				private int val;
				private int max;
				
				@Override
				public void run()
				{
					while(!this.isInterrupted())
					{
						if(BarAPI.getNumberOfFileToDownload() == 0)
						{
							LauncherFrame.getInstance().getLauncherPanel().setInfoText("Verification des fichiers");
							continue;
						}
						val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
						max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
						
						LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
						LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
						
						LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des fichier " + 
								BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " +
									Swinger.percentage(val, max) + "%");
					}
				}
			};
			updateThread.start();
			su.start();
			updateThread.interrupt();
	}
	

	public static void launch() throws LaunchException
	{
		
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(XA_INFOS, GameFolder.BASIC, authInfos);
		ExternalLauncher launcher = new ExternalLauncher(profile);
		profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getrRamSelector().getRamArguments()));
		LauncherFrame.getInstance().setVisible(false);
		
		launcher.launch();
		

		System.exit(0);
	}
	
	public static void interuptTread()
	{
		updateThread.interrupt();
	}
	


}
