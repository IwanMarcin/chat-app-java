package com.example.demo.client;

import com.example.demo.Message;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel;
    private ChatStompClient chatStompClient;
    private String username;
    private JScrollPane messageScrollPane;

    public ClientGUI(String username) throws ExecutionException, InterruptedException {
        super("User: " + username);
        this.username = username;
        chatStompClient = new ChatStompClient(this, username);

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Do you want to leave?", "Exit", JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION){
                    chatStompClient.disconnectUser(username);
                    ClientGUI.this.dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMessageSize();
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }

    private void addGuiComponents(){
        addConnectedUserComponents();
        addChatComponents();
    }

    private void addConnectedUserComponents(){
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10,10, 10, 10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);

        add(connectedUsersPanel, BorderLayout.WEST);
    }

    private void addChatComponents(){
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messageScrollPane = new JScrollPane(messagePanel);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        messageScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        messageScrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });

        chatPanel.add(messageScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){
                    String input = inputField.getText();

                    if(input.isEmpty()) return;
                    inputField.setText("");

                    chatStompClient.sendMessage(new Message(input, username));
                }
            }
        });
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setBorder(Utilities.addPadding(0, 10, 0, 10));
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);

    }

    private JPanel createChatMessageComponent(Message message){
        boolean isMine = message.getUser().equals(username);
        String time = new SimpleDateFormat("HH:mm").format(new Date());

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setBackground(Utilities.TRANSPARENT_COLOR);

        wrapper.setBorder(Utilities.addPadding(5, 2, 5, 2));

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(Utilities.addPadding(10, 15, 10, 15));
        bubble.setBackground(isMine ? Utilities.MY_MESSAGE_COLOR : Utilities.OTHER_MESSAGE_COLOR);
        bubble.setOpaque(true);

        int maxWidth = (int)(getWidth() * 0.6);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        header.setBackground(Utilities.TRANSPARENT_COLOR);

        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        timeLabel.setForeground(Color.WHITE);

        header.add(usernameLabel);
        header.add(timeLabel);
        bubble.add(header);

        JTextArea textArea = new JTextArea(message.getMessage());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setForeground(Utilities.TEXT_COLOR);
        textArea.setBackground(isMine ? Utilities.MY_MESSAGE_COLOR : Utilities.OTHER_MESSAGE_COLOR);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBorder(null);

        textArea.setSize(maxWidth, Short.MAX_VALUE);
        textArea.setPreferredSize(new Dimension(maxWidth, textArea.getPreferredSize().height));

        bubble.add(textArea);

        bubble.setMaximumSize(new Dimension(maxWidth, bubble.getPreferredSize().height));
        bubble.setAlignmentX(isMine ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        wrapper.removeAll();
        if(isMine){
            wrapper.add(Box.createHorizontalGlue());
            wrapper.add(bubble);
        } else {
            wrapper.add(bubble);
            wrapper.add(Box.createHorizontalGlue());
        }

        return wrapper;
    }

    @Override
    public void onMessageRecieve(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();

        messageScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsersUpdate(ArrayList<String> activeUsers) {
        if(connectedUsersPanel.getComponents().length >= 2){
            connectedUsersPanel.remove(1);
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        for(String user : activeUsers){
            JLabel username = new JLabel();
            username.setText(user);
            username.setForeground(Utilities.TEXT_COLOR);
            username.setFont(new Font("Monospaced", Font.BOLD, 16));
            userListPanel.add(username);
        }

        connectedUsersPanel.add(userListPanel);
        revalidate();
        repaint();
    }

    private void updateMessageSize(){
        for(int i = 0; i < messagePanel.getComponents().length; i++){
            Component component = messagePanel.getComponent(i);
            if(component instanceof JPanel chatMessage){
                if(chatMessage.getComponent(1) instanceof JLabel messageLabel){
                    messageLabel.setText("<html>" +
                            "<body style='width:" + (0.60 * getWidth()) + "'px>" +
                            messageLabel.getText() +
                            "</body>"+
                            "</html>");
                }
            }
        }
    }
}