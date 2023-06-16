package edu.uob.Exceptions;

public class DBException extends Exception {

    static final long serialVersionUID = 1L;

    String message;

    public DBException(String message){
        this.message = message;
    }

    public String getMessage(){
        return "Something is going on currently; But I can't give your more information;";
    }
}
