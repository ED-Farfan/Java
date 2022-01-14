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

class PI {
  static Object lock = new Object();
  static double pi = 0;
  

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
        // recibe un numero punto flotante
        double x = entrada.readDouble();
        System.out.println(x);
        synchronized (lock) {
          pi += x;
        }
        salida.close();
        entrada.close();
        conexion.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Uso:");
      System.err.println("java PI <nodo>");
      System.exit(0);
    }
    int nodo = Integer.valueOf(args[0]);
    if (nodo == 0) {
        long TInicio, TFin, tiempo;
        TInicio = System.currentTimeMillis();
        System.out.println("\nServidor");
        ServerSocket servidor = new ServerSocket(50000);
        Worker w [];    
        w = new Worker[3];
        double suma = 0.0;
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        for (int i = 0;i<3;i++)
        {
            Date date = new Date();
            Socket conexion = servidor.accept();
            System.out.println("\n Se conecto el nodo "+(i+1));
            System.out.println("\n"+hourdateFormat.format(date)+"\n");
            w[i] = new Worker(conexion);
            w[i].start();
        }
        for(int i = 0; i<10000000; i++)
        {
            suma += 4.0/(8*i+1);
        }
        System.out.println("\nSuma resultante de servidor: "+suma);
        synchronized (lock) {
            pi += suma;
        }
        for(int i=0;i<3;i++){
            w[i].join();
        }
        Date date = new Date();
        System.out.println("\n"+hourdateFormat.format(date)+"\n");
        System.out.println("El Valor de Pi es:"+pi);
        TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
        tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
        System.out.println("Tiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos
    } else {
      // Algoritmo 3

        Socket conexion = null;            
        double suma = 0.0;
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        System.out.println("Soy el nodo: "+nodo);
        System.out.println("\nEsperando conexion");
        for(;;)
        try
        {
            conexion = new Socket("localhost",50000);
            break;
        }
        catch (Exception e)
        {        
            Thread.sleep(100);
        }
        long TInicio, TFin, tiempo;
        TInicio = System.currentTimeMillis();
        System.out.println("\nConexion establecida");
        Date date = new Date();
        System.out.println("\n"+hourdateFormat.format(date)+"\n");
        for(int i = 0; i< 10000000; i++){
            suma += 4.0/(8*i+2*(nodo-1)+3);            
        }
        suma = nodo%2 == 0 ? suma : -suma;
        System.out.println("\nResultado de la suma: "+suma);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        System.out.println("\nMandando Resultado");
        date = new Date();
        System.out.println("\n"+hourdateFormat.format(date)+"\n");
        ByteBuffer b = ByteBuffer.allocate(1*8);
        b.putDouble(suma);        
        byte[] a = b.array();
        salida.write(a);
        salida.close();
        entrada.close();
        conexion.close();
        System.out.println("\nFin de programa");
        date = new Date();
        System.out.println("\n"+hourdateFormat.format(date)+"\n");
        TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
        tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
        System.out.println("Tiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos
        }
  }
  
}
