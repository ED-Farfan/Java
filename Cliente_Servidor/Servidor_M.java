import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

class Servidor {

  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f, byte[] b, int posicion, int longitud)
    throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception {
    ServerSocket servidor = new ServerSocket(50000);

    Socket conexion = servidor.accept();

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    long inicio = System.currentTimeMillis();

    double[] numeros_Cliente = new double[1000];
    // recibe un numero punto flotante
    for (int i = 0; i < 1000; i++) {
      numeros_Cliente[i] = entrada.readDouble();
    }
    long fin = System.currentTimeMillis();
    String aaa = String.valueOf(fin - inicio);
    System.out.println("Tiempo de Recibido con readDouble:");
    System.out.println(aaa + "ms");

    System.out.println("------------------");

    // recibe 5 numeros punto flotante
    double[] numeros_Cliente2 = new double[1000];
    inicio = System.currentTimeMillis();
    byte[] a = new byte[1000 * 8];
    read(entrada, a, 0, 1000 * 8);
    ByteBuffer b = ByteBuffer.wrap(a);
    for (int i = 0; i < 1000; i++) numeros_Cliente2[i] = b.getDouble();
    fin = System.currentTimeMillis();
    System.out.println("Tiempo de Recibido con ByteBuffer:");
    System.out.println(aaa + "ms");

    salida.close();
    entrada.close();
    conexion.close();
  }
}
