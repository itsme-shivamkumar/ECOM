package in.zeta.ecom.exceptions;

public class WrongCredentialException extends RuntimeException {
    public WrongCredentialException(String msg) {
        super(msg);
    }
}
