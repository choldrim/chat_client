package chat.client.main;

import javax.swing.UIManager;

import chat.client.view.LoginFrame;

/**
 * �ͻ������
 * @author �ƿ�
 * ��дʱ�䣺2013-06-06
 */
public class MainClass
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            // ʹ��ָ�����
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            
        } catch(Exception e)
        {
        }
        
        new LoginFrame().setVisible(true);
    }
}
