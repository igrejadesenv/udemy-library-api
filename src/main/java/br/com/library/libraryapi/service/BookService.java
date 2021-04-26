package br.com.library.libraryapi.service;

import br.com.library.libraryapi.api.model.entity.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookService {
    Book save(Book book);

    Optional<Book> getByid(long id);
}
