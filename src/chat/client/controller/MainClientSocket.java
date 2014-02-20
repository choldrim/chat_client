package chat.client.controller;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import chat.client.common.CommonData;

/**
 * ˵�����������ࣨ������
 * ���ܣ���ͨ��ʱ�ṩͨ�ž�̬����ʵ��
 * 
 * @author �ƿ� ��дʱ�䣺2013-06-06
 */
public class MainClientSocket
{
    private static final MainClientSocket mainSocket = new MainClientSocket(); // ���屾��ʵ������
    private static Socket                 socket;                             // ���Ӷ���
    private static ClientServerThread     serverThread;                       // �����߳�

    private MainClientSocket()
    {
        try
        {
            System.out.println(CommonData.getServerIp());
            System.out.println(CommonData.getServerPort());
            
            // ��ʼ������
            socket = new Socket(CommonData.getServerIp().trim(), CommonData.getServerPort());
            serverThread = new ClientServerThread(socket);

        } catch(ConnectException e)
        {
            System.err.println("δ���ҵ�������");
//            e.printStackTrace();
        } catch(UnknownHostException e)
        {
            e.printStackTrace();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ������ʵ��
     * 
     * @return ������ʵ��
     */
    public static MainClientSocket getMainSocket()
    {
        return mainSocket;
    }

    /**
     * ��ȡ�����߳�
     * 
     * @return �����߳�ʵ��
     */
    public ClientServerThread getServerThread()
    {
        return serverThread;
    }

    /**
     * ���ӷ����������Ե���
     * 
     * @param userName_
     * @param sex_
     */
    public void login(String userName_, String sex_)
    {
        // ���ӷ���������������
        this.getServerThread().start();

        // sendMsg��ʽΪmsgType:::name:::sex
        String sendMsg = MessageTypeEnum.Login.toString() + ":::" + userName_ + ":::" + sex_;

        // ������Ϣ
        this.getServerThread().sendData(sendMsg);

    }

}
