package com.NoletoTech.usuario.infrastructure.exceptions;

public class ResourceNotFoundByException extends RuntimeException {
    public ResourceNotFoundByException(String mensagem) {
        super(mensagem);
    }

    public ResourceNotFoundByException (String mensagem, Throwable throwable){
        super (mensagem, throwable);
    }
}
