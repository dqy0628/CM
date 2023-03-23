import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client {
    //此类管理登录用户和注册用户
    static JFrame login = new JFrame("登录界面");
    //登录按钮
    JButton loginButton = new JButton();
    //注册按钮
    JButton registerButton = new JButton();
    //用户名输入框
    JTextField username = new JTextField();
    //密码输入框
    JPasswordField password = new JPasswordField();
    //是否登录成功
    static boolean isLogin = false;


    public Client() throws IOException {
        //连接服务器
        //这里的IP要改成自己服务器的IP地址，记得设置安全组（一般不直接关闭防火墙）
        Socket socket = new Socket("127.0.0.1",20001);
        //将本地换回测试IP改成服务器IP
        //打开接收端
        new Thread(new ClientRunnable(socket)).start();
        //取消内部默认布局
        login.setLayout(null);
        //添加背景图片
        ImageIcon backGroundImg = new ImageIcon("src/img/background.png");
        JLabel label = new JLabel(backGroundImg);
        //设置宽高
        label.setSize(backGroundImg.getIconWidth(),backGroundImg.getIconHeight());
        login.setSize(backGroundImg.getIconWidth(),backGroundImg.getIconHeight());
        //设置显示窗口
        login.setVisible(true);
        //设置关闭方式
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置居中
        login.setLocationRelativeTo(null);
        login.getLayeredPane().add(label,Integer.valueOf(Integer.MIN_VALUE));
        JPanel panel = (JPanel) login.getContentPane();
        panel.setOpaque(false);
        init(socket);
    }

    public void init(Socket socket) throws IOException {
        //添加用户名字
        Font userNameFront = new Font(null,1,16);
        JLabel userNameText = new JLabel("用户名");
        userNameText.setForeground(Color.white);
        userNameText.setBounds(140,85,55,22);
        userNameText.setFont(userNameFront);
        login.getContentPane().add(userNameText);
        //添加用户名输入框
        username.setBounds(200,85,200,22);
        login.getContentPane().add(username);
        //添加密码文字
        Font passwordFront = new Font(null,1,16);
        JLabel passwordText = new JLabel("密码");
        passwordText.setForeground(Color.white);
        passwordText.setBounds(150,120,55,22);
        passwordText.setFont(passwordFront);
        login.getContentPane().add(passwordText);
        //添加密码框
        password.setBounds(200,120,200,22);
        login.getContentPane().add(password);
        //登录按钮
        ImageIcon loginImage = new ImageIcon("src/img/登录按钮.png");
        JLabel loginImageLabel = new JLabel(loginImage);
        loginImageLabel.setSize(loginImage.getIconWidth(),loginImageLabel.getHeight());
        loginImageLabel.setBounds(150,200,loginImage.getIconWidth(),loginImage.getIconHeight());
        login.getContentPane().add(loginImageLabel);
        //登录按钮的按钮组件
        JButton loginButton = new JButton();
        loginButton.setSize(loginImage.getIconWidth(),loginImageLabel.getHeight());
        loginButton.setBounds(150,200,loginImage.getIconWidth(),loginImage.getIconHeight());
        loginButton.setContentAreaFilled(false);
        loginButton.setBorder(null);
        loginButton.addActionListener((e)->{
            try {
                LoginUser(socket,username.getText(),new String(password.getPassword()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        login.getContentPane().add(loginButton);
        //注册按钮
        ImageIcon registerImage = new ImageIcon("src/img/注册按钮.png");
        JLabel registerImageLabel = new JLabel(registerImage);
        registerImageLabel.setSize(registerImage.getIconWidth(),registerImageLabel.getHeight());
        registerImageLabel.setBounds(300,200,registerImage.getIconWidth(),registerImage.getIconHeight());
        login.getContentPane().add(registerImageLabel);
        //注册按钮的按钮组件
        JButton registerButton = new JButton();
        registerButton.setSize(registerImage.getIconWidth(),registerButton.getHeight());
        registerButton.setBounds(300,200,registerImage.getIconWidth(),registerImage.getIconHeight());
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(null);
        registerButton.addActionListener((e)->{
            try {
                RegisterUser(socket,username.getText(),new String(password.getPassword()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        login.getContentPane().add(registerButton);
    }

    public static void LoginUser(Socket socket,String name,String pas) throws IOException {
        //获得字符输出流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("login");
        bw.newLine();
        bw.flush();

        //发送账号
        bw.write(name);
        bw.newLine();
        bw.flush();

        //发送密码
        bw.write(pas);
        bw.newLine();
        bw.flush();
    }

    public static void RegisterUser(Socket socket,String name,String pas) throws IOException {
        //获得字符输出流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("register");
        bw.newLine();
        bw.flush();

        //发送账号
        bw.write(name);
        bw.newLine();
        bw.flush();

        //发送密码
        bw.write(pas);
        bw.newLine();
        bw.flush();
    }

}
