package client;

import server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientGUI extends JFrame {
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 400;
    private JTextArea chat = new JTextArea();
    private JTextField tfIP, tfPort, tfName, tfPassword, tfMessage;
    private JButton btnLogin, btnSend;
    private boolean connect = false;
    ServerWindow server;
    public ClientGUI(ServerWindow server){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(server);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("I'm a employ");
        this.server = server;
        add(getNorthPanel(), BorderLayout.NORTH);
        add(getSouthPanel(), BorderLayout.SOUTH);
        add(getJTextArea());



        setVisible(true);
    }
    private JPanel getNorthPanel(){
        JPanel NorthPanel = new JPanel(new GridLayout(2,3));
        tfIP = new JTextField("122.0.0.1");
        tfPort = new JTextField("8081");
        tfName = new JTextField("Name");
        tfPassword = new JTextField("11111");
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isServerWork()) {
                    chat.setText("");
                    chat.append("Connect!\n");
                    chat.append(getMesFromChat(server.getChat()));
                    server.appToChat(tfName.getText() + " in chat");
                    connect = true;
                } else {
                    chat.append("No connect!\n");
                    connect = false;
                }
            }
        });
        NorthPanel.add(tfIP);
        NorthPanel.add(tfPort);
        NorthPanel.add(new JLabel(" "));
        NorthPanel.add(tfName);
        NorthPanel.add(tfPassword);
        NorthPanel.add(btnLogin);
        return NorthPanel;
    }
    private JPanel getSouthPanel(){
        JPanel southPanel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        btnSend = new JButton("Send");
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMes();
                }
            }
        });
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMes();
            }
        });
        southPanel.add(btnSend, BorderLayout.EAST);
        southPanel.add(tfMessage);
        return southPanel;
    }
    private JScrollPane getJTextArea() {
        chat.setEditable(false);
        return new JScrollPane(chat);
    }
    private boolean isServerWork(){
        return server.isServerWork();
    }
    public String getMesFromChat(JTextArea archive) {
        StringBuilder mes = new StringBuilder();
        String[] messages = archive.getText().split("\n");
        for (int i = 0; i < messages.length; i++) {
            if(messages[i].contains(":")) {
                mes.append(messages[i]).append("\n");
            }
        }
        return mes.toString();
    }
    private boolean newMesOnServer() {
        int idServer = getMesFromChat(server.getChat()).split("\n").length;
        int idClient = getMesFromChat(chat).split("\n").length;
        if(idClient == 1 && getMesFromChat(chat).equals(" ")) {
            idClient = 0;
        }
        if(idServer == 1 &&  getMesFromChat(server.getChat()).equals(" ")) {
            idServer = 0;
        }
        if(idServer > idClient) {
            return true;
        }
        return false;
    }
    private void upClientMes() {
        if(newMesOnServer()) {
            String[] mesServer = getMesFromChat(server.getChat()).split("\n");
            String[] mesClient = getMesFromChat(chat).split("\n");
            int idClient, idServer;
            if(mesClient[0].equals(" ")){
                idClient = 0;
            } else {
                idClient = mesClient.length;
            }
            if (mesServer[0].equals(" ")) {
                idServer = 0;
            } else {
                idServer = mesServer.length;
            }
            for (int i = idClient; i < idServer ; i++) {
                chat.append(mesServer[i] + "\n");

            }
        }
    }
    private void sendMes() {
        if(connect && isServerWork()) {
            if (!tfMessage.getText().isEmpty()) {
                server.appToChat(tfName.getText() + ":" + tfMessage.getText());
                upClientMes();
                tfMessage.setText("");
            } else {
                chat.append("Enter mes! \n");
            }
        } else {
            chat.append("No connect!\n");
        }
    }
}
