package chat.client.common;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Vector;

import chat.client.view.ChatFrame;
import chat.client.view.MainFrame;
import chat.client.view.Sounds;
import chat.model.entity.OnlineUserDetail;

/**
 * 包含了整个客户端基本要用到的一些全局共同的变量
 * @author choldrim
 *
 */
public class CommonData
{
    private static String                            serverIP;            // 服务器ip
    private static int                               serverPort;          // 服务器端口
    private static String                            userName;            // 用户名
    private static String                            userSex;             // 性别
    private static HashMap<String, ChatFrame>        chatUserMap;         // 所有聊天用户表
    private static ChatFrame                         groupChatFrame;      // 群聊天单例窗口
    private static MainFrame                         mainFrame;           // 主窗口（单例）
    private static HashMap<String, OnlineUserDetail> onlineUserMap = null; // 所有在线用户的基本信息
    private static Vector<String>                    expressionPathVec;       // 表情路径

    static
    {
        serverIP = "192.168.11.110"; // 默认ip
        serverPort = 6688; // 默认端口
        userName = "";
        userSex = "男"; // 默认 男
        chatUserMap = new HashMap<>();
        
        // 初始化表情路径
        String directoryPath;
        try
        {
            expressionPathVec = new Vector<>();
            directoryPath = URLDecoder.decode(CommonData.class.getResource("/").toString() + "chat/client/view/imgs", "utf-8");
            directoryPath = directoryPath.replace("file:/", ""); // 要除去 getResource 返回的路径中的  file:/ 才能进行读取
            File file = new File(directoryPath);
            String[] expressionFileNames = file.list();
            for (String item : expressionFileNames)
            {
                expressionPathVec.add(directoryPath + "/" + item);
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 服务器ip
     * 
     * @return
     */
    public static String getServerIp()
    {
        return CommonData.serverIP;
    }

    public static void setServerIp(String serverIP_)
    {
        CommonData.serverIP = serverIP_;
    }

    /**
     * 服务器port
     * 
     * @return
     */
    public static int getServerPort()
    {
        return CommonData.serverPort;
    }

    public static void setServerPort(int serverPort_)
    {
        CommonData.serverPort = serverPort_;
    }

    /**
     * 用户名
     * 
     * @return
     */
    public static String getUserName()
    {
        return CommonData.userName;
    }

    public static void setUserName(String userName_)
    {
        CommonData.userName = userName_;
    }

    /**
     * 性别
     * 
     * @return
     */
    public static String getUserSex()
    {
        return CommonData.userSex;
    }

    public static void setUserSex(String userSex_)
    {
        CommonData.userSex = userSex_;
    }

    /**
     * 聊天用户表
     * 
     * @return
     */
    public static HashMap<String, ChatFrame> getChatUserMap()
    {
        return CommonData.chatUserMap;
    }

    public static void setChatUserMap(HashMap<String, ChatFrame> chatUserMap_)
    {
        CommonData.chatUserMap = chatUserMap_;
    }

    /**
     * 群聊天单例窗口
     * 
     * @return
     */
    public static ChatFrame getGroupChatFrame()
    {
        if (CommonData.groupChatFrame == null)
        {
            try
            {
                // 群聊窗体弟一次初始化时播放有新消息声音
                Sounds.getSounds().startMessageAudio();
                
                CommonData.groupChatFrame = (ChatFrame) Class.forName("chat.client.view.ChatFrame").newInstance();
            } catch(Exception e)
            {
                System.err.println("群聊天窗口类异常");
                return null;
            }
        }
        return CommonData.groupChatFrame;
    }

    public static void setGroupChatFrame(ChatFrame groupChatFrame_)
    {
        CommonData.groupChatFrame = groupChatFrame_;
    }

    /**
     * 所有在线用户基本信息表
     * 
     * @return
     */
    public static HashMap<String, OnlineUserDetail> getOnlineUserMap()
    {
        return CommonData.onlineUserMap;
    }

    public static void setOnlineUserMap(HashMap<String, OnlineUserDetail> onlineUserMap_)
    {
        CommonData.onlineUserMap = onlineUserMap_;
    }
    
    /**
     * 主窗体
     * 
     * @return
     */
    public static MainFrame getMainFrame()
    {
        return CommonData.mainFrame;
    }

    public static void setMainFrame(MainFrame mainFrame_)
    {
        CommonData.mainFrame = mainFrame_;
    }
        
    /**
     * 表情路径
     * 
     * @return
     */
    public static Vector<String> getExpressionPathVector()
    {
        return CommonData.expressionPathVec;
    }

    public static void setExpressionPathVector(Vector<String> expressionPathVector_)
    {
        CommonData.expressionPathVec = expressionPathVector_;
    }
}
