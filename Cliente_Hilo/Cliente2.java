import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.Thread;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class Cliente2
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
    Socket conexion = null;
    //Funcion para dar formato, este formato sera para ver la hora que fue atendido
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    for(;;)
      try
      {
    	  conexion = new Socket("localhost",50000);
          break;
      }
      catch (Exception e)
      {
        System.out.println("\nEsperando\n");
        Thread.sleep(100);
      }
      Date date = new Date();        
      System.out.println("\nAtendido: \n");
      System.out.println("Hora actual: " + dateFormat.format(date));
    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // envia un entero de 32 bits
    salida.writeInt(123);

    // envia un numero punto flotante
    salida.writeDouble(1234567890.1234567890);

    // envia una cadena
    salida.write("hola".getBytes());

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    // envia 5 numeros punto flotante
    ByteBuffer b = ByteBuffer.allocate(5*8);
    b.putDouble(1.1);
    b.putDouble(1.2);
    b.putDouble(1.3);
    b.putDouble(1.4);
    b.putDouble(1.5);
    byte[] a = b.array();
    salida.write(a);

    salida.close();
    entrada.close();
    conexion.close();    
  }
}