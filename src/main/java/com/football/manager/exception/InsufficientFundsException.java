package com.football.manager.exception;

public class InsufficientFundsException extends RuntimeException {

    /**
     * Exception thrown when a player transfer cannot be completed due to insufficient funds.
     * This exception is typically thrown when the buyer team does not have enough budget
     * to complete the transfer of a player.
     *
     * @param message The detail message which explains the cause of the exception.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
