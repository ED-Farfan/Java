import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

class Servidor {

  static int [][] MatA =null;
  static int [][] MatB =null;  
  static int [][] MatC =null;
  static int [][] Mnodos = null;
  static int N =100;
    
  static void read(DataInputStream f, byte[] b, int posicion, int longitud)
    throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception {    
    int nodo=0;
    ServerSocket servidor = new ServerSocket(50000);
    Socket conexion = servidor.accept();
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    System.out.println("\nConecte\n");
    MatA=new int[N][N];
    MatB=new int[N][N];
    MatC=new int[N][N];    
    LlenadoMatriz();
    //Asignacion para partir las matrices 
    int FI = N/2;//Fila Intermedia
    Mnodos=new int[4][6];
    Asignacion(FI,N,Mnodos);
    //Reservamos el espacio para conocer el nodo
    byte[] a = new byte[4];
    read(entrada, a, 0, 4);
    ByteBuffer nodoE = ByteBuffer.wrap(a);
    nodo = nodoE.getInt();
    System.out.println("Se Conecto el nodo "+nodo+"\n");

    // Reservamos el espacio para mandar informacion 
    ByteBuffer b = ByteBuffer.allocate(2*4);
    //Enviamos el numero de Filas
    b.putInt(FI);
    //Enviamos el numero de Columnas
    b.putInt(N);
    byte[] aaa = b.array();
    salida.write(aaa);
    
    //Creamos el espacio para mandar 2 Matrices que es 4 por que un entero utiliza 4 byts, (2(4*(Filas*Columnas)))
    ByteBuffer bb = ByteBuffer.allocate(2*(4*N*FI));
    for(int i =Mnodos[nodo-1][1];i<Mnodos[nodo-1][2];i++){
      for(int j=0; j<N;j++){
        bb.putInt(MatA[i][j]);
        System.out.print(MatA[j][i] + " ");
      }
      System.out.print("\n");
    }
    System.out.print("\n\nB\n");
    for(int i = Mnodos[nodo-1][3];i<Mnodos[nodo-1][4];i++){
      for(int j=0; j<N;j++){
        bb.putInt(MatB[i][j]);
        System.out.print(MatB[j][i] + " ");
      }
      System.out.print("\n");
    }
    byte[] m = bb.array();
    salida.write(m);    

    salida.close();
    entrada.close();
    conexion.close();
  }
  static void LlenadoMatriz(){
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
      {
        MatA[i][j] = 2 * i - j;        
        MatB[j][i] = i + 2 * j;
        MatC[i][j] = 0;
      }
  }
  static void Matriz_T(){
    for (int i = 0; i < N; i++)
    for (int j = 0; j < i; j++)
    {
      int x = MatB[i][j];
      MatB[i][j] = MatB[j][i];
      MatB[j][i] = x;
    }
  }  
  static void Asignacion(int FI,int C,int [][]M){
      for(int i=0;i<4;i++){                           
              M[i][0] = i;
              M[i][1] = (i<2 ? 0 : FI);
              M[i][2] = (i<2 ? FI : C);
              M[i][3] = (i%2==0 ? 0 : FI);
              M[i][4] = (i%2==0 ? FI : C);
              M[i][5] = C ;           
      }
  }
}

