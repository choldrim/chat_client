package chat.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;


import chat.client.common.CommonData;
import chat.client.controller.MainClientSocket;
import chat.client.controller.MessageTypeEnum;

/**
 * �������
 * 
 * @author �ƿ�
 * <p>��дʱ�� 2013-06-06
 */
public class ChatFrame extends JFrame implements ActionListener
{
    private static final long serialVersionUID = -7325097074721032093L;

    private JLabel            titleLab;                                // ����
    private JTextArea         inputTA;                                 // �����
    private ChatPanel         chatContentPanel;                        // ����������ʾ��
    private ControllToolbar   controllToolbar;                         // ������
    private JButton           sendMsgBtn;                              // ���Ͱ�ť
    private JButton           closeBtn;                                // �رհ�ť
    private String            targetUser;                              // ���������Ŀ���û�
    private boolean           isSingleChat;                            // �Ƿ���һ��һ���죨falseȺ�ģ�
    private Font              inputFont;                               // ����������ʽ
    private String            chatRecordsStr;                          // �����¼
    private ChatFrame         parentFrame;                             // ���ӿؼ����ڲ��������

    // Ⱥ�����
    public ChatFrame()
    {
        isSingleChat = false;
        this.targetUser = "";
        
        // ���ñ���
        titleLab = new JLabel("<html><span style='font-weight:bold;font-size:14px;'>Group Chat</span></html>");

        // ��ʼ������
        intiInterface();
        
        // ��ʼ����������
        initBasicData();
    }

    // һ��һ���죨������ڣ�
    public ChatFrame(String name_)
    {
        isSingleChat = true;
        this.targetUser = name_;
        
        // ���ñ���
        titleLab = new JLabel("<html>Chatting with <font color = blue>" + targetUser + "</font></html>");

        // ��ʼ������
        intiInterface();

        // ��ʼ����������
        initBasicData();
    }

    /**
     * ��ʼ������
     */
    private void intiInterface()
    {
        // ----------- �������� ------------//
        JPanel northPanel = new JPanel();
        northPanel.add(titleLab);
        northPanel.setBackground(Color.white);

        // ---------- �в���ʾ���� ----------//
        JPanel centerPanel = new JPanel(new BorderLayout());
        // message
        chatContentPanel = new ChatPanel();
        JScrollPane msgScrollPane = new JScrollPane(chatContentPanel);
        msgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        msgScrollPane.setBorder(new TitledBorder("Message"));

        // center south panel ( tool bar + input )
        JPanel centerSouthPanel = new JPanel(new BorderLayout());
        inputFont = new Font(null, Font.PLAIN, 12);
        controllToolbar = new ControllToolbar();
        inputTA = new JTextArea(4, 0);
        inputTA.setAutoscrolls(true);
        JScrollPane inputScroll = new JScrollPane(inputTA);
        inputScroll.setBorder(new TitledBorder("Input"));
        inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerSouthPanel.setBackground(Color.white);
        centerSouthPanel.add(controllToolbar, BorderLayout.NORTH);
        centerSouthPanel.add(inputScroll, BorderLayout.SOUTH);
        inputTA.setLineWrap(true);
        inputTA.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                // Ctrl + enter ����
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    inputTA.setText(inputTA.getText() + "\n");
                }

                // enter ����
                else if (!e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    sendMsgBtn.doClick();
                    e.setKeyChar('\0');
                }
            }
        });

        centerPanel.add(msgScrollPane, BorderLayout.CENTER);
        centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);

        msgScrollPane.setBackground(Color.white);
        inputScroll.setBackground(Color.white);
        centerPanel.setBackground(Color.white);

        // ----------- �Ϸ���ť ------------//
        JPanel southPanel = new JPanel();
        sendMsgBtn = new JButton("Send");
        closeBtn = new JButton("Close");
        sendMsgBtn.setActionCommand("send");
        sendMsgBtn.addActionListener(this);
        closeBtn.setActionCommand("close");
        closeBtn.addActionListener(this);
        southPanel.add(sendMsgBtn);
        southPanel.add(closeBtn);
        southPanel.setBackground(Color.white);

        // ----------- ����ͼƬ------------//
