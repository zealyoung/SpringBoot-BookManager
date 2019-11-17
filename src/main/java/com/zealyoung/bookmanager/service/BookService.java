package com.zealyoung.bookmanager.service;

import com.zealyoung.bookmanager.dao.BookDAO;
import com.zealyoung.bookmanager.model.Book;
import com.zealyoung.bookmanager.model.enums.BookStatusEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Zealyoung
 */
@Service
public class BookService {

    @Autowired
    private BookDAO bookDAO;

    public List<Book> getAllBooks(){
        return bookDAO.SelectAll();
    }

    public int addBooks(Book book){
        return bookDAO.addBook(book);
    }

    public void deleteBooks(int id){
        bookDAO.updateBookStatus(id, BookStatusEnums.DELETE.getValue());
    }

    public void recoverBooks(int id){
        bookDAO.updateBookStatus(id, BookStatusEnums.NORMAL.getValue());
    }
}
