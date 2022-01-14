import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
//Librerias para ver la hora exacta
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class MultiplicacionM {
  static Object lock = new Object();
  static int checkC = 0;
  static int FI = 0;
  static int[][] MatA = null;
  static int[][] MatB = null;
  static int[][] MatC = null;
  static int[][] Mnodos = null;
  static int N = 0;
  static int pi = 100;

  static void read(DataInputStream f, byte[] b, int posicion, int longitud)
    throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  static class Worker extends Thread {
    Socket conexion;

    Worker(Socket conexion) {
      this.conexion = conexion;
    }

    //Algoritmo 1
    public void run() {
      try {
        DataOutputStream salida = new DataOutputStream(
          conexion.getOutputStream()
        );
        DataInputStream entrada = new DataInputStream(
          conexion.getInputStream()
        );
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        //Reservamos el espacio para conocer el nodo
        byte[] a = new byte[4];
        read(entrada, a, 0, 4);
        //Recibimos el id del nodo
        ByteBuffer nodoE = ByteBuffer.wrap(a);
        int nodo;
        nodo = nodoE.getInt();
        System.out.println(
          "\n" +
          hourdateFormat.format(date) +
          " Se Conecto el nodo " +
          nodo +
          "\n" + " Le mandaremos de la matriz A["+Mnodos[nodo - 1][1]+" -> "+Mnodos[nodo - 1][2]+"][0 ->" +
          N +"] Y de la matriz BT["+Mnodos[nodo - 1][3]+" -> "+Mnodos[nodo - 1][4]+"]"+
          "\n"
        );
        


        // Reservamos el espacio para mandar informacion
        ByteBuffer b = ByteBuffer.allocate(2 * 4);
        //Enviamos el numero de Filas
        b.putInt(FI);
        //Enviamos el numero de Columnas
        b.putInt(N);
        byte[] aaa = b.array();
        salida.write(aaa);
        //Creamos el espacio para mandar 2 Matrices que es 4 por que un entero utiliza 4 byts, (2(4*(Filas*Columnas)))
        ByteBuffer bb = ByteBuffer.allocate(2 * (4 * N * FI));
        for (int i = Mnodos[nodo - 1][1]; i < Mnodos[nodo - 1][2]; i++) {
          for (int j = 0; j < N; j++) {
            bb.putInt(MatA[i][j]);            
          }
          
        }        
        for (int i = Mnodos[nodo - 1][3]; i < Mnodos[nodo - 1][4]; i++) {
          for (int j = 0; j < N; j++) {
            bb.putInt(MatB[i][j]);          
          }          
        }
        byte[] m = bb.array();
        salida.write(m);
        //Creamos el espacio para recibir una parte de la matriz C. Un entero utiliza 4 byts, ((4*(Filas*Fila)))
        byte[] mc = new byte[4 * (FI * FI)];
        read(entrada, mc, 0, 4 * (FI * FI));
        ByteBuffer bbb = ByteBuffer.wrap(mc);
        for (int i = Mnodos[nodo - 1][1]; i < Mnodos[nodo - 1][2]; i++) {
          for (int j = Mnodos[nodo - 1][3]; j < Mnodos[nodo - 1][4]; j++) {
            MatC[i][j] = bbb.getInt();            
          }          
        }
        //Creamos el espacio para recibir la suma de una parte de la matriz C
        byte[] rsc = new byte[4];
        read(entrada, rsc, 0, 4);
        ByteBuffer rsmc = ByteBuffer.wrap(rsc);
        int psumac = rsmc.getInt();
        synchronized (lock) {
          checkC += psumac;
        }
        Date date2 = new Date();
        System.out.println(
          "\n" +
          hourdateFormat.format(date2) +
          " Termino el nodo " +
          nodo +
          "\n"+ "El nodo: " +
          nodo + " Mando de la matriz C["+Mnodos[nodo - 1][1]+" -> "+Mnodos[nodo - 1][2]+"]["+
          Mnodos[nodo - 1][3]+" -> "+Mnodos[nodo - 1][4] +"]"+
          "\n"
        );        
        //
        salida.close();
        entrada.close();
        conexion.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("Uso:");
      System.err.println("java MultiplicacionM <nodo>");
      System.err.println("java MultiplicacionM 0 TamMatrices");
      System.exit(0);
    }
    int nodo = Integer.valueOf(args[0]);
    if (args.length == 2 && nodo == 0) { //Es el servidor
      N = Integer.valueOf(args[1]);
      if ((N * N) % 4 != 0) {
        System.err.println("Uso:");
        System.err.println("java MultiplicacionM <nodo>");
        System.err.println("java MultiplicacionM 0 TamMatrices");
        System.err.println("TamMatrices = N donde (N*N)%4 == 0");
        System.err.println("ERROR:");
        System.err.println(
          "java MultiplicacionM 0 " +
          N +
          " <-\n" +
          (N*N) +
          "%4 = " +
          (N*N) %
          4 +
          " != 0"
        );
        System.exit(0);
      }
      long TInicio, TFin, tiempo;
      TInicio = System.currentTimeMillis();
      System.out.println("\n\n\t\tServidor");
      ServerSocket servidor = new ServerSocket(50000);
      //Asignamos en memoria el espacio requerido para las matrices
      MatA = new int[N][N];
      MatB = new int[N][N];
      MatC = new int[N][N];
      LlenadoMatriz();
      //Asignacion para partir las matrices
      FI = N / 2; //Fila Intermedia
      Mnodos = new int[4][6];
      //Creamos una matriz con las asignaciones requeridas para cada nodo
      Asignacion(FI, N, Mnodos);
      Worker w[];
      w = new Worker[4];
      double suma = 0.0;
      System.out.println("\n\tReporte de conexiones:\n");
      for (int i = 0; i < 4; i++) {
        Date date = new Date();
        Socket conexion = servidor.accept();
        w[i] = new Worker(conexion);
        w[i].start();
      }
      for (int i = 0; i < 4; i++) {
        w[i].join();
      }
      System.out.println("\n\n\tReporte de resultados");
      System.out.println("\n-> El valor del CheckC:" + checkC);
      TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
      tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
      System.out.println("\nTiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos
      System.out.println("\n\t Matriz C");
      Mostrar(N, N, MatC);
      System.out.println("\n");
      System.exit(0);
    } else if (nodo >= 1 && nodo <= 4) {
      // Algoritmo 3      
      DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

      Socket conexion = null;
      System.out.println("\n\tSoy el nodo: " + nodo);
      System.out.println("\nEsperando conexion");
      //Funcion para dar formato, este formato sera para ver la hora que fue atendido
      DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      for (;;) try {
        conexion = new Socket("localhost", 50000);
        break;
      } catch (Exception e) {
        Thread.sleep(100);
      }
      long TInicio, TFin, tiempo;
      TInicio = System.currentTimeMillis();
      Date date = new Date();
      System.out.println("\n" + hourdateFormat.format(date) + " Atendido: \n");
      DataOutputStream salida = new DataOutputStream(
        conexion.getOutputStream()
      );
      DataInputStream entrada = new DataInputStream(conexion.getInputStream());
      //M[Renglon o Fila][Columna]
      int[][] MatA;
      int[][] MatB;
      int[][] MatC;
      //Inicializamos las variables que almacenaran la informacion del numero de Columnas y Filas
      int Columnas = 0;
      int Filas = 0;
      //Inicializamos una variable para almacenar la suma de el contenido en c
      int sumC = 0;
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
      byte[] aa = new byte[4 * (Filas * Columnas) * 2];
      read(entrada, aa, 0, 4 * 2 * (Filas * Columnas));
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
      System.out.println("\n\tMatriz A ");
      Mostrar(Filas, Columnas, MatA);
      System.out.println("\n\tMatriz B ");
      Mostrar(Filas, Columnas, MatB);
      System.out.println("\n\tMatriz C ");
      Mostrar(Filas, Filas, MatC);

      sumC = Multiplicar(Filas, Columnas, MatA, MatB, MatC);
      System.out.println("\nDespues de la multiplicacion ");
      System.out.println("\n\n\tMatriz A ");
      Mostrar(Filas, Columnas, MatA);
      System.out.println("\n\tMatriz B ");
      Mostrar(Filas, Columnas, MatB);
      System.out.println("\n\tMatriz C ");
      Mostrar(Filas, Filas, MatC);
      System.out.print("\nLa Suma De C es:" + sumC + "\n\n");
      //Creamos el espacio para mandar la matriz C, 4*Filas*Filas
      ByteBuffer mc = ByteBuffer.allocate(4 * (Filas * Filas));
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
      System.out.println(
        "\n" + hourdateFormat.format(date2) + " Termino Ejecucion \n"
      );

      TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
      tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
      System.out.println("Tiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos
      System.exit(0);
    } else {
      System.err.println("Uso:");
      System.err.println("java MultiplicacionM <nodo>");
      System.err.println("0 <= <nodo> <= 4");
      System.exit(0);
    }
  }

  static void LlenadoMatriz() {
    for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) {
      MatA[i][j] = 2 * i + j;
      MatB[j][i] = 2 * i - j;
      MatC[i][j] = 0;
    }
  }

  static void Matriz_T() {
    for (int i = 0; i < N; i++) for (int j = 0; j < i; j++) {
      int x = MatB[i][j];
      MatB[i][j] = MatB[j][i];
      MatB[j][i] = x;
    }
  }

  static void Asignacion(int FI, int C, int[][] M) {
    for (int i = 0; i < 4; i++) {
      M[i][0] = i;
      M[i][1] = (i < 2 ? 0 : FI);
      M[i][2] = (i < 2 ? FI : C);
      M[i][3] = (i % 2 == 0 ? 0 : FI);
      M[i][4] = (i % 2 == 0 ? FI : C);
      M[i][5] = C;
    }
  }

  static void Mostrar(int F, int C, int[][] Matriz) {    
    if(C< 16){
      System.out.print("\n");
      for (int j = 0; j < F; j++) {
        for (int i = 0; i < C; i++) {
          System.out.print("|"+Matriz[j][i] +"|\t");
        }
        System.out.print("\n");
      }
    }else{
      System.out.print("\n Las Dimensiones de la matriz Filas:"+F+" Columas: "+C+" Es Decir "+F+" X "+C);
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
