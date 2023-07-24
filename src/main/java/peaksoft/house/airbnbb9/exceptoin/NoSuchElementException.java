package peaksoft.house.airbnbb9.exceptoin;
public class NoSuchElementException extends RuntimeException{

    public NoSuchElementException(String message) {
        super(message);
    }
    public NoSuchElementException(Class<?> clazz, long id) {
        super(String.format("Entity %s with id %d not found", clazz.getSimpleName(), id));
    }

}
