package tennisclub.exceptions;

public class TennisClubManagerException extends RuntimeException{
    public TennisClubManagerException(){
        super();
    }

    public TennisClubManagerException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TennisClubManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TennisClubManagerException(String message) {
        super(message);
    }

    public TennisClubManagerException(Throwable cause) {
        super(cause);
    }
}
