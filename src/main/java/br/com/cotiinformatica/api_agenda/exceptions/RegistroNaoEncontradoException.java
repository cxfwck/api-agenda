package br.com.cotiinformatica.api_agenda.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {
    public RegistroNaoEncontradoException(String message) {
        super(message);
    }
}
