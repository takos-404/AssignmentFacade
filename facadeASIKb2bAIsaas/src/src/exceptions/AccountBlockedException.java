package exceptions;
public class AccountBlockedException extends RuntimeException {
    public AccountBlockedException(String msg) { super(msg); }
}