package Scene;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import EnemyState.Enemy;
import PlayerClient.PlayerClient;
import PlayerState.Player;
import SkeletonEnemy.SkeletonEnemy;
import main.GamePanel;
import main.KeyHandler;
import tile.TileManager;

public class LobbyScene extends Scene {

	public KeyHandler keyH;
	private BufferedImage pauseImage;
	private BufferedImage lobbyImage;
	// ske enemy
	// pause button
	private int pauseX;
	private int pauseY;
	private int pauseWidth;
	private int pauseHeight;
	// pause menu
	private boolean isDrawPauseMenu;
	// resume button
	private int resumeX;
	private int resumeY;
	private int resumeWidth;
	private int resumeHeight;
	// main menu button
	private int mainMenuX;
	private int mainMenuY;
	private int mainMenuWidth;
	private int mainMenuHeight;
	// win
	private boolean isRoomCreated;
	private int createRoomX;
	private int createRoomY;
	private int createRoomWidth;
	private int createRoomHeight;
	// win
	private boolean isJoinRoom;
	private int joinRoomX;
	private int joinRoomY;
	private int joinRoomWidth;
	private int joinRoomHeight;
	// color
	private Color color;
	private float red;
	private float green;
	private float blue;
	public GamePanel gp;
	public boolean isWaitingPlayer;
	// dot panel waiting player
	private int dotCount = 1;
	private int dotFrameFPS = 0;

	public LobbyScene(GamePanel _gp, KeyHandler _keyH) {
		super(_gp);
		gp = _gp;
		this.keyH = _keyH;
		getImage();
		gp.addMouseListener(this);
		gp.addMouseMotionListener(this);
		addInputField();
	}

	private void addInputField() {
		gp.createRoomInput.setVisible(true);
		gp.panelListRoom.removeAll();
		gp.panelListRoom.repaint();
		gp.panelListRoom.validate();
		gp.scrollpanel.revalidate();
		gp.panelListRoom.setVisible(true);
		gp.scrollpanel.setVisible(true);
		Border roundedBorder = BorderFactory.createLineBorder(Color.BLACK, 2, true); // true: rounded corners
		gp.createRoomInput.setBorder(roundedBorder);
	}

	@Override
	public void enter() {
		super.enter();
		gp.isJoinRoom = false;
		gp.isCreateRoom = false;
		// setUpEnemy();
	}

	@Override
	public void exit() {
		super.exit();
		gp.createRoomInput.setVisible(false);
		gp.removeMouseListener(this);
		gp.removeMouseMotionListener(this);
	}

