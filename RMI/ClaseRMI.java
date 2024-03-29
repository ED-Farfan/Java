import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI
{
  // es necesario que el contructor default de la clase ClaseRMI invoque el constructor de la super-clase
  public ClaseRMI() throws RemoteException
  {
    super( );
  }
  public String mayusculas(String s) throws RemoteException
  {
    return s.toUpperCase();
  }
  public int suma(int a,int b)
  {
    return a + b;
  }
  public long checksum(int[][] m) throws RemoteException
  {
    long s = 0;
    for (int i = 0; i < m.length; i++)
      for (int j = 0; j < m[0].length; j++)
        s += m[i][j];
    return s;
  }
}
//https://www.youtube.com/watch?v=JTW_BTWmbaI