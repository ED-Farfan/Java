import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI {

  // es necesario que el contructor default de la clase ClaseRMI invoque el constructor de la super-clase
  public ClaseRMI() throws RemoteException {
    super();
  }

  public long[][] multiplica_matrices(long[][] A, long[][] B)throws RemoteException {
    String format;
    String MatA="";
    String MatB="";
    String MatC="";
    long[][] C = new long[A.length][A.length];
    int Filas = A.length;
    int Columnas = A[0].length;
    
    for (int i = 0; i < Filas; i++) 
    for (int j = 0; j < Filas; j++) 
        for (int k = 0;k < Columnas;k++ ) C[i][j] += A[i][k] * B[j][k];
    
    format ="\n--------------REPORTE MULTIPLICACION------------------------";
    format +="\n\t\t Matriz A";
    if (Filas <= 5 && Columnas <= 5) {
      for (int i = 0; i < Filas; i++) {
        for (int j = 0; j < Columnas; j++) {
            MatA+="\t" + String.valueOf(A[i][j]);
            MatB+="\t" + String.valueOf(B[i][j]);            
        }
        MatA += '\n';
        MatB += '\n';        
      }      
      for (int i = 0; i < C.length; i++) {
        for (int j = 0; j < C[0].length; j++) {            
            MatC+="\t" + String.valueOf(C[i][j]);
        }        
        MatC += '\n';
      }

      format +="\n\n"+MatA;
      format +="\n\n\t\t Matriz B\n";
      format +="\n"+MatB;
      format +="\n\n\t\t Matriz C\n";
      format +="\n"+MatC+"\n";      
    } else {
      format +="\n Tamaño: Filas: "+String.valueOf(Filas)+" x Columas:"+String.valueOf(Columnas)+"\n";
      format +="\n\n\t Matriz B";
      format +="\n Tamaño: Filas: "+String.valueOf(Filas)+" x Columas:"+String.valueOf(Columnas)+"\n";
      format +="\n\n\t Matriz C ";
      format +="\n Tamaño: Filas: "+String.valueOf(C.length)+" x Columas:"+String.valueOf(C[0].length)+"\n";
    }
    format+="\n---------------------------------------------------------\n";
    System.out.print(format);
    return C;
  }

  public long[][] parte_matriz(long[][] A, int inicio)throws RemoteException{
    int N = A.length;
    long[][] M = new long[N / 2][N];
    for (int i = 0; i < N / 2; i++) for (int j = 0; j < N; j++) M[i][j] =
      A[i + inicio][j];
    return M;
  }

  public long checksum(long[][] m) throws RemoteException {
    long s = 0;
    for (int i = 0; i < m.length; i++) for (
      int j = 0;
      j < m[0].length;
      j++
    ) s += m[i][j];
    String f = "\n-------------------REPORTE DE CHECKSUM----------------------\n";
    f+="\n\tChecksum: "+String.valueOf(s);
    f+="\n----------------------------------------------------------\n";
    System.out.print(f);
    return s;
  }
}