//        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // ------------------ ------------------------//
        this.getContentPane().setBackground(Color.white);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(northPanel, BorderLayout.NORTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Ĭ�ϵ�رհ�ťʱ�˳�����
        this.setMinimumSize(new Dimension(550, 500));
        this.setSize(550, 500);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenDim.width - 550) / 2, (screenDim.height - 500) / 2); // ��Ļ����
        
        // ��ӹر��¼�
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                eixtCurrentChat(); 
            }
        });
    }

    /**
     * ��ʼ����������
     */
    private void initBasicData()
    {
        // �������
        this.setTitle("I am " + CommonData.getUserName());
        
        // �������¼����ֵ
        this.chatRecordsStr = "";

        // ��ʼ�����ര��
        this.parentFrame = this;
        
        // ����������Ϣ����
//        Sounds.getSounds().startMessageAudio(); // �����ⲥ�ţ��ĳ��ڽ�����Ϣʱ����
        
    }
    
    /**
     * �˳���ǰ����
     */
    private void eixtCurrentChat()
    {
        // ���ǲ���Ⱥ����������û������Ƴ���Ӧ��
        if(isSingleChat)
        {
            CommonData.getChatUserMap().remove(targetUser);
        }
        
        parentFrame.dispose();
    }
    
    /**
     * ������
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionStr = e.getActionCommand();
        switch (actionStr)
        {
            case "send":
            {
                String inputText = inputTA.getText().trim();
                String fontStyle = "";
                fontStyle += inputFont.isBold() ? ("font-weight:bold;") : "";
                fontStyle += inputFont.isItalic() ? ("font-style:italic;") : "";
                String chatContent = "<span style='font-size:" + inputFont.getSize() + "px;" + fontStyle + "'>"
                        + inputText + "</span>";
                String formatContent = ""; // ��ʽ������������

                if (!chatContent.equals(""))
                {
                    // ����������������Ϣ
                    String sendMsg = "";

                    // �������ڸ�ʽ
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // ��ȡ��ǰϵͳʱ��
                    String timeStr = df.format(new Date());

                    // ��ʽ����������
                    formatContent = "<html>" + "<font color = green>" + CommonData.getUserName() + "  " + timeStr
                            + " :</font><br>" + chatContent + "</html>";

                    // һ��һ����
                    if (isSingleChat)
                    {
                        // ��Ϣ��ʽ��msgType:::fromUser:::toUser:::formatContent
                        sendMsg = MessageTypeEnum.SingleChat.toString() + ":::" + CommonData.getUserName() + ":::"
                                + targetUser + ":::" + formatContent;

                    }
                    // Ⱥ��
                    else
                    {
                        // ��Ϣ��ʽ��msgType:::fromUser:::formatContent
                        sendMsg = MessageTypeEnum.GroupChat.toString() + ":::" + CommonData.getUserName() + ":::"
                                + formatContent;
                    }

                    // ����������ݵ��������촰��
                    addChatContent("<html><font color = blue>" + CommonData.getUserName() + " (me) :</font><br>"
                            + chatContent + "</html>");

                    // ������Ϣ��������
                    MainClientSocket.getMainSocket().getServerThread().sendData(sendMsg);

                    // ������촰��
                    inputTA.setText("");

                } else
                {
                    JOptionPane.showMessageDialog(this, "Can not send an empty message", "infor",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                break;
            }

            case "close":
            {
                eixtCurrentChat();
                break;
            }

            default:
                break;
        }
    }

    /**
     * �����������
     * 
     * @param chatContent_
     */
    public void addChatContent(String chatContent_)
    {
        chatContentPanel.addContent(chatContent_);

        // ��ӵ������¼�ַ�����
        chatRecordsStr += (chatContent_ + "<br>");
    }

    /**
     * ��д������panel
     * 
     * @author choldrim
     * 
     */
    class ChatPanel extends JPanel
    {
        private static final long serialVersionUID = 8253395484207504951L;

        public ChatPanel()
        {
            initInterface();
        }

        private void initInterface()
        {
            // ����
            this.setBackground(Color.white);

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        /**
         * ���ⲿʹ�õ�������ݽӿ�
         * 
         * @param content_
         */
        public void addContent(String content_)
        {
            
            // �ȴ�һ��label
            JLabel dialogLab = new JLabel(content_);

            // �����
            dialogLab.setAlignmentX(Component.LEFT_ALIGNMENT);

            // ��ӵ������
            this.add(dialogLab);

            // ˢ�½���
            this.updateUI();
        }

    }

    /**
     * ��д������
     * 
     * @author choldrim
     * 
     */
    class ControllToolbar extends JToolBar implements ActionListener, ItemListener
    {
        private static final long serialVersionUID = -3935245682148689477L;

        public ControllToolbar()
        {
            this.intiInterface();
        }

        /**
         * ��ʼ��������
         */
        public void intiInterface()
        {
            try
            {
                // ���ͱ���
                JButton expressionBtn;
                expressionBtn = new JButton("Send Expression", new ImageIcon(URLDecoder.decode(this.getClass().getResource("/").toString() + "/imgs/zk.gif", "utf-8")));
                
                // font size
                JLabel fontSizeLab = new JLabel("    Size: ");
                JComboBox<String> fontSizeCombbox = new JComboBox<>();
                fontSizeCombbox.addItem("10");
                fontSizeCombbox.addItem("12");
                fontSizeCombbox.addItem("14");
                fontSizeCombbox.addItem("16");
                fontSizeCombbox.addItem("18");
                fontSizeCombbox.addItem("22");
                fontSizeCombbox.setSelectedItem("12"); // Ĭ�ϴ�СΪ 12
                
                // font style
                JButton fontBoldBtn = new JButton("Bold");
                JButton fontItalicBtn = new JButton("Italic");
                
                // save records
                JButton saveRecordsBtn = new JButton("Save Records");
                
                // layout
                this.setLayout(new FlowLayout(LEADING, 10, 5));
                
                this.add(fontSizeLab);
                this.add(fontSizeCombbox);
                this.add(fontBoldBtn);
                this.add(fontItalicBtn);
                this.add(expressionBtn);
                this.add(saveRecordsBtn);
                
                // ��Ӽ���
                fontBoldBtn.addActionListener(this);
                fontItalicBtn.addActionListener(this);
                saveRecordsBtn.addActionListener(this);
                expressionBtn.addActionListener(this);
                fontSizeCombbox.addItemListener(this);
                fontBoldBtn.setActionCommand("SetFontBlod");
                fontItalicBtn.setActionCommand("SetFontItalic");
                expressionBtn.setActionCommand("SendExpression");
                saveRecordsBtn.setActionCommand("SaveRecords");
                
                // ���ò��ɸ���
                this.setFloatable(false);
                
            } catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * �������¼�
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String actionStr = e.getActionCommand();
            switch (actionStr)
            {
                case "SetFontBlod":
                {
                    // ��ԭ���Ǵ���
                    if (inputFont.isBold())
                    {
                        // ��ԭ����б��
                        if (inputFont.isItalic())
                        {
                            // �ĳ�ֻ��б��
                            inputFont = new Font(inputFont.getFamily(), Font.ITALIC, inputFont.getSize());
                        } else
                        {
                            // �ĳ��� style
                            inputFont = new Font(inputFont.getFamily(), Font.PLAIN, inputFont.getSize());
                        }
                    }
                    // ��ԭ�����Ǵ���
                    else
                    {
                        // ��ԭ����б��
                        if (inputFont.isItalic())
                        {
                            // �ĳɴ�б��
                            inputFont = new Font(inputFont.getFamily(), Font.ITALIC | Font.BOLD, inputFont.getSize());
                        } else
                        {
                            // �ĳ�ֻ�Ǵ���
                            inputFont = new Font(inputFont.getFamily(), Font.BOLD, inputFont.getSize());
                        }
                    }

                    // Ӧ�õ������
                    inputTA.setFont(inputFont);

                    break;
                }

                case "SetFontItalic":
                {
                    // ��ԭ����б��
                    if (inputFont.isItalic())
                    {
                        // ��ԭ���Ǵ���
                        if (inputFont.isBold())
                        {
                            // �ĳ�ֻ�Ǵ���
                            inputFont = new Font(inputFont.getFamily(), Font.BOLD, inputFont.getSize());
                        } else
                        {
                            // �ĳ��� style
                            inputFont = new Font(inputFont.getFamily(), Font.PLAIN, inputFont.getSize());
                        }
                    }
                    // ��ԭ������б��
                    else
                    {
                        // ��ԭ���Ǵ���
                        if (inputFont.isBold())
                        {
                            // �ĳɴ�б��
                            inputFont = new Font(inputFont.getFamily(), Font.ITALIC | Font.BOLD, inputFont.getSize());
                        } else
                        {
                            // �ĳ�ֻ��б��
                            inputFont = new Font(inputFont.getFamily(), Font.ITALIC, inputFont.getSize());
                        }
                    }

                    // Ӧ�õ������
                    inputTA.setFont(inputFont);

                    break;
                }

                case "SaveRecords":
                {
                    try
                    {
                        // ѡ�񱣴�·��
                        FileDialog saveRecordsFD;
                        if (isSingleChat)
                        {
                            saveRecordsFD = new FileDialog(CommonData.getChatUserMap().get(targetUser), "Save Records",
                                    FileDialog.SAVE);
                        } else
                        {
                            saveRecordsFD = new FileDialog(CommonData.getGroupChatFrame(), "Save Records",
                                    FileDialog.SAVE);
                        }
                        saveRecordsFD.setFile("records.html"); // ���ñ�����
                        saveRecordsFD.setVisible(true);
                        if (saveRecordsFD.getDirectory() == null)
                        {
                            break;
                        }

                        // д���¼�ļ�
                        String recordPath = saveRecordsFD.getDirectory() + saveRecordsFD.getFile();
                        RandomAccessFile raf = new RandomAccessFile(new java.io.File(recordPath), "rw");
                        byte[] recordsArray = chatRecordsStr.getBytes();
                        raf.write(recordsArray);
                        raf.close();

                    } catch(FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    } catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    break;
                }

                case "SendExpression":
                {
                    ExpressionPanel expressionPanel = new ExpressionPanel();
                    JDialog dialog = new JDialog();
                    dialog.setUndecorated(true);
                    dialog.setModal(false);
                    dialog.getContentPane().add(expressionPanel, BorderLayout.CENTER);
                    dialog.setSize(200, 150);
                    int x = ((JButton)e.getSource()).getLocation().x + controllToolbar.getLocation().x + parentFrame.getLocation().x;
                    int y = (parentFrame.getLocation().y + parentFrame.getSize().height) - 370;
                    dialog.setLocation(x, y);
                    dialog.setVisible(true);
                    
                    // �ñ������ʧȥ����͹ر�
                    dialog.addWindowFocusListener(new WindowAdapter()
                    {
                        public void windowLostFocus(WindowEvent e)
                        {
                            ((JDialog)e.getSource()).dispose();
                        };
                    });
                    
                    break;
                }
                default:
                    break;
            }
        }

        /**
         * �����С
         * 
         * @param e
         */
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            inputFont = new Font(inputFont.getFamily(), inputFont.getStyle(), Integer.parseInt(e.getItem().toString()));

            // Ӧ�õ������
            inputTA.setFont(inputFont);
        }
    }

    /**
     * �������
     * @author choldrim
     *
     */
    class ExpressionPanel extends JPanel implements MouseListener
    {
        private static final long serialVersionUID = -8553820156460304386L;

        public ExpressionPanel()
        {
            initInterface();
        }
        
        private void initInterface()
        {
            this.setLayout(new GridLayout(4, 5, 5, 5));
            
            for (String path : CommonData.getExpressionPathVector())
            {
                JLabel label = new JLabel(new ImageIcon(path));
                label.addMouseListener(this);
                label.setBorder(new TitledBorder(""));
                this.add(label);
            }
            
            this.setBorder(new TitledBorder("Expression"));
            this.setBackground(Color.white);
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
//            dispose();
        }

        @Override
        public void mousePressed(MouseEvent e){}

        @Override
        public void mouseReleased(MouseEvent e){}

        @Override
        public void mouseEntered(MouseEvent e)
        {
            // TODO �Զ����ɵķ������
            
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            // TODO �Զ����ɵķ������
            
        }
    }
    
