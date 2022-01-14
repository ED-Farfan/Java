import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
  
  public long[][] multiplica_matrices(long[][] A, long[][] B)throws RemoteException;
  public long[][] parte_matriz(long[][] A, int inicio)throws RemoteException;
  public long checksum(long[][] m) throws RemoteException;
}
