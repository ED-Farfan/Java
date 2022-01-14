import java.rmi.Naming;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteServidorM {

  public static String url = "rmi://localhost/prueba";
  public static String url2 = "rmi://";
  public static long[][] A;
  public static long[][] B;
  public static long[][] BT;
  public static long[][] C;
  public static int N = 500;
  public static long checkSum = 0;
  static Object lock = new Object();
  static InetAddress localhost;
  static class Worker extends Thread {    
    public int Nhilo = -1;    
    public String ext;
    public void run() {       
      if (Nhilo == -1) {        
        try {
          // en este caso el objeto remoto se llama "prueba", notar que se utiliza el puerto default 1099
          // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
          InterfaceRMI r = (InterfaceRMI) Naming.lookup(url+"prueba");
          long[][] A1 = r.parte_matriz(A, 0);
          long[][] A2 = r.parte_matriz(A, N / 2);
          long[][] B1 = r.parte_matriz(BT, 0);
          long[][] B2 = r.parte_matriz(BT, N / 2);
          long[][] C1 = r.multiplica_matrices(A1, B1);
          long[][] C2 = r.multiplica_matrices(A1, B2);
          long[][] C3 = r.multiplica_matrices(A2, B1);
          long[][] C4 = r.multiplica_matrices(A2, B2);
          checkSum += r.checksum(C1);
          checkSum += r.checksum(C2);
          checkSum += r.checksum(C3);
          checkSum += r.checksum(C4);
          acomoda_matriz(C, C1, 0, 0);
          acomoda_matriz(C, C2, 0, N / 2);
          acomoda_matriz(C, C3, N / 2, 0);
          acomoda_matriz(C, C4, N / 2, N / 2);
        } catch (Exception e) {
          System.out.println(e);
        }        
        Reporte();
      } else if (Nhilo == 0) {
        try {
          System.out.println("\n Soy: "+Nhilo+"\n"+"\tConectarme con: "+url+"\n");
          InterfaceRMI r = (InterfaceRMI) Naming.lookup(url);          
          long[][] A1 = r.parte_matriz(A, 0);
          long[][] B1 = r.parte_matriz(BT, 0);
          long[][] C1 = r.multiplica_matrices(A1, B1);
          long sum = r.checksum(C1);
          synchronized (lock) {
            checkSum += sum;
          }
          acomoda_matriz(C, C1, 0, 0);
        } catch (Exception e) {
          System.out.println(e);
        }
      } else if (Nhilo == 1) {
        try {
          System.out.println("\n Soy: "+Nhilo+"\n"+"\t Conectarme con: "+url2+this.ext+"\n");
          InterfaceRMI r = (InterfaceRMI) Naming.lookup(url2+this.ext);          
          long[][] A1 = r.parte_matriz(A, 0);
          long[][] B2 = r.parte_matriz(BT, N / 2);
          long[][] C2 = r.multiplica_matrices(A1, B2);
          long sum = r.checksum(C2);
          synchronized (lock) {
            checkSum += sum;
          }
          acomoda_matriz(C, C2, 0, N / 2);
        } catch (Exception e) {
          System.out.println(e);
        }
      } else if (Nhilo == 2) {
        try { 
          System.out.println("\n Soy: "+Nhilo+"\n"+"\t Conectarme con: "+url2+this.ext+"\n");
          InterfaceRMI r = (InterfaceRMI) Naming.lookup(url2+this.ext);          
          long[][] A2 = r.parte_matriz(A, N / 2);
          long[][] B1 = r.parte_matriz(BT, 0);
          long[][] C3 = r.multiplica_matrices(A2, B1);
          long sum = r.checksum(C3);
          synchronized (lock) {
            checkSum += sum;
          }
          acomoda_matriz(C, C3, N / 2, 0);
          
        } catch (Exception e) {
          System.out.println(e);
        }
        
      } else if (Nhilo == 3) {
        try {
          System.out.println("\n Soy: "+Nhilo+"\n"+"\t Conectarme con: "+url2+this.ext+"\n");
          InterfaceRMI r = (InterfaceRMI) Naming.lookup(url2+this.ext);         
          long[][] A2 = r.parte_matriz(A, N / 2);
          long[][] B2 = r.parte_matriz(BT, N / 2);
          long[][] C4 = r.multiplica_matrices(A2, B2);
          long sum = r.checksum(C4);
          acomoda_matriz(C, C4, N / 2, N / 2);
          synchronized (lock) {
            checkSum += sum;
          }  
        } catch (Exception e) {
          System.out.println(e);
        }      
        
      }
    }

    public void Asignar_Numero_Hilo(int hilo) {
      this.Nhilo = hilo;
    }
    public void Asignar_Nombre(String hilo) {
      this.ext = hilo;
    }
  }


  public static void main(String[] args) throws Exception {
    try {
      localhost = InetAddress.getLocalHost();      
      
    } catch (Exception e) {
      System.out.println("No se tiene ip local");
      System.exit(0);
    }
    if (args.length == 0) { //Es servidor Solo
      ClaseRMI obj = new ClaseRMI();
      System.out.println("Soy servidor con url: "+url+" en: "+localhost);
      
      // registra la instancia en el rmiregistry
      Naming.rebind(url, obj);
    } else {
      
      if (args.length == 1) { // Realizar operaciones en la misma computadora
        int opc = Integer.parseInt(args[0]);
        N = opc;
        Worker w = new Worker();
        ClaseRMI obj = new ClaseRMI();
        // registra la instancia en el rmiregistry
        Naming.rebind(url+"prueba", obj);
        InicializarMatrices();
        Tranpuesta();//Tanspone la matriz B     
        w.start();
        w.join();
        System.exit(0);
      }
      else if(args.length == 2 ) {
        N = Integer.parseInt(args[1]); // Tamaño matriz
        InicializarMatrices();
        Tranpuesta();//Tanspone la matriz B     
        Worker[] w = new Worker[4];
        ClaseRMI obj = new ClaseRMI();        
        String ip1,ip2,ip3;
        
        // registra la instancia en el rmiregistry
        Naming.rebind(url, obj);        
        for (int i = 0; i < 4; i++) w[i] = new Worker();        
        for (int i = 0; i < 4; i++) w[i].Asignar_Numero_Hilo(i);
        System.out.println("\nIngrese el ip del servidor 1\n-> ");
        ip1 = (new Scanner(System.in)).nextLine();        
        System.out.println("\nIngrese el ip del servidor 2\n-> ");
        ip2 = (new Scanner(System.in)).nextLine();        
        System.out.println("\nIngrese el ip del servidor 3\n-> ");
        ip3 = (new Scanner(System.in)).nextLine();        
        System.out.println("\n\n\t\tServidor Inicializado\n");
        System.out.println("\n Soy el servidor: "+url+" en: "+localhost);
        w[0].Asignar_Nombre("/prueba");
        w[1].Asignar_Nombre(ip1+"/prueba");        
        w[2].Asignar_Nombre(ip2+"/prueba");
        w[3].Asignar_Nombre(ip3+"/prueba");        
        for (int i = 0; i < 4; i++) w[i].start();
        for (int i = 0; i < 4; i++) w[i].join();        
        Reporte();
        System.exit(0);
      }
    }
  }

  public static void InicializarMatrices() {
    A = new long[N][N];
    B = new long[N][N];
    C = new long[N][N];
    BT = new long[N][N];
    for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) {
      A[i][j] = 2 * i - j;
      B[i][j] = 2 * i + j;
    }
  }

  public static void Tranpuesta() {
    
    long aux = 0;
    for (int i = 0; i < B.length; i++) for (int j = 0; j < B[0].length; j++) {
      BT[i][j] = B[j][i];
      BT[j][i] = B[i][j];
    }
  }

  static void acomoda_matriz(long[][] C, long[][] A, int renglon, int columna) {
    for (int i = 0; i < N / 2; i++) for (int j = 0; j < N / 2; j++) C[i +
      renglon][j + columna] =
      A[i][j];
  }

  static void ImprimirMatriz(long[][] M) {
    if (N <= 5) {
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          System.out.print("\t" + M[i][j]);
        }
        System.out.print("\n");
      }
    } else {
      System.out.print("\n");
    }
  }

  static void Reporte() {
    for (int i = 0; i < 10; i++) System.out.println("\n\n\n");
    System.out.println("\n\t\t\tReporte\n");
    if (N <= 5) {
      System.out.println("\n\t\t Matriz A\n");
      ImprimirMatriz(A);
      System.out.println("\n\t\t Matriz B\n");
      ImprimirMatriz(B);
      System.out.println("\n\t\t Matriz C\n");
      ImprimirMatriz(C);
    } else {
      System.out.print("\n");
      System.out.print("\n Tamaño de A: " + N + " x " + N);
      System.out.print("\n Tamaño de B: " + N + " x " + N);
      System.out.print("\n Tamaño de C: " + N + " x " + N);
    }
    System.out.println("\nSuma de C : " + checkSum);
  }
}
