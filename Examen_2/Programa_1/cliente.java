import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    Socket conexion = new Socket("sisdis.sytes.net",10000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // 3 como entero de 32 bits
    salida.writeInt(3);
    // 10 como entero de 32 bits (int) 
    salida.writeInt(10);
    // 50 como entero de 32 bits (int)
    salida.writeInt(50);

    //recibir del servidor un número entero
    int n = entrada.readInt();
    System.out.print("\n");
    System.out.print(n);
    System.out.print("\n");

    salida.close();
    entrada.close();
    conexion.close();    
  }
}