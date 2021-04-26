package br.com.library.libraryapi.service.impl;

import br.com.library.libraryapi.api.exceptions.BusinessException;
import br.com.library.libraryapi.api.model.entity.Book;
import br.com.library.libraryapi.model.repository.BookRepository;
import br.com.library.libraryapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw  new BusinessException("Ops! Isbn já cadastrado !!!");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getByid(long id) {
        return Optional.empty();
    }
}
