package chat.client.model.biz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

import chat.client.common.CommonData;
import chat.client.view.MainFrame;
import chat.model.entity.OnlineUserDetail;

/**
 * �����¼�����������
 * @author choldrim
 * <p>��дʱ�� 2013-06-06
 */
public class LoginHandleBiz
{
    private static final LoginHandleBiz loginHandleBiz   = new LoginHandleBiz(); // ���屾��ʵ������
    
    private LoginHandleBiz()
    {
        
    }
    
    /**
     * ���ص�¼���������ʵ��
     * @return
     */
    public static LoginHandleBiz getLoginHandleBiz()
    {
        return LoginHandleBiz.loginHandleBiz;
    }
    
    /**
     * �����¼���
     * @param resultStr
     */
    public void loginHandle(String resultStr)
    {
        // resultStr ��ʽ��result
        int resultValue = Integer.parseInt(resultStr.trim());
        
        // ��¼�ɹ�
        if (resultValue == 1)
        {
            System.out.println("login successfully! \n I am " + CommonData.getUserName());
            
            // ����������ʵ��
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            
            // ����ȫ����Ϣ�е�������
            CommonData.setMainFrame(mainFrame);
        }
        
        // ����ͬ���û�
        else if (resultValue == 0)
        {
            JOptionPane.showMessageDialog(null, "����ͬ���û������޸��û������ٵ�¼��", "information", JOptionPane.INFORMATION_MESSAGE);
            
            // ��������
            System.exit(0);

            System.err.println("singnal...");
            return;
        }
        
        // ��¼����
        else if (resultValue == -1)
        {
            JOptionPane.showMessageDialog(null, "��¼����", "erro", JOptionPane.ERROR_MESSAGE);
            
            // ��������
            System.exit(0);
            
            return;
        }
    }
    
    /**
     * �����¼�󷵻ص������û�������Ϣ��
     * @param msgStr_ ��ʽ��objectString
     */
    public void onlineUserDetailReturnHandle(String msgStr_)
    {
        byte[] buf;// ������
        
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try
        {
            // �����ַ���,ת�����ֽ�����
            buf = decoder.decodeBuffer(msgStr_);
            
            // �ֽ�����ת������
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
            
             // ��ȡ����
            HashMap<String, OnlineUserDetail> OnlineUserMap = (HashMap<String, OnlineUserDetail>)ois.readObject();
            
            // ͬ��������Ϣ
            CommonData.setOnlineUserMap(OnlineUserMap);
            
            // ��ʼ�������û�
            CommonData.getMainFrame().initUserList(OnlineUserMap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * ���������û�����
     * @param msgStr_ ��Ϣ��ʽ��userName:::Sex
     */
    public void newClientConnectHandle(String msgStr_)
    {
        String[] msgInfos = msgStr_.split(":::");
        String userName = msgInfos[0];
        String userSex = msgInfos[1];
        
        // ��ӵ���������
        CommonData.getMainFrame().addUserItem(userName, userSex);
    }

    /**
     * �����û�����
     * @param msgStr_ ��Ϣ��ʽ��userName
     */
    public void ClientOffLineHandle(String msgStr_)
    {
        String[] msgInfos = msgStr_.split(":::");
        String userName = msgInfos[0];
        
        CommonData.getMainFrame().removeUserItem(userName);
    }
}
