import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class MainFrame extends JFrame implements ActionListener {
    private static JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    protected BufferedWriter bw;

    public MainFrame(BufferedWriter bw) {
        super("网络聊天室");
        this.bw = bw;
        //关闭方式
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        textArea = new JTextArea("-------------------------------------------欢迎来到网络聊天室-------------------------------------------");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        textField = new JTextField();
        textField.addActionListener(this);

        sendButton = new JButton("发送");
        sendButton.addActionListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }


    //按下发送按钮
    @Override
    public void actionPerformed(ActionEvent e) {
        String message = textField.getText();
        if (!message.isEmpty()) {
            try {
                bw.write(message);
                bw.newLine();
                bw.flush();

                textField.setText("");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //收到消息，反映在文本区里
    public static void appendText(String message){
        textArea.append(message);
    }
}
