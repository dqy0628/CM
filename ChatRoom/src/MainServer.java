import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainServer {
    //连接的socket集合
    static ArrayList<Socket> list = new ArrayList<>();
    //用户的Map集合
    static HashMap<String,String> userMap = new HashMap<>();


    public static void main(String[] args) throws IOException {
        //使用线程池减少新建销毁服务器的开销
        ThreadPoolExecutor tps = new ThreadPoolExecutor(
                3,//核心线程数
                6,//总线程数
                60,//存在时间
                TimeUnit.SECONDS,//单位
                new ArrayBlockingQueue<>(10),//阻塞队列最大长度
                Executors.defaultThreadFactory(),//线程工厂
                new ThreadPoolExecutor.AbortPolicy()//任务拒绝策略
        );
        //设置服务端
        ServerSocket ss = new ServerSocket(20001);
        //读取本地用户文件
        BufferedReader br = new BufferedReader(new FileReader("user.txt"));
        //读取每一行到map集合
        String userStr = "";
        while ((userStr = br.readLine()) != null){
            String[] strings = userStr.split("\\|");
            userMap.put(strings[0],strings[1]);
        }
        //释放资源
        br.close();


        //循环监听
        while (true){
            Socket accept = ss.accept();
            System.out.println("有客户端来连接了");
            //提交一个线程
            tps.submit(new ServerRunnable(accept));
        }
    }
}
