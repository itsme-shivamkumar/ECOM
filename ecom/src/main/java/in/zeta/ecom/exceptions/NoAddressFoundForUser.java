package in.zeta.ecom.exceptions;

public class NoAddressFoundForUser extends RuntimeException {
    public NoAddressFoundForUser(String message) {
        super(message);
    }
}
