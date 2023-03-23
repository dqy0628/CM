import java.io.*;
import java.net.Socket;

public class ServerRunnable implements Runnable{
    //自身的TCP连接
    Socket socket;

    public ServerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //获取字符输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true){
                String choose = br.readLine();
                switch (choose){
                    case "login"->Login(br);
                    case "register"->Register(br);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //用户点击的是登录按钮
    public void Login(BufferedReader br) throws IOException {
        //如果有这个用户
        String account = br.readLine();
        if(MainServer.userMap.containsKey(account)){
            //如果密码正确
            String pas = br.readLine();
            if(MainServer.userMap.get(account).equals(pas)){
                System.out.println("true");
                //给客户端返回消息
                writeMessage("true");
                System.out.println("ces");
                //加入到socket集合
                MainServer.list.add(socket);
                //
                takeAll(br,account);
            }
        }
    }

    //用户点击的注册按钮
    public void Register(BufferedReader br) throws IOException {
        //如果没有这个用户
        String account = br.readLine();
        if(!MainServer.userMap.containsKey(account)){
            //将此用户添加到user.txt文件中
            //用锁锁住这段代码，避免并发异常
            synchronized (MainServer.class){
                FileWriter fw = new FileWriter("user.txt",true);
                fw.write(account+"|"+br.readLine()+"\n");
                //释放资源
                fw.close();
            }
            //给客户端返回消息
            writeMessage("true");
            //将连接添加到集合中
            MainServer.list.add(socket);
            //
            takeAll(br,account);
        }
    }

    public void takeAll(BufferedReader br,String userName) throws IOException {
        //一直执行这段代码
        while (true){
            //获取用户发送过来的内容
            String str = br.readLine();
            for (Socket s : MainServer.list) {
                //s代表每个用户
                try {
                    writeMessage(s,userName+":"+str);
                }catch (Exception e){
                    MainServer.list.remove(s);
                }

            }
        }
    }

    public void writeMessage(String message) throws IOException {
        //自身写出内容
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(message);
        bw.newLine();
        bw.flush();

    }

    public void writeMessage(Socket s,String message) throws IOException {
        //可能是别的socket
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bw.write(message);
        bw.newLine();
        bw.flush();
    }


}
