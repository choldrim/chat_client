package chat.model.entity;

import java.io.Serializable;

/**
 * 在线用户实体类（可自行扩充）
 * 注意：此类要严格与服务器保持一致
 * @author choldrim
 * 编写时间：2013-06-07
 */
public class OnlineUserDetail  implements Serializable
{
    private String userName = ""; // 用户名
    private String userSex = "男"; // 用户性别
    
    /**
     * 空构造函数, 留着特殊情况下使用
     */
    public OnlineUserDetail(){} 
    
    /**
     * 带参构造，初始化参数
     * @param userName_
     * @param userSex_
     */
    public OnlineUserDetail(String userName_, String userSex_)
    {
        this.userName = userName_;
        this.userSex = userSex_;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName_)
    {
        this.userName = userName_;
    }

    public String getUserSex()
    {
        return userSex;
    }

    public void setUserSex(String userSex_)
    {
        this.userSex = userSex_;
    }
    

}
