package edu.uob.Exceptions;

public class parseException extends DBException{

    static final long serialVersionUID = 2L;

    public parseException(String message){
        super(message);
    }

    @Override
    public String getMessage(){
        return message;
    }
}