	@Override
	public void update() {
		super.update();
		if (!isDrawPauseMenu) {

		}

	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		// resume, win la button canh le
		// drawSkeletonEnemy(g2);
		// drawPlayer(g2);
		if (!isWaitingPlayer)
			g2.drawImage(lobbyImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
		g2.setColor(new Color(0, 255, 255, 100));
		g2.fillRoundRect((int) (0.15 * gp.maxScreenCol) * gp.tileSize, (int) (0.15 * gp.maxScreenRow) * gp.tileSize,
				(int) (0.78 * gp.maxScreenCol) * gp.tileSize, (int) (0.75 * gp.maxScreenRow) * gp.tileSize, 30, 30);
		if (isWaitingPlayer)
			DrawWaitingPlayerPanel(g2);
		if (!isDrawPauseMenu) {
			drawPauseButton(g2);
			if (!isWaitingPlayer) {
				gp.createRoomInput.setVisible(true);
				drawCreateRoomButton(g2);
			//	drawJoinRoomButton(g2);
			} else {
				gp.createRoomInput.setVisible(false);
				gp.createRoomInput.setText("");
			}

		} else {
			gp.scrollpanel.setVisible(false);
			gp.createRoomInput.setVisible(false);
			gp.createRoomInput.setText("");
		}
		checkForPressPause(g2);
		// checkForWin(g2);
	}

	public void DrawWaitingPlayerPanel(Graphics2D g2) {
		gp.setBackground(color.black);
		Font font = new Font("Consolas", Font.BOLD, 18);
		g2.setFont(font);

		// dotCount=1;
		if (dotFrameFPS < 15) {
			dotFrameFPS++;
		} else {
			dotFrameFPS = 0;
			if (dotCount < 3) {
				dotCount++;
			} else {
				dotCount = 1;
			}
		}
		String dotString = "";
		String whiteString = "";
		for (int i = 0; i < dotCount; i++) {
			dotString += ".";
			whiteString += " ";
		}
		String text = whiteString + "Waiting for player" + dotString;

		FontMetrics fontMetrics = g2.getFontMetrics();
		g2.setColor(new Color(255, 255, 0));
		int x = (gp.screenWidth - fontMetrics.stringWidth(text)) / 2;
		int y = gp.screenHeight / 2 - gp.tileSize / 2;
		g2.drawString(text, x, y);
	}

	private void checkForPressPause(Graphics2D g2) {
		if (isDrawPauseMenu) {
			drawPauseFrame(g2);
			drawResumeButton(g2);
			drawMainMenuButton(g2);
		}
	}

	private void drawJoinRoomButton(Graphics2D g2) {
		Font font = new Font("Consolas", Font.BOLD, 18);
		g2.setFont(font);
		String text = "JOIN ROOM";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (int) (gp.tileSize * 4.28 - fontMetrics.stringWidth(text));
		int y = (int) (gp.screenHeight / 2);
		// areaOfButton;
		joinRoomX = x - gp.tileSize / 2;
		joinRoomY = y - gp.tileSize / 2;
		joinRoomWidth = fontMetrics.stringWidth(text) + gp.tileSize;
		joinRoomHeight = gp.tileSize;
		// check mouse in area
		boolean isInArea = false;
		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(joinRoomX + 2, joinRoomY + 2, joinRoomWidth - 4, joinRoomHeight - gp.tileSize / 4 - 4, 20, 20);
		if (mouseX < joinRoomX + joinRoomWidth && mouseX > joinRoomX && mouseY < joinRoomY + joinRoomHeight
				&& mouseY > joinRoomY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(joinRoomX, joinRoomY, joinRoomWidth, joinRoomHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(joinRoomX + 2, joinRoomY + 2, joinRoomWidth - 4, joinRoomHeight - gp.tileSize / 4 - 4, 20,
					20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text, x, y);
	}

	private void drawCreateRoomButton(Graphics2D g2) {
		Font font = new Font("Consolas", Font.BOLD, 18);
		g2.setFont(font);
		String text = "CREATE ROOM";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (int) ((0.35 * gp.maxScreenCol) * gp.tileSize - fontMetrics.stringWidth(text));
		int y = (int) ((0.4 * gp.maxScreenRow) * gp.tileSize);
		// areaOfButton;

		createRoomX = x - gp.tileSize / 2;
		createRoomY = y - gp.tileSize / 2;
		createRoomWidth = fontMetrics.stringWidth(text) + (int) (0.05 * gp.maxScreenCol) * gp.tileSize;
		createRoomHeight = (int) ((0.068 * gp.maxScreenRow) * gp.tileSize);
		// check mouse in area
		boolean isInArea = false;
		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(createRoomX + 2, createRoomY + 2, createRoomWidth - 4, createRoomHeight - gp.tileSize / 4 - 4,
				20, 20);
		if (mouseX < createRoomX + createRoomWidth && mouseX > createRoomX && mouseY < createRoomY + createRoomHeight
				&& mouseY > createRoomY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(createRoomX, createRoomY, createRoomWidth, createRoomHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(createRoomX + 2, createRoomY + 2, createRoomWidth - 4,
					createRoomHeight - gp.tileSize / 4 - 4, 20, 20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text, x, y);
	}

	private void drawPauseButton(Graphics2D g2) {
		pauseX = gp.screenWidth - gp.tileSize * 2;
		pauseY = gp.tileSize / 4;
		pauseWidth = gp.tileSize * 2;
		pauseHeight = gp.tileSize * 2;
		// check mouse in area
		boolean isInArea = false;
		g2.drawImage(pauseImage, pauseX, pauseY, pauseWidth, pauseHeight - gp.tileSize / 4, null);
		if (mouseX < pauseX + pauseWidth && mouseX > pauseX && mouseY < pauseY + pauseHeight && mouseY > pauseY) {
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRoundRect(pauseX + gp.tileSize / 4, pauseY + gp.tileSize / 4, pauseWidth - gp.tileSize / 2,
					(int) (pauseHeight - gp.tileSize / 1.5), 100, 100);
			isInArea = true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if (mouseX < createRoomX + createRoomWidth && mouseX > createRoomX && mouseY < createRoomY + createRoomHeight
				&& mouseY > createRoomY && !isDrawPauseMenu && !isWaitingPlayer) {
			String roomName2 = gp.createRoomInput.getText();
			if(!roomName2.isBlank()) {
				gp.isJoinRoom = false;
				gp.isCreateRoom = true;				
			}

			// gp.mainGameOnlineSceneActive = true;
		}
//		if (mouseX < joinRoomX + joinRoomWidth && mouseX > joinRoomX && mouseY < joinRoomY + joinRoomHeight
//				&& mouseY > joinRoomY && !isDrawPauseMenu && !isWaitingPlayer) {
//			gp.isCreateRoom = false;
//			gp.isJoinRoom = true;
//			// gp.mainGameOnlineSceneActive = true;
//		}
		if (mouseX < pauseX + pauseWidth && mouseX > pauseX && mouseY < pauseY + pauseHeight && mouseY > pauseY
				&& !isDrawPauseMenu) {
			isDrawPauseMenu = true;
		}
		// click resume
		if (mouseX < resumeX + resumeWidth && mouseX > resumeX && mouseY < resumeY + resumeHeight && mouseY > resumeY
				&& isDrawPauseMenu) {
			isDrawPauseMenu = false;
			if(!isWaitingPlayer)
				gp.scrollpanel.setVisible(true);
		}
	}

	private void drawPauseFrame(Graphics2D g2) {
		// ve khunng login
		g2.setColor(new Color(0, 255, 250, 200));
		g2.fillRoundRect(resumeX - gp.tileSize, resumeY - gp.tileSize / 2, resumeWidth + gp.tileSize * 2,
				(int) (gp.tileSize * 4.5), 30, 30);
		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(resumeX - gp.tileSize + 4, resumeY - gp.tileSize / 2 + 4, resumeWidth + gp.tileSize * 2 - 8,
				(int) (gp.tileSize * 4.5 - 8), 30, 30);
		// drawNeonLights(g2);
	}

	private void drawResumeButton(Graphics2D g2) {
		Font font = new Font("Consolas", Font.BOLD, 20);
		g2.setFont(font);
		String text = "RESUME";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (gp.screenWidth - fontMetrics.stringWidth(text)) / 2;
		int y = gp.screenHeight / 2 - gp.tileSize / 2;
		// areaOfButton;
		resumeX = x - gp.tileSize;
		resumeY = y - gp.tileSize / 2;
		resumeWidth = fontMetrics.stringWidth(text) + gp.tileSize * 2;
		resumeHeight = gp.tileSize;
		// check mouse in area
		boolean isInArea = false;
		if (mouseX < resumeX + resumeWidth && mouseX > resumeX && mouseY < resumeY + resumeHeight && mouseY > resumeY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(resumeX, resumeY, resumeWidth, resumeHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(resumeX + 2, resumeY + 2, resumeWidth - 4, resumeHeight - gp.tileSize / 4 - 4, 20, 20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text, x, y);
	}

	private Color color(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		return null;
	}

	private void drawMainMenuButton(Graphics2D g2) {
		Font font = new Font("Consolas", Font.BOLD, 20);
		g2.setFont(font);
		String text = "MAIN MENU";
		FontMetrics fontMetrics = g2.getFontMetrics();
		// possition of text;
		int x = (gp.screenWidth - fontMetrics.stringWidth(text)) / 2;
		int y = gp.screenHeight / 2 + gp.tileSize / 2;
		// areaOfButton;
		mainMenuX = x - gp.tileSize;
		mainMenuY = y - gp.tileSize / 2;
		mainMenuWidth = fontMetrics.stringWidth(text) + gp.tileSize * 2;
		mainMenuHeight = gp.tileSize;
		// check mouse in area
		boolean isInArea = false;
		if (mouseX < mainMenuX + mainMenuWidth && mouseX > mainMenuX && mouseY < mainMenuY + mainMenuHeight
				&& mouseY > mainMenuY) {
			g2.setColor(new Color(255, 255, 0));
			g2.fillRoundRect(mainMenuX, mainMenuY, mainMenuWidth, mainMenuHeight - gp.tileSize / 4, 20, 20);
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRoundRect(mainMenuX + 2, mainMenuY + 2, mainMenuWidth - 4, mainMenuHeight - gp.tileSize / 4 - 4, 20,
					20);
			isInArea = true;
		}
		if (!isInArea)
			g2.setColor(Color.white);
		else
			g2.setColor(new Color(255, 255, 0));
		g2.drawString(text, x, y);
	}

	private void getImage() {
		try {
			pauseImage = ImageIO.read(getClass().getResourceAsStream("/buttonImage/OIG.png"));
			lobbyImage = ImageIO.read(getClass().getResourceAsStream("/startMenuImage/lobby.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
