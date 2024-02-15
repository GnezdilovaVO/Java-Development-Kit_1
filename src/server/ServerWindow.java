package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ServerWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_POSX = 400;
    private static final int WINDOW_POSY = 300;
    private final String LOG_NAME_FILE = "log.txt";
    private JButton btnStart, btnExit;
    private boolean isServerWork = false;
    private JTextArea chat;
    public ServerWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocation(WINDOW_POSX, WINDOW_POSY);
//        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setBounds(WINDOW_POSX, WINDOW_POSY, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("I`m a server");
        setResizable(false);
        add(getBotPanel(), BorderLayout.SOUTH);
        add(getJTextArea());
        setVisible(true);
    }
    private JPanel getBotPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("Старт");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isServerWork) {
                    loadLog();
                    appToChat("I'm working");
                    isServerWork = true;
                }
            }
        });
        btnExit = new JButton("Стоп");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWork) {
                    appToChat("I'm not working");
                    saveLog();
                    isServerWork = false;
                }

            }
        });
        bottomPanel.add(btnStart);
        bottomPanel.add(btnExit);
        return bottomPanel;
    }
    private void saveLog() {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_NAME_FILE));
            bw.write(chat.getText());
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private void loadLog() {
        try{
            FileReader fr = new FileReader(LOG_NAME_FILE);
            BufferedReader br = new BufferedReader(fr);
            br.lines()
                    .map(e -> (e + "\n"))
                    .forEach(chat::append);
            chat.append("\n");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    public void appToChat(String message){
        chat.append(message + "\n");
    }
    private JScrollPane getJTextArea() {
        chat = new JTextArea();
        chat.setEditable(false);
        return new JScrollPane(chat);
    }
    public JTextArea getChat(){
        return chat;
    }

    public boolean isServerWork() {
        return isServerWork;
    }
}
