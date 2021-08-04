package com.ushine.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }
    public StorageException(String message, Exception e) {
        super(message);
        this.uuid = null;
    }
    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public StorageException(Exception e) {
        super(e.getMessage());
        uuid = null;
    }

    public String getUuid() {
        return uuid;
    }
}