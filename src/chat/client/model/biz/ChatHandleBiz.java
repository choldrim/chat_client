package chat.client.model.biz;

import javax.swing.JFrame;

import chat.client.common.CommonData;
import chat.client.view.ChatFrame;
import chat.client.view.Sounds;

/**
 * 说明：聊天消息处理类
 * <p>功能：对聊天消息进行处理，将消息分类显示在相应的聊天窗口中
 * 
 * @author choldrim
 * 
 */
public class ChatHandleBiz
{
    private static final ChatHandleBiz chatHandleBiz = new ChatHandleBiz();

    private ChatHandleBiz()
    {

    }

    /**
     * 返回聊天消息处理类实例
     * 
     * @return
     */
    public static ChatHandleBiz getChatHandleBiz()
    {
        return ChatHandleBiz.chatHandleBiz;
    }

    /**
     * 处理群聊消息
     * 
     * @param msgStr_
     *            格式：msgType:::fromUser:::content
     */
    public void groupChatHandle(String msgStr_)
    {
        String[] msgInfos = msgStr_.split(":::");
        String fromUser = msgInfos[0];
        String content = msgInfos[1];

        // 从基本信息类获取单例群聊天窗体，添加聊天内容
        CommonData.getGroupChatFrame().setVisible(true);
        CommonData.getGroupChatFrame().addChatContent(content);
    }

    /**
     * 处理单聊消息
     * 
     * @param msgStr_
     *            格式：fromUser:::content
     */
    public void singleChatHandle(String msgStr_)
    {
        String[] msgInfos = msgStr_.split(":::");
        String fromUser = msgInfos[0];
        String content = msgInfos[1];

        // 从基本信息类获取目标用户聊天窗体，添加聊天内容
        if (CommonData.getChatUserMap().get(fromUser) == null)
        {
            // 播放有新消息声音
            Sounds.getSounds().startMessageAudio();

            CommonData.getChatUserMap().put(fromUser, new ChatFrame(fromUser));
        }
        
        System.out.println("isNull: " + (CommonData.getChatUserMap().get(fromUser) == null));
        
        // 当窗体可见时
        if (CommonData.getChatUserMap().get(fromUser).isVisible())
        {
            // 窗体可见且正常时不对窗体做其它操作
            if (CommonData.getChatUserMap().get(fromUser).getState() == JFrame.NORMAL) //JFrame.MAXIMIZED_BOTH
            {
                
            }
            else
            {
                CommonData.getChatUserMap().get(fromUser).setVisible(true);
                CommonData.getChatUserMap().get(fromUser).setExtendedState(JFrame.ICONIFIED);
            }
        }
        
        // 当窗体不可见时
        else
        {
            // 若窗体不可见则让其可见且最小化
            CommonData.getChatUserMap().get(fromUser).setVisible(true);
//            CommonData.getChatUserMap().get(fromUser).setExtendedState(JFrame.ICONIFIED);
        }
        
        CommonData.getChatUserMap().get(fromUser).addChatContent(content);
    }
    
}
