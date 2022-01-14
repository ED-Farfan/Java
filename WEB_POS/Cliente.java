import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.Scanner;

class Cliente {

  static String ip = "70.37.95.212";

  static class Usuario {

    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;

    Usuario(
      String email,
      String nombre,
      String apellido_paterno,
      String apellido_materno,
      String fecha_nacimiento,
      String telefono,
      String genero
    ) {
      System.out.print("\nMandan " + email + "\n");
      this.email = email;
      this.nombre = nombre;
      this.apellido_paterno = apellido_paterno;
      this.apellido_materno = apellido_materno;
      this.fecha_nacimiento = fecha_nacimiento;
      this.telefono = telefono;
      this.genero = genero;
      this.foto = null;
    }
  }

  public static void main(String[] args) {
    ip = Preguntar("Ingrese el ip", "ip:");
    menu();
  }

  static void Limpiar() {
    for (int i = 0; i < 10; i++) System.out.println("\n\n\n");
  }

  static String Preguntar(String pregunta, String resp) {
    int a = 1;
    String entradaTeclado = "";
    while (a == 1) {
      //Limpiar();
      System.out.print(pregunta);
      System.out.print("\n\n" + resp + " ");
      Scanner entradaEscaner = new Scanner(System.in); //Creación de un objeto Scanner
      entradaTeclado = entradaEscaner.nextLine(); //Invocamos un método sobre un objeto Scanner
      if (entradaTeclado.length() != 0) {
        a = 0;
      } else {
        try {
          System.out.print("\n¡¡Error no ingreso ningun dato!!\n");
          Thread.sleep(1 * 1000);
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    }
    return entradaTeclado;
  }

  static void menu() {
    int a = 0;
    do {
      Limpiar();
      System.out.print("\nConectandonos a:" + ip + "\n");
      String opc = Preguntar(
        "\n\t\tMENU\n\na. Alta usuario\nb. Consulta usuario\nc. Borra usuario\nd. Borra todos los usuarios\ne. Salir",
        "Opción:"
      );
      if (opc.length() < 1) {
        try {
          System.out.print("\n¡¡Error no ingreso ningun dato!!\n");
          Thread.sleep(1 * 1000);
        } catch (Exception e) {
          System.out.println(e);
        }
      } else if (
        opc.length() > 1 ||
        (
          opc.length() == 1 &&
          !(
            opc.equals("a") ||
            opc.equals("b") ||
            opc.equals("c") ||
            opc.equals("d") ||
            opc.equals("e")
          )
        )
      ) {
        try {
          System.out.print(
            "\n¡¡Error no ingreso '" +
            opc +
            "' no corresponde a las opciones (a,b,c,d,e)!!\n"
          );
          Thread.sleep(1 * 1000);
        } catch (Exception e) {
          System.out.println(e);
        }
      } else {
        if (opc.equals("a")) {
          Alta();
        } else if (opc.equals("b")) {
          Consulta();
        } else if (opc.equals("c")) BorrarUsuario(); 
        else if (opc.equals("d")) Borrar(); 
        else if (
          opc.equals("e")
        ) a++;
      }
    } while (a == 0);
  }

  static void Alta() {
    String opc = "";
    int a = 0;
    while (a == 0) {
      Limpiar();
      String email = Preguntar("\nIngrese el email", "->_");
      String nombre = Preguntar("\nIngrese el nombre", "->_");
      String apellido_paterno = Preguntar(
        "\nIngrese el apellido paterno",
        "->_"
      );
      String apellido_materno = Preguntar(
        "\nIngrese el apellido materno",
        "->_"
      );
      String fecha_nacimiento = Preguntar(
        "\n Ingrese la fecha de nacimiento en el formato 'yyyy-MM-dd' por ejemplo: '2020-01-01'",
        "->_"
      );
      String telefono = Preguntar("\nIngrese el telefono", "->_");
      String genero = Preguntar(
        "\nIngrese el genero (Si es Masculino ingrese 'M', si es Femenino ingrese 'F'",
        "->_"
      );
      byte[] foto = null;
      Limpiar();
      opc =
        Preguntar(
          "\nEmail:" +
          email +
          "\n" +
          "\nNombre:" +
          nombre +
          "\n" +
          "\nApellido paterno:" +
          apellido_paterno +
          "\n" +
          "\nApellido materno:" +
          apellido_materno +
          "\n" +
          "\nFecha de nacimiento:" +
          fecha_nacimiento +
          "\n" +
          "\nTelefono:" +
          telefono +
          "\n" +
          "\nGenero:" +
          genero +
          "\n" +
          "\n¿Los datos son correctos? (si | no)\n",
          "Resp:_"
        );
      if (opc.equals("si")) {
        Usuario nuevo = new Usuario(
          email,
          nombre,
          apellido_paterno,
          apellido_materno,
          fecha_nacimiento,
          telefono,
          genero
        );
        Gson j = new GsonBuilder()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
          .create();

        String s = j.toJson(nuevo);
        System.out.println(s);
        String man = "http://" + ip + ":8080/Servicio/rest/ws/alta";
        try {
          URL url = new URL(man);
          System.out.println("\nMandando a:" + man + "\n");
          HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
          conexion.setDoOutput(true);
          // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
          conexion.setRequestMethod("POST");
          // indica que la petición estará codificada como URL
          conexion.setRequestProperty(
            "Content-Type",
            "application/x-www-form-urlencoded"
          );
          // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
          String parametros = "usuario=" + URLEncoder.encode(s, "UTF-8");
          OutputStream os = conexion.getOutputStream();
          os.write(parametros.getBytes());
          os.flush();
          // se debe verificar si hubo error
          if (
            conexion.getResponseCode() != HttpURLConnection.HTTP_OK
          ) throw new RuntimeException(
            "Codigo de error HTTP: " + conexion.getResponseCode()
          );
          Limpiar();
          System.out.println(
            "\n\nSe realizo correctamente la accion de alta\n"
          );
          Preguntar(
            "\n\nOprima una tecla y posteriormente enter para continuar\n",
            "->"
          );
        } catch (Exception e) {
          Limpiar();
          System.out.print(e);
          System.out.println(
            "\n\nError: 400 -> El correo ya existe en  la base\n"
          );
          Preguntar(
            "\n\nOprima una tecla y posteriormente enter para continuar\n",
            "->"
          );
        }
        a++;
      }
    }
  }

  static void Consulta() {
    Limpiar();
    String email = Preguntar("Ingrese el email a buscar:", "->");
    String man = "http://" + ip + ":8080/Servicio/rest/ws/consulta";
    try {
      URL url = new URL(man);
      System.out.println("\nMandando a:" + man + "\n");
      HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
      conexion.setDoOutput(true);
      // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
      conexion.setRequestMethod("POST");
      // indica que la petición estará codificada como URL
      conexion.setRequestProperty(
        "Content-Type",
        "application/x-www-form-urlencoded"
      );
      // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
      String parametros = "email=" + URLEncoder.encode(email, "UTF-8");
      OutputStream os = conexion.getOutputStream();
      os.write(parametros.getBytes());
      os.flush();
      // se debe verificar si hubo error
      if (
        conexion.getResponseCode() != HttpURLConnection.HTTP_OK
      ) throw new RuntimeException(
        "Codigo de error HTTP: " + conexion.getResponseCode()
      );
      BufferedReader br = new BufferedReader(
        new InputStreamReader((conexion.getInputStream()))
      );
      String respuesta, aux = "";
      // el método web regresa una string en formato JSON
      while ((respuesta = br.readLine()) != null) {
        if (respuesta != null) {
          aux = respuesta;
        }
      }
      conexion.disconnect();
      Gson gson = new Gson();
      Usuario v = gson.fromJson(aux, Usuario.class);
      //Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
      System.out.println(aux);
      //Usuario[] v = (Usuario[])j.fromJson(aux, Usuario[].class);
      Mostrar_Resuldato(v);
    } catch (Exception e) {
      Limpiar();
      System.out.println("\nError:" + e + "\n");
      System.out.println(
        "\n\nError: 400 -> el correo no se encontro en la base\n"
      );
      Preguntar(
        "\n\nOprima una tecla y posteriormente enter para continuar\n",
        "->"
      );
    }
  }

  static void Mostrar_Resuldato(Usuario a) {
    Limpiar();
    System.out.print("\n\tUsuario\n");
    System.out.print("\n Correo: " + a.email);
    System.out.print("\n Nombre: " + a.nombre);
    System.out.print("\n Apellido Paterno: " + a.apellido_paterno);
    System.out.print("\n Apellido Materno: " + a.apellido_materno);
    System.out.print("\n Fecha De Nacimiento: " + a.fecha_nacimiento);
    System.out.print("\n Telefono: " + a.telefono);
    System.out.print(
      "\n Genero: " + ((a.genero).equals("F") ? "Femenino" : "Masculino")
    );
    Preguntar(
      "\n\nOprima una tecla y posteriormente enter para continuar\n",
      "->"
    );
  }

  static void BorrarUsuario() {
    Limpiar();
    String email = Preguntar("Ingrese el email a borrar:", "->");
    String man = "http://" + ip + ":8080/Servicio/rest/ws/borra";
    try {
      URL url = new URL(man);
      System.out.println("\nMandando a:" + man + "\n");
      HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
      conexion.setDoOutput(true);
      // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
      conexion.setRequestMethod("POST");
      // indica que la petición estará codificada como URL
      conexion.setRequestProperty(
        "Content-Type",
        "application/x-www-form-urlencoded"
      );
      // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es "c@com"
      String parametros = "email=" + URLEncoder.encode(email, "UTF-8");
      OutputStream os = conexion.getOutputStream();
      os.write(parametros.getBytes());
      os.flush();
      // se debe verificar si hubo error
      if (
        conexion.getResponseCode() != HttpURLConnection.HTTP_OK
      ) throw new RuntimeException(
        "Codigo de error HTTP: " + conexion.getResponseCode()
      );
      Limpiar();
      System.out.println(
        "\nSe elimino el correo " + email + " de la base correctamente\n"
      );
      Preguntar(
        "\n\nOprima una tecla y posteriormente enter para continuar\n",
        "->"
      );
    } catch (Exception e) {
      Limpiar();
      System.out.println("\nError:" + e + "\n");
      System.out.println(
        "\n\nError: 400 -> el correo no se encontro en la base\n"
      );
      Preguntar(
        "\n\nOprima una tecla y posteriormente enter para continuar\n",
        "->"
      );
    }
  }
  static void Borrar() {
    Limpiar();    
    String man = "http://" + ip + ":8080/Servicio/rest/ws/borrar";
    try {
      URL url = new URL(man);
      System.out.println("\nMandando a:" + man + "\n");
      HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
      conexion.setDoOutput(true);
      // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
      conexion.setRequestMethod("POST");
      // indica que la petición estará codificada como URL
      conexion.setRequestProperty(
        "Content-Type",
        "application/x-www-form-urlencoded"
      );     
     
      OutputStream os = conexion.getOutputStream();      
      os.flush();
      // se debe verificar si hubo error
      if (
        conexion.getResponseCode() != HttpURLConnection.HTTP_OK
      ) throw new RuntimeException(
        "Codigo de error HTTP: " + conexion.getResponseCode()
      );
      Limpiar();
      System.out.println(
        "\nSe elimino todo correctamente \n"
      );
      Preguntar(
        "\n\nOprima una tecla y posteriormente enter para continuar\n",
        "->"
      );
    } catch (Exception e) {
      Limpiar();
      System.out.println("\nError:" + e + "\n");
      System.out.println(
        "\n\nError: 400 -> No tiene elementos la base\n"
      );
      Preguntar(
        "\n\nOprima una tecla y posteriormente enter para continuar\n",
        "->"
      );
    }
  }
}
//javac -cp gson-2.8.6.jar Cliente.java
//java -cp gson-2.8.6.jar:. Cliente
//borrar
