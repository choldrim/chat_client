package chat.client.controller;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import chat.client.common.CommonData;

/**
 * 说明：主连接类（单例）
 * 功能：在通信时提供通信静态连接实例
 * 
 * @author 财俊 编写时间：2013-06-06
 */
public class MainClientSocket
{
    private static final MainClientSocket mainSocket = new MainClientSocket(); // 定义本类实例对象
    private static Socket                 socket;                             // 连接对像
    private static ClientServerThread     serverThread;                       // 服务线程

    private MainClientSocket()
    {
        try
        {
            System.out.println(CommonData.getServerIp());
            System.out.println(CommonData.getServerPort());
            
            // 初始化参数
            socket = new Socket(CommonData.getServerIp().trim(), CommonData.getServerPort());
            serverThread = new ClientServerThread(socket);

        } catch(ConnectException e)
        {
            System.err.println("未能找到服务器");
//            e.printStackTrace();
        } catch(UnknownHostException e)
        {
            e.printStackTrace();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接类实例
     * 
     * @return 连接类实例
     */
    public static MainClientSocket getMainSocket()
    {
        return mainSocket;
    }

    /**
     * 获取服务线程
     * 
     * @return 服务线程实例
     */
    public ClientServerThread getServerThread()
    {
        return serverThread;
    }

    /**
     * 连接服务器，尝试登入
     * 
     * @param userName_
     * @param sex_
     */
    public void login(String userName_, String sex_)
    {
        // 连接服务器，开户监听
        this.getServerThread().start();

        // sendMsg格式为msgType:::name:::sex
        String sendMsg = MessageTypeEnum.Login.toString() + ":::" + userName_ + ":::" + sex_;

        // 发送消息
        this.getServerThread().sendData(sendMsg);

    }

}
