package chat.client.controller;

/**
 * ͨ����Ϣö������
 * ע�⣺����Ҫ�ϸ���������˳�һ��
 * @author �ƿ�
 * ��дʱ�䣺2013-06-06
 */
public enum MessageTypeEnum
{
    Login, // ��¼
    LoginReturn, // ���ص�¼���
    AllOnlineUserDetailReturn, // �������������û�������Ϣ
    ClientOffLine, // �û�����
    NewClientConnect, // �����û�����
    GroupChat, // Ⱥ��
    SingleChat, // һ��һ����
}
