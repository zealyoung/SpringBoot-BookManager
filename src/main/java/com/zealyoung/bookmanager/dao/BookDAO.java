package com.zealyoung.bookmanager.dao;

import com.zealyoung.bookmanager.model.Book;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
    @Author:Zealyoung
*/
@Repository
@Mapper
public interface BookDAO {

    @Select({"select id, name, author, price, status from book"})
    List<Book> SelectAll();

    @Insert({"insert into book(name, author, price) values (#{name},#{author},#{price})"})
    int addBook(Book book);

    @Update({"update book set status=#{status} where id=#{id}"})
    void updateBookStatus(@Param("id") int id, @Param("status") int status);
}
