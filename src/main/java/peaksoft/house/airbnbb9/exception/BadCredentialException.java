package peaksoft.house.airbnbb9.exception;
public class BadCredentialException extends RuntimeException{
    public BadCredentialException(String message) {
        super(message);
    }
}