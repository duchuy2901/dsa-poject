package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ChatFunction.ChatClientPanel;
import CustomElements.CustomScrollPane;
import CustomElements.RoundJTextField;
import CustomElements.RoundedBorder;

public class Main {
	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Tran Duc Huy");

		JLayeredPane layeredPane = new JLayeredPane();
		// jtextfield
		Font font = new Font("Consolas", Font.BOLD, 20);
		JTextField passTextField = new JTextField();
		passTextField.setFont(font);
		passTextField.setBounds(50, 90, 150, 30);
		passTextField.setVisible(false);
		layeredPane.add(passTextField, JLayeredPane.PALETTE_LAYER);

		JTextField inputIDTextField = new JTextField();
		inputIDTextField.setFont(font);
		inputIDTextField.setVisible(false);
		layeredPane.add(inputIDTextField, JLayeredPane.PALETTE_LAYER);

		JTextField createRoomInput = new RoundJTextField(15, "room id");
		createRoomInput.setFont(font);
		createRoomInput.setHorizontalAlignment(SwingConstants.CENTER);
		createRoomInput.setVisible(false);
		createRoomInput.setFocusTraversalKeysEnabled(false);
		layeredPane.add(createRoomInput, JLayeredPane.PALETTE_LAYER);
		// panel list room
		JPanel panelListRoom = new JPanel();
		JScrollPane scrollPane = new CustomScrollPane(panelListRoom);
		scrollPane.setFocusTraversalKeysEnabled(false);
		// chat funtion
		 // Create an instance of ChatClientPanel
        ChatClientPanel chatPanel = new ChatClientPanel();
        chatPanel.setFocusTraversalKeysEnabled(false);
        layeredPane.add(chatPanel, JLayeredPane.PALETTE_LAYER);
        chatPanel.Disable();
        // Adjust the position of chat panel as needed
        chatPanel.setBounds(20, 400, 350, 300); // Example position and size
		// game panel
		GamePanel gamePanel = new GamePanel(inputIDTextField, passTextField, createRoomInput, panelListRoom,
				scrollPane,chatPanel);
		createRoomInput.setBounds((int) (0.2 * gamePanel.maxScreenCol) * gamePanel.tileSize,
				(int) (0.3 * gamePanel.maxScreenRow) * gamePanel.tileSize,
				(int) (0.22 * gamePanel.maxScreenCol) * gamePanel.tileSize, 30);
		gamePanel.setBounds(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
		layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
		// add
		panelListRoom.setLayout(new BoxLayout(panelListRoom, BoxLayout.Y_AXIS)); // Sử dụng BoxLayout với hướng dọc
		panelListRoom.setBackground(new Color(240, 240, 240, 100));

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// System.out.println(*gamePanel.maxScreenCol);
		scrollPane.setBounds((int) (0.5 * gamePanel.maxScreenCol) * gamePanel.tileSize,
				(int) (0.2 * gamePanel.maxScreenRow) * gamePanel.tileSize,
				(int) (0.35 * gamePanel.maxScreenCol) * gamePanel.tileSize,
				(int) (0.6 * gamePanel.maxScreenRow) * gamePanel.tileSize); // Điều chỉnh kích thước và vị trí của
		scrollPane.setVisible(false); // JScrollPane
		layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);
		window.add(layeredPane);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		gamePanel.startGameThread();
	}
}
