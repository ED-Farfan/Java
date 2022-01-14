import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class Cliente {

  static byte[] recibe_mensaje(MulticastSocket socket, int longitud)
    throws IOException {
    byte[] buffer = new byte[longitud];
    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
    socket.receive(paquete);
    return buffer;
  }

  public static void main(String[] args) {
    try {
      InetAddress grupo = InetAddress.getByName("230.0.0.0");
      MulticastSocket socket = new MulticastSocket(50000);
      byte[] buffer;
      ByteBuffer car;
      socket.joinGroup(grupo);
      int caracteres;

      System.out.println("Esperando datagrama...");
      /*Recibimos el numero de caracteres */
      buffer = recibe_mensaje(socket, 4);
      car = ByteBuffer.wrap(buffer);
      caracteres = car.getInt();      
      /* recibe una string */
      byte[] men = recibe_mensaje(socket, caracteres);
      System.out.println(new String(men, "UTF-8"));
      socket.leaveGroup(grupo);
      socket.close();
    } catch (Exception e) {
      System.out.println(e);
    }

    
  }
}
