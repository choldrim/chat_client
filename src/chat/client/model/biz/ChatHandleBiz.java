package chat.client.model.biz;

import javax.swing.JFrame;

import chat.client.common.CommonData;
import chat.client.view.ChatFrame;
import chat.client.view.Sounds;

/**
 * ˵����������Ϣ������
 * <p>���ܣ���������Ϣ���д�������Ϣ������ʾ����Ӧ�����촰����
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
     * ����������Ϣ������ʵ��
     * 
     * @return
     */
    public static ChatHandleBiz getChatHandleBiz()
    {
        return ChatHandleBiz.chatHandleBiz;
    }

    /**
     * ����Ⱥ����Ϣ
     * 
     * @param msgStr_
     *            ��ʽ��msgType:::fromUser:::content
     */
    public void groupChatHandle(String msgStr_)
    {
        String[] msgInfos = msgStr_.split(":::");
        String fromUser = msgInfos[0];
        String content = msgInfos[1];

        // �ӻ�����Ϣ���ȡ����Ⱥ���촰�壬�����������
        CommonData.getGroupChatFrame().setVisible(true);
        CommonData.getGroupChatFrame().addChatContent(content);
    }

    /**
     * ��������Ϣ
     * 
     * @param msgStr_
     *            ��ʽ��fromUser:::content
     */
    public void singleChatHandle(String msgStr_)
    {
        String[] msgInfos = msgStr_.split(":::");
        String fromUser = msgInfos[0];
        String content = msgInfos[1];

        // �ӻ�����Ϣ���ȡĿ���û����촰�壬�����������
        if (CommonData.getChatUserMap().get(fromUser) == null)
        {
            // ����������Ϣ����
            Sounds.getSounds().startMessageAudio();

            CommonData.getChatUserMap().put(fromUser, new ChatFrame(fromUser));
        }
        
        System.out.println("isNull: " + (CommonData.getChatUserMap().get(fromUser) == null));
        
        // ������ɼ�ʱ
        if (CommonData.getChatUserMap().get(fromUser).isVisible())
        {
            // ����ɼ�������ʱ���Դ�������������
            if (CommonData.getChatUserMap().get(fromUser).getState() == JFrame.NORMAL) //JFrame.MAXIMIZED_BOTH
            {
                
            }
            else
            {
                CommonData.getChatUserMap().get(fromUser).setVisible(true);
                CommonData.getChatUserMap().get(fromUser).setExtendedState(JFrame.ICONIFIED);
            }
        }
        
        // �����岻�ɼ�ʱ
        else
        {
            // �����岻�ɼ�������ɼ�����С��
            CommonData.getChatUserMap().get(fromUser).setVisible(true);
//            CommonData.getChatUserMap().get(fromUser).setExtendedState(JFrame.ICONIFIED);
        }
        
        CommonData.getChatUserMap().get(fromUser).addChatContent(content);
    }
    
}
