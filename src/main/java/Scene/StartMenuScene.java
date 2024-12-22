package Scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ViewportUI;

import org.w3c.dom.Text;

import PlayerClient.PlayerClient;
//import Server.ConnectToServer;
import main.GamePanel;


public class StartMenuScene extends Scene {
	private BufferedImage image;
	private boolean isDrawLogInAndQuitGameButton = true;
	private boolean isDrawInputID = false;
	private boolean isDrawSignUpMenu = false;
	// login button
	private int logInX;
	private int logInY;
	private int logInWidth;
	private int logInHeight;
	// play offline button
	private int playOffX;
	private int playOffY;
	private int playOffWidth;
	private int playOffHeight;
	// play online button
	private int playOnX;
	private int playOnY;
	private int playOnWidth;
	private int playOnHeight;
	// submit
	private int subX;
	private int subY;
	private int subWidth;
	private int subHeight;
	// dang ki sign up
	private int signupX;
	private int singupY;
	private int singupWidth;
	private int singupHeight;
	// back button
	private int backX;
	private int backY;
	private int backWidth;
	private int backHeight;
	// back button
	private int quitX;
	private int quitY;
	private int quitWidth;
	private int quitHeight;
	// input field
	private String IdText;
	private String passwordText;
	private String id;
	private String pass;
	private String level;
	// music
	private Clip musiClip;
	//
	private boolean isPlayAndPlayOffLineOption = true;
	//
	boolean canMoveToGame = false;

	//
	public StartMenuScene(GamePanel _gp) {
		super(_gp);
		gp.chatPanel.setVisible(false);
		gp.chatPanel.clearChatPane();
		gp.addMouseListener(this);
		gp.addMouseMotionListener(this);
		setUpSound();
		setUpImage();
		gp.playerClient = null;
	}

	@Override
	public void enter() {
		super.enter();
		playSound();
		gp.scrollpanel.setVisible(false);
		// System.out.println(gp.level1Scene);
	}

	@Override
	public void exit() {
		super.exit();
		musiClip.stop();
		gp.removeMouseListener(this);
		gp.removeMouseMotionListener(this);
	}

	@Override
	public void update() {
		super.update();
	}

