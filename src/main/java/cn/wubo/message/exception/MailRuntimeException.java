package cn.wubo.message.exception;

public class MailRuntimeException extends RuntimeException {

    public MailRuntimeException(String message) {
        super(message);
    }

    public MailRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
