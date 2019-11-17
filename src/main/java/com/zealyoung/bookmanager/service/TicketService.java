package com.zealyoung.bookmanager.service;

import com.zealyoung.bookmanager.dao.TicketDAO;
import com.zealyoung.bookmanager.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:Zealyoung
 */
@Service
public class TicketService {

    @Autowired
    private TicketDAO ticketDAO;

    public void addTicket(Ticket t){
        ticketDAO.addTicket(t);
    }

    public Ticket getTicket(int uid){
        return ticketDAO.selectByUserId(uid);
    }

    public Ticket getTicket(String t){
        return ticketDAO.selectByTicket(t);
    }

    public void deleteTicket(int tid){
        ticketDAO.deleteTicketById(tid);
    }

    public void deleteTicket(String t){
        ticketDAO.deleteTicket(t);
    }
}
