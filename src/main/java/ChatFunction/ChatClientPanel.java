package ChatFunction;

import javax.swing.*;
import javax.swing.text.*;

import CustomElements.CustomScrollPane;
import net.bytebuddy.asm.Advice.This;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public  class ChatClientPanel extends JPanel implements Runnable {

    private JTextField chatInputField;
    private JTextPane chatPane;
    private StyledDocument chatDoc;
    private JButton sendButton;
    public Socket socket;
    private Thread gameThread;
    private boolean running=true;
    private boolean isActive=true;
    private BufferedReader reader;
    private PrintWriter writer;
    public ChatClientPanel() {
        setupUI();
        // setupNetworking();
        
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Chat area
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFocusTraversalKeysEnabled(false);
        chatPane.setFocusable(false);
        chatPane.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for chatPane
        chatDoc = chatPane.getStyledDocument();
        JScrollPane scrollPane = new CustomScrollPane(chatPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Input field and send button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Color.WHITE); // Set background color for inputPanel
        inputPanel.setFocusTraversalKeysEnabled(false);
        chatInputField = new JTextField(20);
        chatInputField.setFocusTraversalKeysEnabled(false);
        chatInputField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for chatInputField
        inputPanel.add(chatInputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFocusTraversalKeysEnabled(false);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set font for sendButton
        sendButton.setBackground(new Color(60, 179, 113)); // Set background color
        sendButton.setForeground(Color.WHITE); // Set text color
        sendButton.setFocusPainted(false); // Remove focus border
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (chatInputField.getText().length()>36) {
            		JOptionPane.showMessageDialog(null, "Exceeds 37 characters", "Custom Dialog", JOptionPane.INFORMATION_MESSAGE);
				}else {
					if(chatInputField.getText().length()>0)
						sendMessage();
				}
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);
        // enter button
        chatInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	if (chatInputField.getText().length()>36) {
                		JOptionPane.showMessageDialog(null, "Exceeds 37 characters", "Custom Dialog", JOptionPane.INFORMATION_MESSAGE);
    				}else {
    					if(chatInputField.getText().length()>0)
    						sendMessage();
    				}
                }
            }
        });
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void setupNetworking() {
        try {
            socket = new Socket("server_address", 12345); // Replace with your server address and port
            OutputStream outputStream = socket.getOutputStream();
            // out = new PrintWriter(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace(); // Handle connection error
        }
    }

    private void sendMessage() {
        
        try {
            String message = chatInputField.getText();
            writer.println(message);
            chatInputField.setText("");
            appendToChatPane("Me: ", new Color(60, 179, 113), true); // Append "Me:" in bold and sea green color
            appendToChatPane(message + "\n", Color.BLACK, false); // Append the message in normal text color
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void appendToChatPane(String text, Color color, boolean bold) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributes = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        if (bold) {
            attributes = styleContext.addAttribute(attributes, StyleConstants.Bold, true);
        }
        int length = chatDoc.getLength();
        try {
            chatDoc.insertString(length, text, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String message;
        try {        
            while (running && (message = reader.readLine()) != null) {
                String[] parts = message.split(" : ", 2); // Tách name và tin nhắn
                if (parts.length == 2) {
                    String name = parts[0];
                    String msg = parts[1];
                    appendToChatPane(name+" : ",Color.BLUE, true); // Append "Me:" in bold and sea green color
                    appendToChatPane(msg + "\n", Color.BLACK, false); // Append the message in normal text color
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StartThread() {
    	try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
	        running=true;
	        gameThread = new Thread(this);
	        gameThread.start();
	        System.out.println("startttt");
		} catch (IOException e) {
			System.out.println("không thể tạo chat online  161 :startThread");
		}

    }

    public void StopThread() {
        running=false;
    }

    public void Active() {
    	running=true;
        isActive=false;
        setVisible(true);
    }

    public  void Disable() {
    	running=false;
        isActive=false;
        this.setVisible(false);
    }
    public void clearChatPane() {
        try {
            chatDoc.remove(0, chatDoc.getLength());
            writer=null;
            reader=null;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
