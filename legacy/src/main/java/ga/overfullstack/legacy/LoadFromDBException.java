package ga.overfullstack.legacy;

public class LoadFromDBException extends Exception {
  public LoadFromDBException(Throwable cause) {
    super(cause);
  }

  public LoadFromDBException(String msg) {
    super(msg);
  }
}
