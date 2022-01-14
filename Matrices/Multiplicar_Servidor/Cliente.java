import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.Thread;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


class Cliente2 {  

  static void read(DataInputStream f, byte[] b, int posicion, int longitud)
    throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception {
    int nodo=4;
    DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    Socket conexion = null;
    System.out.println("Soy el nodo: " + nodo);
    System.out.println("\nEsperando conexion");
    //Funcion para dar formato, este formato sera para ver la hora que fue atendido
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    for (;;) try {
      conexion = new Socket("localhost", 50000);
      break;
    } catch (Exception e) {      
      Thread.sleep(100);
    }    
    Date date = new Date();    
    System.out.println("\n"+hourdateFormat.format(date) +" Atendido: \n");
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    //M[Renglon o Fila][Columna]    
    int[][] MatA;
    int[][] MatB;
    int[][] MatC;
    //Inicializamos las variables que almacenaran la informacion del numero de Columnas y Filas
    int Columnas = 0;
    int Filas = 0;
    //Inicializamos una variable para almacenar la suma de el contenido en c
    int sumC=0;
    // Reservamos el espacio para mandar informacion 
    ByteBuffer nodob = ByteBuffer.allocate(4);
    //Enviamos el numero de columnas    
    nodob.putInt(nodo);    
    byte[] an = nodob.array();
    salida.write(an);
    //Leemos el numero de Columnas y Filas
    byte[] a = new byte[2 * 4];
    read(entrada, a, 0, 2 * 4);
    ByteBuffer b = ByteBuffer.wrap(a);
    Filas = b.getInt();
    Columnas = b.getInt();
    //Inicializamos a las matrices
    MatA = new int[Filas][Columnas];
    MatB = new int[Filas][Columnas];
    MatC = new int[Filas][Filas];
    System.out.println("Filas: " + Filas + " Columnas: " + Columnas);
    // Un entero es de 4 bits, por lo que tenemos que reservar el espacio para 3 matrices de nFilas*nColumnas
    byte[] aa = new byte[4 *(Filas * Columnas)*2];
    read(entrada, aa,0, 4 * 2*(Filas * Columnas));
    ByteBuffer bb = ByteBuffer.wrap(aa);
    //Llenamos cada una de las Matrices A y B
    for (int i = 0; i < Filas; i++) {
      for (int j = 0; j < Columnas; j++) {
        MatA[i][j] = bb.getInt();
      }
    }
    for (int i = 0; i < Filas; i++) {
      for (int j = 0; j < Columnas; j++) {
        MatB[i][j] = bb.getInt();
      }    
    }    
    System.out.println("\nMatriz A ");
    Mostrar(Filas,Columnas, MatA);
    System.out.println("\nMatriz B ");
    Mostrar(Filas,Columnas, MatB);
    System.out.println("\nMatriz C ");
    Mostrar(Filas,Filas, MatC);

    sumC=Multiplicar(Filas, Columnas,MatA,MatB,MatC);
    System.out.println("\nMatriz A ");
    Mostrar(Filas,Columnas, MatA);
    System.out.println("\nMatriz B ");
    Mostrar(Filas,Columnas, MatB);
    System.out.println("\nMatriz C ");
    Mostrar(Filas,Filas, MatC);
    System.out.print("\nLa Suma De C es:"+sumC+"\n\n");
    //Creamos el espacio para mandar la matriz C, 4*Filas*Filas
    ByteBuffer mc = ByteBuffer.allocate(4*(Filas*Filas));
    for (int i = 0; i < Filas; i++) {
      for (int j = 0; j < Filas; j++) {
          mc.putInt(MatC[i][j]);
      }
    }
    byte[] mcc = mc.array();    
    salida.write(mcc);
    //Creamos el espacio para mandar el resultado de la suma de la matriz c
    ByteBuffer rsc = ByteBuffer.allocate(4);
    rsc.putInt(sumC);
    byte[] sc = rsc.array();    
    salida.write(sc);
    salida.close();
    entrada.close();
    conexion.close();
    Date date2 = new Date();    
    System.out.println("\n"+hourdateFormat.format(date2) +" Termino Ejecucion \n");
  }

  static void Mostrar(int F,int C, int [][]Matriz){
    for (int j = 0; j < F; j++) {
      for (int i = 0; i < C; i++) {
        System.out.print(Matriz[j][i] + " ");
      }
      System.out.print("\n");
    }
  }

  static int Multiplicar(int F, int C,int [][]A,int [][]B,int [][]MC) {    
    int sumC=0;
    for (int i = 0; i < F; i++) 
        for (int j = 0; j < F; j++) 
            {for (int k = 0;k < C;k++ ){ 
              //System.out.println("C[" + i + "] ["+j+"] A["+i+"]["+k+"] B["+j+"]["+k+"]");
              MC[i][j] += A[i][k] * B[j][k];              
            }
            sumC+=MC[i][j];}
    return sumC;
  }
}
