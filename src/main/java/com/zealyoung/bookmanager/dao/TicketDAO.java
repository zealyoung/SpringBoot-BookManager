package com.zealyoung.bookmanager.dao;

import com.zealyoung.bookmanager.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
    @Author:Zealyoung
 */
@Repository
@Mapper
public interface TicketDAO {
    @Insert({"insert into ticket(user_id, ticket, expired_at) " +
            "values(#{userId},#{ticket},#{expiredAt})"})
    int addTicket(Ticket ticket);

    @Select({"select id, user_id, ticket, expired_at from ticket where user_id=#{uid}"})
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "userId", column = "user_id"),
            //@Result(property = "ticket", column = "nick_name"),
            @Result(property = "expiredAt", column = "expired_at", javaType = java.util.Date.class)
    })
    Ticket selectByUserId(int uid);

    @Select({"select id, user_id, ticket, expired_at from ticket where ticket=#{t}"})
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "userId", column = "user_id"),
            //@Result(property = "ticket", column = "nick_name"),
            @Result(property = "expiredAt", column = "expired_at", javaType = java.util.Date.class)
    })
    Ticket selectByTicket(String t);

    @Delete({"delete from ticket where id=#{tid}"})
    void deleteTicketById(int tid);

    @Delete({"delete from ticket where ticket=#{t}"})
    void deleteTicket(String t);
}
