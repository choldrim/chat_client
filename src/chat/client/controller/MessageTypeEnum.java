package chat.client.controller;

/**
 * 通信消息枚举类型
 * 注意：此类要严格与服务器端持一致
 * @author 财俊
 * 编写时间：2013-06-06
 */
public enum MessageTypeEnum
{
    Login, // 登录
    LoginReturn, // 返回登录结果
    AllOnlineUserDetailReturn, // 返回所有在线用户基本信息
    ClientOffLine, // 用户下线
    NewClientConnect, // 有新用户登入
    GroupChat, // 群聊
    SingleChat, // 一对一单聊
}