////     ������
//    public static void main(String[] args)
//    {
//        try
//        {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//
//        } catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        ChatFrame chatFrame = new ChatFrame();
//        chatFrame.setVisible(true);

//        chatFrame.addChatContent("<html><span style='background-image:url"
//                + "(H:/javaWorkspace_2/javaʵ��5(chat_client)/src/chat/client/view/imgs/zk.gif);' >"
//                + "</span><font color = blue> choldrim </font></html>");
//
//        class sounda extends Applet
//        {
//            public sounda()
//            {
////                AudioClip ac = getAudioClip(getCodeBase(), "/sound/beyond.wav");
////                ac.play();
//                // ac.loop();
//                // ac.stop();
//                
//                 AudioStream as;
//                try
//                {
//                    String filePath = URLDecoder.decode(CommonData.class.getResource("/").toString() + "chat/client/view/sound/beyond.wav", "utf-8");
//                    filePath = filePath.replace("file:/", ""); // Ҫ��ȥ getResource ���ص�·���е�  file:/ ���ܽ��ж�ȡ
//                    FileInputStream fileau=new  FileInputStream(filePath);
//                    as = new AudioStream(fileau);
//                    AudioPlayer.player.start(as);
//                    
//                } catch(IOException e)
//                {
//                    // TODO �Զ����ɵ� catch ��
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        new sounda();

//    }

}
