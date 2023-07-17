package peaksoft.house.airbnbb9.exceptoin;
public class BadCredentialException extends RuntimeException{
    public BadCredentialException(String message) {
        super(message);
    }
}