	public void draw(Graphics2D g2) {
		// vẽ khung
		if (!canMoveToGame) {

			drawStartImageBackground(g2);
			drawStartMenuFrame(g2);

			if (isPlayAndPlayOffLineOption) {
				drawPlayOfflineButton(g2);
//				drawPlayOnlineButton(g2); //huy online
				drawQuitGameButton(g2);
				return;
			}
			if (isDrawLogInAndQuitGameButton)
				drawStartMenu(g2);
			if (isDrawInputID)
				drawInputMenu(g2, true);
			if (isDrawSignUpMenu)
				drawSignUpMenu(g2);

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		if (mouseX < quitX + quitWidth && mouseX > quitX && mouseY < quitY + quitHeight && mouseY > quitY &&isPlayAndPlayOffLineOption) {
			if (isDrawLogInAndQuitGameButton)
				System.exit(0);
		}
		if (mouseX < playOffX + playOffWidth && mouseX > playOffX && mouseY < playOffY + playOffHeight
				&& mouseY > playOffY && isPlayAndPlayOffLineOption) {
			gp.level1IsActive = true;
		}
		if (mouseX < playOnX + playOnWidth && mouseX > playOnX && mouseY < playOnY + playOnHeight && mouseY > playOnY
				&& isPlayAndPlayOffLineOption) {
			isPlayAndPlayOffLineOption = false;
		}
		if (isPlayAndPlayOffLineOption)
			return;
	}

	private void checkPressBackButton() {
		if (mouseX < backX + backWidth && mouseX > backX && mouseY < backY + backHeight && mouseY > backY) {
			isDrawInputID = false;
			isDrawSignUpMenu = false;
			isDrawLogInAndQuitGameButton = true;
			gp.passTextField.setVisible(false);
			gp.passTextField.setText(null);
			gp.inputIDTextField.setVisible(false);
			gp.inputIDTextField.setText(null);
		}
	}


	private void drawStartMenu(Graphics2D g2) {
		g2.setColor(Color.white);
		drawBackButton(g2, gp.screenHeight / 2 + gp.tileSize + gp.tileSize / 2);
		// drawQuitGameButton(g2);
	
	}

	private void drawInputMenu(Graphics2D g2, boolean isDrawSubmitButton) {
		Font font = new Font("Consolas", Font.BOLD, 14);
		g2.setFont(font);
		// ve chu id va pass
		g2.setColor(Color.white);
		g2.drawString("ID", (int) (logInX - gp.tileSize / 2.2), logInY + gp.tileSize / 3);
		g2.drawString("PASS", (int) (logInX - gp.tileSize / 1.5), logInY + gp.tileSize / 3 + gp.tileSize);
		if (isDrawSubmitButton)
		drawBackButton(g2,0);
		// kich hoat text fields
		gp.inputIDTextField.setBounds(logInX, logInY, logInWidth, logInHeight / 2);
		gp.passTextField.setBounds(logInX, logInY + gp.tileSize / 2 + gp.tileSize / 2, logInWidth, logInHeight / 2);
		if (!gp.inputIDTextField.isVisible()) {
			gp.inputIDTextField.setVisible(true);
			gp.inputIDTextField.setText(null);
		}
		if (!gp.passTextField.isVisible()) {
			gp.passTextField.setVisible(true);
			gp.passTextField.setText(null);
		}

	}

	private void drawSignUpMenu(Graphics2D g2) {
		drawInputMenu(g2, false);
	}

	private void drawStartMenuFrame(Graphics2D g2) {
		// ve khunng login
		Font font = new Font("Consolas", Font.BOLD, 20);
		g2.setFont(font);
		String text = "LOG IN";
		FontMetrics fontMetrics = g2.getFontMetrics();
		int x = (gp.screenWidth - fontMetrics.stringWidth(text)) / 2;
		int y = gp.screenHeight / 2 - gp.tileSize / 2;
		// areaOfButton;
		x = x - gp.tileSize;
		y = y - gp.tileSize / 2;
		int width = fontMetrics.stringWidth(text) + gp.tileSize * 2;
		int height = gp.tileSize;

		g2.setColor(new Color(0, 255, 250, 200));
		g2.fillRoundRect(x - gp.tileSize, y - gp.tileSize / 2, width + gp.tileSize * 2, (int) (gp.tileSize * 4.5), 30,
				30);
		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(x - gp.tileSize + 4, y - gp.tileSize / 2 + 4, width + gp.tileSize * 2 - 8,
				(int) (gp.tileSize * 4.5 - 8), 30, 30);
	}

	private void drawStartImageBackground(Graphics2D g2) {
		g2.drawImage(image, -gp.tileSize, 0, gp.screenWidth + gp.tileSize + 20, gp.screenHeight, null);
		// 255,255,255
	}

	private void drawPlayOfflineButton(Graphics2D g2) {
		Font font = new Font("Lucida Sans Unicode", Font.BOLD, 25);
		g2.setFont(font);
		String text2 = "PLAY NOW";
		String text = "LOG IN";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (int) ((gp.screenWidth - fontMetrics.stringWidth(text)) / 2 - gp.tileSize / 3);
		int y = gp.screenHeight / 2 - gp.tileSize / 2;
		// areaOfButton;
		playOffX = x - gp.tileSize;
		playOffY = y - gp.tileSize / 2;
		playOffWidth = fontMetrics.stringWidth(text2) + gp.tileSize * 2;
		playOffHeight = gp.tileSize;
		// check mouse in area
		boolean isInArea = false;
		if (mouseX < playOffX + playOffWidth && mouseX > playOffX && mouseY < playOffY + playOffHeight
				&& mouseY > playOffY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(playOffX, playOffY, playOffWidth, playOffHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(playOffX + 2, playOffY + 2, playOffWidth - 4, playOffHeight - gp.tileSize / 4 - 4, 20, 20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text2, x, y);
	}


	private void drawQuitGameButton(Graphics2D g2) {
		Font font = new Font("Lucida Sans Unicode", Font.BOLD, 20);
		g2.setFont(font);
		String text = "QUIT GAME";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (gp.screenWidth - fontMetrics.stringWidth(text)) / 2;
		int y = gp.screenHeight / 2 + gp.tileSize + gp.tileSize / 2;
		// areaOfButton;
		quitX = x - gp.tileSize;
		quitY = y - gp.tileSize / 2;
		quitWidth = fontMetrics.stringWidth(text) + gp.tileSize * 2;
		quitHeight = gp.tileSize;
		// check mouse in area
		boolean isInArea = false;
		if (mouseX < quitX + quitWidth && mouseX > quitX && mouseY < quitY + quitHeight && mouseY > quitY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(quitX, quitY, quitWidth, quitHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(quitX + 2, quitY + 2, quitWidth - 4, quitHeight - gp.tileSize / 4 - 4, 20, 20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text, x, y);
	}


	private void drawBackButton(Graphics2D g2, int _y) {
		Font font = new Font("Lucida Sans Unicode", Font.BOLD, 20);
		g2.setFont(font);
		String text = "BACK";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (gp.screenWidth - fontMetrics.stringWidth(text)) / 2;
		int y;
		if (_y == 0)
			y = gp.screenHeight / 2 + gp.tileSize + gp.tileSize + gp.tileSize / 4;
		else
			y = _y;
		// areaOfButton;
		backX = x - gp.tileSize;
		backY = y - gp.tileSize / 2;
		backWidth = fontMetrics.stringWidth(text) + gp.tileSize * 2;
		backHeight = gp.tileSize;
		// check mouse in area
		boolean isInArea = false;
		if (mouseX < backX + backWidth && mouseX > backX && mouseY < backY + backHeight && mouseY > backY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(backX, backY, backWidth, backHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(backX + 2, backY + 2, backWidth - 4, backHeight - gp.tileSize / 4 - 4, 20, 20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text, x, y);
	}

	private void setUpSound() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/music/startMenuMusic.wav"));
			musiClip = AudioSystem.getClip();
			musiClip.open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void playSound() {
		musiClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	private void setUpImage() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/startMenuImage/OIG3.png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
