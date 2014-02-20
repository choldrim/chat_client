package chat.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import chat.client.common.CommonData;
import chat.client.controller.MainClientSocket;

/**
 * 登录窗体
 * 
 * @author 财俊
 *         编写时间：2013-06-06
 */
public class LoginFrame extends JFrame implements ActionListener
{
    /**
     * 序列号（可不要）
     */
    private static final long serialVersionUID = -4448454494390799227L;

    JLabel                    userNameLab;                             // 用户名
    JTextField                userNameTF;                              // 用户名
    JLabel                    serverIPLab;                             // 服务器ip
    JTextField                serverIPTF;                              // 服务器ip
    JLabel                    serverPortLab;                           // 服务器端口
    JTextField                serverPortTF;                            // 服务器端口

    JButton                   loginBtn;                                // 登录按钮

    public LoginFrame()
    {
        this.setTitle("Login");
        
        // 初始化界面
        initInterface();
        
        serverIPTF.setText(CommonData.getServerIp());
        serverPortTF.setText(String.valueOf(CommonData.getServerPort()));
    }

    /**
     * 初始化界面
     */
    private void initInterface()
    {
        // ------------------ ------------------------//
        Font labelFont = new Font("幼园", Font.BOLD, 14);
        Font TFFont = new Font("幼园", Font.BOLD, 14);

        JPanel centerPanel = new JPanel();
        userNameLab = new JLabel("User Name:");
        userNameTF = new JTextField();
        userNameLab.setFont(labelFont);
        userNameTF.setFont(TFFont);
        userNameTF.setActionCommand("login");
        userNameTF.addActionListener(this);
        userNameLab.setBounds(20, 30, 100, 20);
        userNameTF.setBounds(130, 30, 150, 25);

        serverIPLab = new JLabel("Server IP:");
        serverIPTF = new JTextField();
        serverIPLab.setFont(labelFont);
        serverIPTF.setFont(TFFont);
        serverIPLab.setBounds(20, 60, 100, 20);
        serverIPTF.setBounds(130, 60, 150, 25);

        serverPortLab = new JLabel("Server Port:");
        serverPortTF = new JTextField();
        serverPortLab.setFont(labelFont);
        serverPortTF.setFont(TFFont);
        serverPortLab.setBounds(20, 90, 100, 20);
        serverPortTF.setBounds(130, 90, 150, 25);

        centerPanel.setLayout(null);
        centerPanel.setBackground(Color.white);
        centerPanel.add(userNameLab);
        centerPanel.add(userNameTF);
        centerPanel.add(serverIPLab);
        centerPanel.add(serverIPTF);
        centerPanel.add(serverPortLab);
        centerPanel.add(serverPortTF);

        // ------------------ ------------------------//
        JPanel loginPanel = new JPanel(new FlowLayout());
        loginBtn = new JButton("     Login        ");
        loginBtn.setActionCommand("login");
        loginBtn.addActionListener(this);
        loginPanel.setBackground(Color.white);
        loginPanel.add(loginBtn);

        // ------------------ ------------------------//
        this.getContentPane().setBackground(Color.white);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.getContentPane().add(loginPanel, BorderLayout.SOUTH);

        this.setAlwaysOnTop(true); // 置顶
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 默认点关闭按钮时退出程序
        this.setResizable(false);
        this.setSize(350, 200);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenDim.width - 350) / 2, (screenDim.height - 250) / 2); // 屏幕居中
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionStr = e.getActionCommand();
        switch (actionStr)
        {
            case "login":
            {
                // 设置全局的 serverIP 和 serverPort 参数
                CommonData.setServerIp(serverIPTF.getText().trim());
                CommonData.setServerPort(Integer.parseInt(serverPortTF.getText().trim()));

                // 设置用户名和性别
                CommonData.setUserName(userNameTF.getText().trim());
                CommonData.setUserSex("男"); // 暂时默认为 “男”

                // 启动登录线程
                MainClientSocket.getMainSocket().login(userNameTF.getText().trim(), "男");

                this.dispose();

                break;
            }

            default:
                break;
        }
    }
}
