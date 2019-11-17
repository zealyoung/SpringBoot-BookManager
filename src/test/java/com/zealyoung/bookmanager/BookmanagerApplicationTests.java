package com.zealyoung.bookmanager;

import com.zealyoung.bookmanager.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookmanagerApplicationTests {

    @Autowired
    TicketService ticketService;

    @Test
    void contextLoads() {

    }

}
