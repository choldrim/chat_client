package chat.client.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import chat.client.model.biz.ChatHandleBiz;
import chat.client.model.biz.LoginHandleBiz;

/**
 * 说明：服务线程
 * 功能：接收处理消息线程,客户端的背后工作线程
 * @author 财俊
 * 编写时间：2013-06-06
 */
public class ClientServerThread extends Thread
{
    private Socket socket = null;
    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;
    private String separatorStr = ""; // 消息分隔符
    boolean isListen = false;
    
    public ClientServerThread(Socket socket_)
    {
        try
        {
            separatorStr = ":::";
            this.socket = socket_;
            
            // 初始化输入输出流
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    

    /**
     * 发送消息
     * @param msgStr 消息格式化内容
     */
    public void sendData(String msgStr)
    {
        try
        {
            dos.writeUTF(msgStr);
            
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 线程入口
     */
    @Override
    public void run()
    {
        try
        {
            // 消息内容
            String msgStr = ""; 
            
            // 分隔符在字符串中的位置
            int separatorIndex = 0; 
            
            // 是否接收消息
            isListen = true; 
            
            while(isListen)
            {
                // 接收消息
                msgStr = dis.readUTF();
                
                // 确定分隔符在字符串中的位置
                separatorIndex = msgStr.indexOf(separatorStr);
                
                // 提取消息类型
                MessageTypeEnum msgType = MessageTypeEnum.valueOf(msgStr.substring(0, separatorIndex));
                
                switch (msgType)
                {
                    
                    /***************** 处理 Login 结果 ******************/
                    case LoginReturn:
                    {
                        // 交给biz层进行处理
                        LoginHandleBiz.getLoginHandleBiz().loginHandle(msgStr.substring(separatorIndex + 3));
                        
                        break;
                    }
                    
                    /***************** 处理 返回所有在线用户基本信息 ******************/
                    case AllOnlineUserDetailReturn:
                    {
                        // 交给biz层进行处理
                        LoginHandleBiz.getLoginHandleBiz().onlineUserDetailReturnHandle(msgStr.substring(separatorIndex + 3));
                        
                        break;
                    }
                    
                     /***************** 处理 有新用户上线 ******************/
                    case NewClientConnect:
                    {
                        // 交给biz层进行处理
                        LoginHandleBiz.getLoginHandleBiz().newClientConnectHandle(msgStr.substring(separatorIndex + 3));
                        
                        break;
                    }
                    
                     /***************** 处理 用户下线 ******************/
                    case ClientOffLine:
                    {
                        // 交给biz层进行处理
                        LoginHandleBiz.getLoginHandleBiz().ClientOffLineHandle(msgStr.substring(separatorIndex + 3));
                        
                        break;
                    }
                    
                    /***************** 群聊消息 ******************/
                    case GroupChat:
                    {
                        // 交给biz层进行处理
                        ChatHandleBiz.getChatHandleBiz().groupChatHandle(msgStr.substring(separatorIndex + 3));
                        
                        break;
                    }

                    /***************** 一对一聊天消息 ******************/
                    case SingleChat:
                    {
                        // 交给biz层进行处理
                        ChatHandleBiz.getChatHandleBiz().singleChatHandle(msgStr.substring(separatorIndex + 3));
                        
                        break;
                    }

                    default:
                        break;
                }
                
            }
            
        }
        catch(SocketException e)
        {
            System.err.println("与服务器断开连接！！");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
