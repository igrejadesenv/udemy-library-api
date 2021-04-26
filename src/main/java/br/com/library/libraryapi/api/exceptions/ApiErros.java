package br.com.library.libraryapi.api.exceptions;

import br.com.library.libraryapi.api.exceptions.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ApiErros {
    private List<String> errors;

    public ApiErros(BindingResult bindingResult) {
        this.errors = new ArrayList<String>();
        bindingResult.getAllErrors().forEach( error -> this.errors.add(error.getDefaultMessage()) );
    }

    public ApiErros(BusinessException ex) {
        this.errors = new ArrayList<String>();
        errors.add(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
