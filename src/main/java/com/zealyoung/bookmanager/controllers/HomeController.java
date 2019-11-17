package com.zealyoung.bookmanager.controllers;

import com.zealyoung.bookmanager.model.Ticket;
import com.zealyoung.bookmanager.model.User;
import com.zealyoung.bookmanager.service.BookService;
import com.zealyoung.bookmanager.service.TicketService;
import com.zealyoung.bookmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
    @Author:Zealyoung
 */
@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @RequestMapping(path = {"/index"}, method = {RequestMethod.GET})
    public String bookList(Model model, @CookieValue("t") String t) {

        User host = null;
        Ticket ticket = ticketService.getTicket(t);
        if (ticket != null){
            host = userService.getUser(ticket.getUserId());
        }
        if (host != null) {
            model.addAttribute("host", host);
        }
        loadAllBooksView(model);
        return "book/books";

    }

    private void loadAllBooksView(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
    }
}
