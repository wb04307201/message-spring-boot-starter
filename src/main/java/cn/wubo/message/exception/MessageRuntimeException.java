package cn.wubo.message.exception;

public class MessageRuntimeException extends RuntimeException {

    public MessageRuntimeException(String message) {
        super(message);
    }

    public MessageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
