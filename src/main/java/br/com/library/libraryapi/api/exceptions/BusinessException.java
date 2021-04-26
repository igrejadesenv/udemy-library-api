package br.com.library.libraryapi.api.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String msgm) {
        super(msgm);
    }
}
