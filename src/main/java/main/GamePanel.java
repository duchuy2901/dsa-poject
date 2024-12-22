package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;

import com.mysql.cj.xdevapi.Statement;

import ChatFunction.ChatClientPanel;
import ChatFunction.TabAction;
import PlayerClient.PlayerClient;
import PlayerState.Player;
import Scene.LobbyScene;
import Scene.MainGameOnlineScene;
import Scene.Scene;
import Scene.SceneManager;
import Scene.StartMenuScene;
import Scene.level1Scene;
import SkeletonEnemy.SkeletonEnemy;
import collisionDetection.CreateCollision;
import net.bytebuddy.asm.Advice.This;
//import physic2d.Gravity;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	// screen setting
	final int scale = 3;
	final int originalTileSize = 16;
	public final int tileSize = originalTileSize * scale;
	public int maxScreenCol = 25;
	public int maxScreenRow = 15;
	public int screenWidth = maxScreenCol * tileSize;
	public int screenHeight = maxScreenRow * tileSize;
	// game frame settings
	final int FPS = 60;
	public double drawInterval = 1000000000 / FPS;
	int x = 10;
	public float deltaTime;
	Thread gameThread;
	// world setting;
	public final int maxWorldCol = 88;
	public final int maxWorldRow = 54;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	// component
	public KeyHandler keyH = new KeyHandler(this);
	// scene
	private SceneManager sceneManager;
	private StartMenuScene startMenuScene;
	public  LobbyScene lobbyScene;
	public MainGameOnlineScene mainGameOnlineScene;
	public boolean startMenuIsActive = false;
	public level1Scene level1Scene;
	public Scene currentSceneLevel;
	public boolean level1IsActive = false;
	public boolean lobbySceneActive = false;
	public boolean mainGameOnlineSceneActive = false;
	// sql
	public java.sql.Statement stmt;
	public JTextField inputIDTextField, passTextField,createRoomInput;
	public JPanel panelListRoom;
	//online
	public boolean isCreateRoom=false;
	public boolean isJoinRoom=false;
	public PlayerClient playerClient;
	public JScrollPane scrollpanel;
	public ChatClientPanel chatPanel;
	
	public GamePanel(JTextField _inputIDTextField, JTextField _passTextField, 
			JTextField _createRoomInput, JPanel _panelListRoom,JScrollPane _scJScrollPane,ChatClientPanel _chatPanel) {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);
		this.setBackground(new Color(255,255,255));
		this.addKeyListener(keyH);
		this.setFocusable(true);
		this.inputIDTextField = _inputIDTextField;
		this.passTextField = _passTextField;
		this.createRoomInput=_createRoomInput;
		this.panelListRoom=_panelListRoom;
		this.scrollpanel=_scJScrollPane;
		this.chatPanel=_chatPanel;
		this.setFocusTraversalKeysEnabled(false);
		addChatFunction();
		setUpScene();


	}
	private  void addChatFunction() {
		  // Tạo chatPanel
     
	}
	
	private void setUpScene() {
		sceneManager = new SceneManager();
		startMenuScene = new StartMenuScene(this);
		sceneManager.initialize(startMenuScene);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void run() {
		double delta = 0;
		long currentTime;
		float  frame=0.0166667f;
		long lastTime = System.nanoTime();
		while (gameThread != null) {
			// System.out.println(2);
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			deltaTime = (float) ((currentTime - lastTime) / drawInterval);
			lastTime = currentTime;
			//System.out.println(frame);
			if (delta >=frame) {
				update();
				repaint();
				delta--;
			}
		}
	}

	public void update() {
		keyH.update();
		SceneActive();
		sceneManager.currentScene.update();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		sceneManager.currentScene.draw(g2);
		g2.dispose();
	}

	// gan scene duoc ve
	public void SceneActive() {
	
		if (level1IsActive) {
			playerClient=null;
			lobbyScene=null;
			startMenuScene = null;
			mainGameOnlineScene=null;
			level1Scene = new level1Scene(this, keyH);
			sceneManager.changeScene(level1Scene);
			level1IsActive = false;
			//PlayerClient playerClient= new PlayerClient(mainGameOnlineScene);
		}
		if (lobbySceneActive) {
			System.out.println(1);
			lobbyScene=null;
			startMenuScene = null;
			mainGameOnlineScene=null;
			playerClient=null;
			lobbyScene = new LobbyScene(this, keyH);
			sceneManager.changeScene(lobbyScene);
			lobbySceneActive = false;
			playerClient= new PlayerClient(this);
		}
		if (mainGameOnlineSceneActive) {


		}
//		if (level1IsActive) {
//			startMenuScene = null;
		
//			level1Scene = new level1Scene(this, keyH);
//		currentSceneLevel=level1Scene;
//			sceneManager.changeScene(level1Scene);
//			level1IsActive = false;
//		}
		if (startMenuIsActive) {
			playerClient=null;
			startMenuScene=null;
			level1Scene = null;
			mainGameOnlineScene=null;
			lobbyScene=null;
			startMenuScene = new StartMenuScene(this);
			sceneManager.changeScene(startMenuScene);
			startMenuIsActive = false;
		}
	}
	public void changeToOnlineGameScene() {
		mainGameOnlineScene=null;
		lobbyScene=null;
		startMenuScene = null;
		mainGameOnlineScene = new MainGameOnlineScene(this, keyH);
		sceneManager.changeScene(mainGameOnlineScene);
		mainGameOnlineSceneActive = false;
	}
	public void checkOpenChat() {
		if(sceneManager.currentScene!=lobbyScene||chatPanel==null||playerClient==null) return;
		try {
			if (chatPanel.isVisible()) {
				chatPanel.Disable();
			} else {
				chatPanel.Active();
			}
		} catch (Exception e) {
			System.out.println("khong the bat tat chat");
		}
	}
}
