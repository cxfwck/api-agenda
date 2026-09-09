package br.com.cotiinformatica.api_agenda.exceptions;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException(String message) {
        super(message);
    }
}
