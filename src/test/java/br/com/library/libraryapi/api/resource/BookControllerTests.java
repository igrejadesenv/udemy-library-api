package br.com.library.libraryapi.api.resource;

import br.com.library.libraryapi.api.dto.BookDTO;
import br.com.library.libraryapi.api.exceptions.BusinessException;
import br.com.library.libraryapi.api.model.entity.Book;
import br.com.library.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTests {

	static String BOOK_API = "/api/books";
	@Autowired
	MockMvc mvc;

	@MockBean
	BookService service;

	@Test
	@DisplayName("create new book")
	public void createBookTest() throws Exception {

		BookDTO bookDTO = createNewBookDto();

		Book savedBook = Book.builder().id(10L).author("Artur").title("As aventuras").isbn("001").build();

		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
		String json = new ObjectMapper().writeValueAsString(bookDTO);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mvc
				.perform(request)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("title").value(bookDTO.getTitle()))
				.andExpect(jsonPath("author").value(bookDTO.getAuthor()))
				.andExpect(jsonPath("isbn").value(bookDTO.getIsbn()))
		;
	}


	@SneakyThrows
	@Test
	@DisplayName("error for created new book")
	public void createInvalidBookTest() {
		String json = new ObjectMapper().writeValueAsString(new BookDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", hasSize(3)));
	}

	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro. ")
	public void createBookWithDuplicateIsbn() throws Exception {
		BookDTO dto = createNewBookDto();
		String json = new ObjectMapper().writeValueAsString(dto);
		String msgmErroIsbn = "Ops! Isbn já cadastrado !!!";
		BDDMockito.given(service.save(Mockito.any(Book.class)))
				.willThrow(new BusinessException(msgmErroIsbn));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", hasSize(1)))
				.andExpect(jsonPath("errors[0]").value(msgmErroIsbn));
	}

	@Test
	@DisplayName("Deve obter um livro")
	public void getBookDetailsTest() throws Exception{
		//cenário ou (given)
		long id = 1L;
		Book book = createNewBook();
		BDDMockito.given(service.getByid(id)).willReturn(Optional.of(book));

		//execução (when)

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API.concat("/1"))
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(createNewBook().getId()))
				.andExpect(jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(jsonPath("author").value(createNewBook().getAuthor()))
				.andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));

	}

	private BookDTO createNewBookDto() {
		return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
	}
	private Book createNewBook() {
		return Book.builder().id(1L).author("Artur").title("As aventuras").isbn("001").build();
	}

}
