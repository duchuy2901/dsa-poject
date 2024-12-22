package PlayerClient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ChatFunction.TabAction;
import CustomElements.RoundedBorder;

import PlayerState.Player;
import Scene.LobbyScene;
import Scene.MainGameOnlineScene;
import main.GamePanel;

public class PlayerClient extends Thread {
	private PrintWriter writer;
	private BufferedReader reader;
	private MainGameOnlineScene mainGameOnlineScene;
	private boolean canShoot = true;
	private boolean completeCreateOrJoinRoom = false;
	private GamePanel gp;
	private boolean isRunning = true;
	private boolean isHost = false;
	private boolean canMoveToGam;
	private TabAction tabAction;

	public PlayerClient(GamePanel _gp) {
		// mainGameOnlineScene = _mainGameOnlineScene;
		this.gp = _gp;
		tabAction = new TabAction(this.gp);
		InputMap inputMap = gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke tabKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
		inputMap.put(tabKeyStroke, "tabAction");
		ActionMap actionMap = gp.getActionMap();
		actionMap.put("tabAction", tabAction);
	

}}