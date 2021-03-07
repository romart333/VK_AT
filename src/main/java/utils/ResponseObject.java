package utils;

public class ResponseObject<X, Y> {
    public final X response;
    public final Y errorMessage;
    public ResponseObject(X response, Y errorMessage) {
        this.response = response;
        this.errorMessage = errorMessage;
    }
}
