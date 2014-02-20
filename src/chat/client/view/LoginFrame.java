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
 * ��¼����
 * 
 * @author �ƿ�
 *         ��дʱ�䣺2013-06-06
 */
public class LoginFrame extends JFrame implements ActionListener
{
    /**
     * ���кţ��ɲ�Ҫ��
     */
    private static final long serialVersionUID = -4448454494390799227L;

    JLabel                    userNameLab;                             // �û���
    JTextField                userNameTF;                              // �û���
    JLabel                    serverIPLab;                             // ������ip
    JTextField                serverIPTF;                              // ������ip
    JLabel                    serverPortLab;                           // �������˿�
    JTextField                serverPortTF;                            // �������˿�

    JButton                   loginBtn;                                // ��¼��ť

    public LoginFrame()
    {
        this.setTitle("Login");
        
        // ��ʼ������
        initInterface();
        
        serverIPTF.setText(CommonData.getServerIp());
        serverPortTF.setText(String.valueOf(CommonData.getServerPort()));
    }

    /**
     * ��ʼ������
     */
    private void initInterface()
    {
        // ------------------ ------------------------//
        Font labelFont = new Font("��԰", Font.BOLD, 14);
        Font TFFont = new Font("��԰", Font.BOLD, 14);

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

        this.setAlwaysOnTop(true); // �ö�
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Ĭ�ϵ�رհ�ťʱ�˳�����
        this.setResizable(false);
        this.setSize(350, 200);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenDim.width - 350) / 2, (screenDim.height - 250) / 2); // ��Ļ����
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionStr = e.getActionCommand();
        switch (actionStr)
        {
            case "login":
            {
                // ����ȫ�ֵ� serverIP �� serverPort ����
                CommonData.setServerIp(serverIPTF.getText().trim());
                CommonData.setServerPort(Integer.parseInt(serverPortTF.getText().trim()));

                // �����û������Ա�
                CommonData.setUserName(userNameTF.getText().trim());
                CommonData.setUserSex("��"); // ��ʱĬ��Ϊ ���С�

                // ������¼�߳�
                MainClientSocket.getMainSocket().login(userNameTF.getText().trim(), "��");

                this.dispose();

                break;
            }

            default:
                break;
        }
    }
}
