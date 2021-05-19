package tennisclub.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception resulting in HTTP code 401
 * @author Ondrej Holub
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorisedException extends RuntimeException {}
