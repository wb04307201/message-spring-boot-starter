package cn.wubo.message.exception;

public class DingtalkRuntimeException extends RuntimeException{

    public DingtalkRuntimeException(String message) {
        super(message);
    }

    public DingtalkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
