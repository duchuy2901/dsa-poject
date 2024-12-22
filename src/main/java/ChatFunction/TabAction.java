package ChatFunction;

import javax.swing.*;

import PlayerClient.PlayerClient;
import main.GamePanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class TabAction extends AbstractAction {
	private GamePanel gp;
	public TabAction(GamePanel gamePanel) {
		this.gp=gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gp.checkOpenChat();
		// Implement your action when Tab key is pressed
		System.out.println("Tab key pressed!");
		// Add your specific action handling here
	}
}
