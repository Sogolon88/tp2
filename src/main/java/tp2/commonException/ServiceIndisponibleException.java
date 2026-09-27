package tp2.commonException;

public class ServiceIndisponibleException extends RuntimeException {
    public ServiceIndisponibleException() {
        super("Service de taux d'échange indisponible");
    }
}