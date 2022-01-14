import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
//Librerias para ver la hora exacta
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Chat {

  static List<String> Conversacion;
  static String nombre;
  static Object lock = new Object();

  static class Worker extends Thread {

    public void run() {
      // En un ciclo infinito se recibirán los mensajes enviados al grupo
      // 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla.
      while (true) try {
        InetAddress grupo = InetAddress.getByName("230.0.0.0");
        MulticastSocket socket = new MulticastSocket(50000);
        byte[] buffer;
        ByteBuffer car;
        socket.joinGroup(grupo);
        int caracteres;
        /*Recibimos el numero de caracteres */
        buffer = recibe_mensaje(socket, 4);
        car = ByteBuffer.wrap(buffer);
        caracteres = car.getInt();        
        /* recibe una string */
        byte[] men = recibe_mensaje(socket, caracteres);        
        AgregarLista(new String(men, "ISO-8859-1"));        
        socket.leaveGroup(grupo);
        socket.close();        
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if(args.length == 0){
      System.out.println("\n Ingrese el siguiente comando: java Chat 'nombre'\n En nombre ingrese el nombre de una persona para iniciar el programa, quite las comillas");
      System.exit(0);
    }            
    Worker w = new Worker();
    Conversacion = new ArrayList<String>();
    w.start();
    nombre = args[0];
    DateFormat hourdateFormat = new SimpleDateFormat("[HH:mm:ss]");
    String hora;
    String enviar;
    Date date;
    Imprimir();
    while (true) {
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));      
      String inputString = input.readLine();
      
      date = new Date();
      hora = hourdateFormat.format(date);
      ByteBuffer caracteres;
      // En un ciclo infinito se leerá los mensajes del teclado y se enviarán
      // al grupo 230.0.0.0 a través del puerto 50000.
      /* envia una string */
      enviar = hora + "<" + nombre + ">: " + inputString;
     /* Enviamos el numero de caracteres de la cadena */      
      caracteres = ByteBuffer.allocate(4);
      caracteres.putInt(enviar.length());
      envia_mensaje(caracteres.array(), "230.0.0.0", 50000);
      /*Enviamos Mensaje*/
      envia_mensaje(enviar.getBytes("ISO-8859-1"), "230.0.0.0", 50000);
    }
  }

  static byte[] recibe_mensaje(MulticastSocket socket, int longitud)
    throws IOException {
    byte[] buffer = new byte[longitud];
    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
    socket.receive(paquete);
    return buffer;
  }

  static void envia_mensaje(byte[] buffer, String ip, int puerto)
    throws IOException {
    DatagramSocket socket = new DatagramSocket();
    InetAddress grupo = InetAddress.getByName(ip);
    DatagramPacket paquete = new DatagramPacket(
      buffer,
      buffer.length,
      grupo,
      puerto
    );
    socket.send(paquete);
    socket.close();
  }

  public static void limpiar(int lineas) {
    for (int i = 0; i < lineas; i++) {
      System.out.println("\n\n\n\n\n\n");
    }
  }

  public static void Imprimir() {
    limpiar(20);
    //Esperamos el mensaje del usuario
    System.out.println("\n\tSALA DE CHAT [" + nombre + "]");
    //System.out.println(Conversacion.isEmpty());
    //System.out.println(Conversacion.size());
    if (!Conversacion.isEmpty()) {
      for (int i = 0; i < Conversacion.size(); i++) {
        System.out.println(Conversacion.get(i));
      }
    }
    System.out.print("->Escribir mensaje: ");
  }

  public static void AgregarLista(String texto) {
    if (texto != "") {
      boolean existe = Conversacion.contains(texto);
      if (!existe) {
        Conversacion.add(texto);
        Imprimir();
      }
    }else{
      Imprimir();
    }
  }
}
