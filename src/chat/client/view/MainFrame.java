package chat.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;

import chat.client.common.CommonData;
import chat.model.entity.OnlineUserDetail;

/**
 * ��������
 * <p>
 * ��дʱ�䣺2013-06-06
 * 
 * @author choldrim
 * 
 */
public class MainFrame extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = -7791117576890422358L;

    private JLabel            userNameLab;                             // �û���
    private ChatList          onlineUserList;                          // �����û��б�
    private ChatList          groupList;                               // Ⱥ�б�
    private Font              userNameFont;                            // �û���������ʽ
    private Color             userNameColor;                           // �û���������ɫ
    private JTabbedPane       tabPane;                                 // ��Ⱥ�ĺ͵��Ľ����л�

    public MainFrame()
    {
        super("Chat -- " + CommonData.getUserName());
        
        // ��ʼ������
        intiInterface();
    }

    /**
     * ��ʼ������
     */
    private void intiInterface()
    {
        userNameFont = new Font("��԰", Font.BOLD, 18);
        userNameColor = new Color(130, 150, 250);

        JPanel topPanel = new JPanel();
        // userNameLab = new JLabel(CommonData.getUserName());
        userNameLab = new JLabel(CommonData.getUserName());
        userNameLab.setFont(userNameFont);
        userNameLab.setBackground(userNameColor);
        topPanel.add(userNameLab);
        topPanel.setBackground(Color.white);

        // ----------- �����û�list ----------//
        JScrollPane userListScrollPane = new JScrollPane();
        onlineUserList = new ChatList();
        onlineUserList.addMouseListener(new listMouseListener());
        userListScrollPane.setViewportView(onlineUserList);

        // ----------- Ⱥ��pane ------------//
        JScrollPane groupListScrollPane = new JScrollPane();
        groupList = new ChatList();
        groupList.addMouseListener(new listMouseListener());
        groupList.addItem("Group Chat");
        groupListScrollPane.setViewportView(groupList);

        // ----------- tab ----------//
        tabPane = new JTabbedPane();
        tabPane.addTab("Online Client", userListScrollPane);
        tabPane.addTab(" ~ Group ~ ", groupListScrollPane);

        // ------------------ ------------------------//
        this.getContentPane().setBackground(Color.white);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(tabPane, BorderLayout.CENTER);

        this.setAlwaysOnTop(true); // �ö�
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Ĭ�ϵ�رհ�ťʱ�˳�����
        this.setResizable(false);
        this.setSize(300, 700);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) ((screenDim.width - 350) / 1.2), (int) ((screenDim.height - 800) / 2));
    }

    /**
     * �� CommonData �б���������û������ͬ��
     * �ṩ���ⲿ�����
     */
    public void initUserList(HashMap<String, OnlineUserDetail> OnlineUserMap)
    {
        Iterator<Entry<String, OnlineUserDetail>> iter = OnlineUserMap.entrySet().iterator();

        while (iter.hasNext())
        {
            Entry<String, OnlineUserDetail> entry = (Entry<String, OnlineUserDetail>) iter.next();
            OnlineUserDetail onlineUserDetail = (OnlineUserDetail) entry.getValue();

            // �Լ������
            if (!onlineUserDetail.getUserName().equals(CommonData.getUserName()))
            {
                addUserItem(onlineUserDetail.getUserName(), onlineUserDetail.getUserSex());
            }
        }
    }

    /**
     * ��list������û�
     * 
     * @param nameStr_
     */
    public void addUserItem(String nameStr_, String userSex_)
    {
        onlineUserList.addItem(nameStr_);
    }

    /**
     * ��list���Ƴ��û�
     * 
     * @param nameStr_
     */
    public void removeUserItem(String nameStr_)
    {
        onlineUserList.removeItem(nameStr_);
    }

    /**
     * �����û��б�(��дjlist�ؼ�)
     * 
     * @author choldrim
     * 
     */
    class ChatList extends JList<Object>
    {
        private static final long serialVersionUID = 4281799662638099298L;

        private Vector<String>    onlineUserVector = new Vector<>();      // list
                                                                           // ����

        public ChatList()
        {
            // list�߶�
            this.setFixedCellHeight(80);

            // ������Ⱦ��Ϊ�Զ������Ⱦ��
            this.setCellRenderer(new UserListCellRenderer());

            // ����list����
            this.setListData(onlineUserVector);
        }

        /**
         * ��list�ṩ���item�Ľӿ�
         * 
         * @param nameStr_
         */
        public void addItem(String nameStr_)
        {
            onlineUserVector.add(nameStr_);
            this.setListData(onlineUserVector);
        }

        /**
         * ��list�ṩɾ��item�Ľӿ�
         * 
         * @param nameStr_
         */
        public void removeItem(String nameStr_)
        {
            onlineUserVector.remove(nameStr_);
            this.setListData(onlineUserVector);
        }
    }

    /**
     * list ��Ⱦ��
     * 
     * @author choldrim
     * 
     */
    class UserListCellRenderer extends JPanel implements ListCellRenderer<Object>
    {
        private static final long serialVersionUID = 3278668944817504772L;

        private String            text;                                   // list�ϵ�text
        private Color             background;                             // bei jing se 
        private Color             foreground;

        public UserListCellRenderer()
        {
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 30));
//            this.add(new JLabel("apple"));
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus)
        {
            text = (String) value;
            // background=isSelected?list.getSelectionBackground():list.getBackground();
            background = isSelected ? new Color(180, 189, 224) : new Color(249, 227, 174);
            foreground = isSelected ? list.getSelectionForeground() : list.getForeground();
            return this;
        }

        /**
         * �ػ游�����
         */
        @Override
        public void paintComponent(Graphics g)
        {
            g.setColor(background);
            g.fillRoundRect(0, 5, getWidth(), getHeight() - 10, 30, 30);
            g.setColor(foreground);
            g.drawString(text, (getWidth() - 50) / 2, (getHeight() - 10) / 2); // ���ƶ�λ�û����ı�
        }

        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(130, 120); // Cell�ĳߴ�
        }

    }

    /**
     * list ������
     * 
     * @author choldrim
     * 
     */
    class listMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            JList<?> userList = (JList<?>) e.getSource();
            if (e.getClickCount() == 2)
            {
                int index = userList.locationToIndex(e.getPoint());
                if (index >= 0)
                {
                    // Ⱥ��
                    if (e.getSource() == groupList)
                    {
                        CommonData.getGroupChatFrame().setVisible(true);
                    }

                    // һ��һ����
                    else
                    {
                        String userName = userList.getModel().getElementAt(index).toString();

                        ChatFrame chatFrame = new ChatFrame(userName);
                        chatFrame.setVisible(true);

                        // ��ӵ������û���
                        CommonData.getChatUserMap().put(userName, chatFrame);
                    }
                }
            }
        }
    }

//    // ������
//    public static void main(String[] args)
//    {
//        MainFrame mainFrame = new MainFrame();
//        mainFrame.setVisible(true);
//        for (int i = 0; i < 5; i++)
//        {
//            mainFrame.addUserItem(new Integer(i).toString(), "boy");
//        }
//    }

}
