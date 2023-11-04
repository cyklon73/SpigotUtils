package de.cyklon.spigotutils.exception;

public class UnsupportedVersionException extends IllegalStateException {

    public UnsupportedVersionException() {
    }

    public UnsupportedVersionException(String s) {
        super(s);
    }

    public UnsupportedVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedVersionException(Throwable cause) {
        super(cause);
    }
}
