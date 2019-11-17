package com.zealyoung.bookmanager.biz;

import com.zealyoung.bookmanager.model.Ticket;
import com.zealyoung.bookmanager.model.User;
import com.zealyoung.bookmanager.model.exceptions.LoginRegisterException;
import com.zealyoung.bookmanager.service.TicketService;
import com.zealyoung.bookmanager.service.UserService;
import com.zealyoung.bookmanager.utils.ConcurrentUtils;
import com.zealyoung.bookmanager.utils.MD5;
import com.zealyoung.bookmanager.utils.TicketUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author:Zealyoung
 */
@Service
public class LoginBiz {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    /**
     * 登录逻辑，LoginController调用
     * @param email
     * @param password
     * @return T票的Ticket(String)
     * @throws Exception
     */
    public String login(String email, String password) throws Exception{
        User user = userService.getUser(email);
        //验证是否可以登录
        if(user == null)
            throw new LoginRegisterException("账户不存在，请重新输入或注册账号");
        if(!StringUtils.equals(MD5.next(password), user.getPassword()))
            throw new LoginRegisterException("密码不正确");
        //检查Ticket
        Ticket t = ticketService.getTicket(user.getId());

        if(t == null){
            t = TicketUtils.next(user.getId());
            ticketService.addTicket(t);
        }

        ConcurrentUtils.setHost(user);
        return t.getTicket();
    }

    /**
     * 登出逻辑
     * @param t
     */
    public void logout(String t){
        ticketService.deleteTicket(t);
    }

    /**
     * 注册逻辑
     * @param user
     * @return
     * @throws LoginRegisterException
     */
    public String register(User user) throws LoginRegisterException{

        if(userService.getUser(user.getEmail()) != null){
            throw new LoginRegisterException("用户账号已存在");
        }

        user.setPassword(MD5.next(user.getPassword()));
        userService.addUser(user);

        Ticket t = TicketUtils.next(userService.getUser(user.getEmail()).getId());
        ticketService.addTicket(t);

        ConcurrentUtils.setHost(user);
        return t.getTicket();
    }
}
