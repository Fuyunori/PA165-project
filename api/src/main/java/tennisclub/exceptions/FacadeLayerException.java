package tennisclub.exceptions;

public class FacadeLayerException extends RuntimeException {
    public FacadeLayerException(){
        super();
    }

    public FacadeLayerException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FacadeLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacadeLayerException(String message) {
        super(message);
    }

    public FacadeLayerException(Throwable cause) {
        super(cause);
    }
}
