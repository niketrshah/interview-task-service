package com.tradeshift.error;

public class TaskNotUpdatedException extends Throwable {
    private final String _message;

    public TaskNotUpdatedException(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }
}
