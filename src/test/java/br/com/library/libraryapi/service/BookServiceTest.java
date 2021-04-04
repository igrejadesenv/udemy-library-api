package br.com.library.libraryapi.service;

import br.com.library.libraryapi.api.exception.Businessexception;
import br.com.library.libraryapi.api.model.entity.Book;
import br.com.library.libraryapi.model.repository.BookRepository;
import br.com.library.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
    this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Save new Book")
    public void saveBookTest(){

        //cenário
        Book book = validBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when( repository.save(book) ).thenReturn(Book.builder().id(11L).author("Artur").title("As aventuras").isbn("001").build());
        //execução
       Book savedBook = service.save(book);

       //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("001");
        assertThat(savedBook.getAuthor()).isEqualTo("Artur");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
    }



    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicateIsbn(){
        //cenário
        Book book = validBook();
        String msgmErroIsbn = "Ops! Isbn já cadastrado !!!";
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));
        assertThat(exception).isInstanceOf(Businessexception.class)
                .hasMessage(msgmErroIsbn);
        Mockito.verify(repository, Mockito.never()).save(book);
    }


    private Book validBook() {
        return Book.builder().author("Artur").title("As aventuras").isbn("001").build();
    }
}
