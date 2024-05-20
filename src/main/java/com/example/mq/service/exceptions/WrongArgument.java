package com.example.mq.service.exceptions;

/**
 * Exception thrown when body of the request is wrong
 */
public class WrongArgument  extends RuntimeException{
    public WrongArgument(String msg){
        super(msg);
    }
}
