package chat.client.main;

import javax.swing.UIManager;

import chat.client.view.LoginFrame;

/**
 * 客户端入口
 * @author 财俊
 * 编写时间：2013-06-06
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
            // 使用指定风格
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            
        } catch(Exception e)
        {
        }
        
        new LoginFrame().setVisible(true);
    }
}
