import java.io.*;
import java.net.Socket;

public class ClientRunnable implements Runnable{
    Socket socket;
    public ClientRunnable(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        while (true){//循环接收消息
            //接收服务器的消息
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String s = br.readLine();//服务器发来的消息,按行隔开
                System.out.println(s);
                //接收到的消息的处理
                if(!Client.isLogin){//还没有完成登录
                    if(s.equals("true")){
                        //登录成功
                        Client.isLogin = true;
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        //关闭登录窗口
                        Client.login.dispose();
                        //打开聊天界面
                        new MainFrame(bw);
                    }
                }else {//已完成登录
                    //写入到文本区里
                    MainFrame.appendText("\n"+s);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
