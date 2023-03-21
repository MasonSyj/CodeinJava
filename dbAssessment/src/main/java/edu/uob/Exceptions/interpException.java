package edu.uob.Exceptions;

public class interpException extends DBException{

    static final long serialVersionUID = 3L;

    public interpException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
