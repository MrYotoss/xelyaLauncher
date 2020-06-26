package fr.mryotoss.xelya.launcher;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;
import static fr.theshark34.swinger.Swinger.getResource;
import static fr.theshark34.swinger.Swinger.getTransparentWhite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.openlauncherlib.util.Saver;
import re.alwyn974.openlauncherlib.util.ramselector.RamSelector;






@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener
{
	
	private Image background = getResource("background.png");
	
	private Saver saver = new Saver(new File(Launcher.XA_DIR, "launcher.properties"));
	
	private JTextField usernameField = new JTextField(saver.get("username"));
	//private JPasswordField passwordField = new JPasswordField();
	
	private STexturedButton playButton = new STexturedButton(getResource("play.png"));
	private STexturedButton quitButton = new STexturedButton(getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"));
	
	private STexturedButton ramButton = new STexturedButton(getResource("ram.png"));

	
	private SColoredBar progressBar = new SColoredBar(getTransparentWhite(100), getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);
	
	private RamSelector ramSelector = new RamSelector(new File(Launcher.XA_DIR, "ram.txt"));
	
	
	public LauncherPanel()
	{
		this.setLayout(null);
		
		usernameField.setForeground(Color.WHITE);
		usernameField.setFont(usernameField.getFont().deriveFont(27F));
		usernameField.setCaretColor(Color.WHITE);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(760, 256, 390, 77);
		this.add(usernameField);
		
		/*passwordField.setForeground(Color.WHITE);
		passwordField.setFont(passwordField.getFont().deriveFont(27F));
		passwordField.setCaretColor(Color.WHITE);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(768, 482, 390, 77);
		this.add(passwordField);*/
		
		playButton.setBounds(765, 604);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(1225, 5);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(1170, 5);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		progressBar.setBounds(0, 690, 1280, 30);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		infoLabel.setBounds(0, 655, 951, 35);
		this.add(infoLabel);
		
		this.ramButton.addEventListener(this);
		this.ramButton.setBounds(1115, 5);
		this.add(ramButton);
		
	}
	
	@Override
	public void onEvent (SwingerEvent e)
	{
		if(e.getSource() == playButton)
		{
			setFieldsEnabled(false);
			
			if (usernameField.getText().replaceAll(" ", "").length() == 0)
			{
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread()
					{
						
						@Override
						public void run() {
						try {
							Launcher.auth(usernameField.getText(), "");
						} catch (AuthenticationException e)
						{
							JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, Impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
							setFieldsEnabled(true);
							return;
						}
						
						saver.set("username", usernameField.getText());
						ramSelector.save();
						
						try {
							Launcher.update();
						} catch (Exception e)
						{
							Launcher.interuptTread();
							LauncherFrame.getCrashReporter().catchError(e, "Impossible de mettre a jour Xelya !");
						}
						
						try {
							Launcher.launch();
						} catch (LaunchException e)
						{
							LauncherFrame.getCrashReporter().catchError(e, "Impossible de lancer Xelya !");
						}
						
						}
					};
					t.start();
			
		} else if (e.getSource() == quitButton)	
			System.exit(0);
	 else if (e.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
	 else if (e.getSource() == ramButton)
		 ramSelector.display();
		
	}
	
	@Override
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		drawFullsizedImage(graphics, this, background);
	}
	
	private void setFieldsEnabled(boolean enabled)
	{
		usernameField.setEnabled(enabled);
		//passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar()
	{
		return progressBar;
	}
	
	public void setInfoText(String text)
	{
		infoLabel.setText(text);
	}
	
	public RamSelector getrRamSelector()
	{
		return ramSelector;
	}
	
}
