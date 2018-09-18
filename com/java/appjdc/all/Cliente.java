package com.java.appjdc.all;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente extends  Thread{
    //VARIABLES DE CLIENTE-SSERVIDOR
   static Socket ClienteM = null;
    Servidor Servidoractivo= null;
    private int idCliente;
    private String ipServidor;
    // si hay servidor es 1, no hay 0
    private int hayServidor=1;
    // si hay conexion ES 1 , SINO 0
    private int hayConexion=1;
    //PARA ENVIAR DATOS AL SERVIDOR
 static  PrintWriter EnviarAlServidor;
  //entrada de datos
  static  DataInputStream RespuestaServidor;
    static  DataInputStream RespuestaIdServidor;
    //POSICION X Y
    static   int numClientes=10;
    private static int[] x=new int[numClientes];
    private static int[] y=new int[numClientes];
    private int[] idsmaster=new int[numClientes];
    private int posClienteX;
    private int posClienteY;
    //NUMERO DE JUGADORES
    private int numJugadores;
    /*CONSTRUCTORES*/
    //CONSTRUCTOR PARA EL  CLIENTE 1->Inicia servidor
    public Cliente(int idCliente,String ipIngresado,int numJugadores)
    {
        this.ipServidor=ipIngresado;
        System.out.println("   IP CON QUE SE TRATA DE CONECTAR EL CLIENTE 1 AL SERVIDOR : "+getIpServidor());
        this.setIdCliente(idCliente);
        this.setNumJugadores(numJugadores);
        for(int k=0;k<numJugadores;k++){
            x[k]=-60;
            y[k]=-60;
        }
    }
    //CONSTRUCTOR PARA 2....N
    public Cliente(String ipServidor)
    {
        setIpServidor(ipServidor);
        System.out.println(" IP CON QUE SE TRATA DE CONECTAR EL CLIENTE 2 AL SERVIDOR : " + getIpServidor());
    }
    //inicio run()
    @Override
    public void run()
    {
        try { //EL CLIENTE 1 INICIA SERVIDOR
            if (getIdCliente() == 1) {
                Servidoractivo = new Servidor(getNumJugadores());
                Servidoractivo.start();
                System.out.println("HAYYYY SERVIDOOOOOOOOOOOOOOORRRRRRRRRR QUE INICIA EL CLIENTE 1 "+getIpServidor());
                setHayServidor(1);
            }
            //SI HAY SERVIDOR
            if (getHayServidor() == 1){
                //CLIENTE 1
                if (getIdCliente() == 1) {
                    ClienteM = new Socket(getIpServidor(), 8080);
                    System.out.println("CON ESTE IP SE CONECTA  EL CLIENTE 1 :"+getIpServidor());
                }
               // CLIENTE 2............N
                else {
                    ClienteM = new Socket(getIpServidor(), 8080);
                    System.out.println("IP CON QUE SE TRATA DE CONECTAR EL CLIENTE 2 AL SERVIDOR : "+getIpServidor());
                    //SI SE LOGRA CONEXION SE RECIBE EL ID ASIGANADO POR EL SERVIDOR
                    RespuestaIdServidor = new DataInputStream(ClienteM.getInputStream());
                    IdRecibidosDesdeServidor(RespuestaIdServidor);
                }
                     System.out.println("cliente : Conectado " + getIdCliente());
            while (true) {
                //sirve para enviar al servidor mensajes
                EnviarAlServidor = new PrintWriter(ClienteM.getOutputStream(), true);
                //envia al servidor
                DatosEnviadosServidor();
                // respuesta de servidor
                //sirve para recibir respuestas del servidor
                RespuestaServidor = new DataInputStream(ClienteM.getInputStream());
                DatosRecibidosDesdeServidor(RespuestaServidor);
            }
        }
                } catch (UnknownHostException e1) {
                System.out.println("el ip servidor no es igual alde host");
                  e1.printStackTrace();
                 }
                catch (IOException e1) {
                    //NO HAY CONEXION 0
                    setHayConexion(0);
                    System.out.println("NO HAY CONEXION");
                  e1.printStackTrace();
                  }

    }

    //CLIENTE RECIBE DE S DATO id
    public void IdRecibidosDesdeServidor(DataInputStream RespuestaServidor) throws IOException {

        String Respuesta = RespuestaServidor.readUTF();
        setIdCliente(Integer.parseInt(Respuesta));
        System.out.println("Id asignado POR EL SERVIDOR"+Respuesta);

    }

//CLIENTE ENVIA X,Y
public void DatosEnviadosServidor(){
            //envia un par coordenado x y
    System.out.println("CLIENTE ENVIA DATOS " + getPosClienteX() + "c" + getPosClienteY() + "*" + getIdCliente());
    EnviarAlServidor.println(getPosClienteX()+"c"+ getPosClienteY()+"*"+ getIdCliente());
}

//CLIENTE RECIBE DE S DATOS X,Y
public void DatosRecibidosDesdeServidor(DataInputStream RespuestaServidor) throws IOException{


    String Respuesta=RespuestaServidor.readUTF();
    System.out.println("RECIBE DATOS DEL SERVIDOR  cliente " + getIdCliente() + "  :" + Respuesta+"\n");
    if(Respuesta.length()<2){}
    else {
        //leyendo numero de jugadores
        setNumJugadores(Integer.parseInt(Respuesta.substring(Respuesta.lastIndexOf('*') + 1, Respuesta.lastIndexOf('p'))));
        //posicion x y
        int[] coordenadaX = new int[getNumJugadores()];
        int[] coordenadaY = new int[getNumJugadores()];
        int[] ids = new int[getNumJugadores()];
        String xc, yc, identificador, fila;
        for (int i = 0; i < getNumJugadores(); i++) {
            //toma la cadena 0 hasta un p
            fila = Respuesta.substring(0, Respuesta.indexOf('p'));
            //mocha la cadena desde p en adelante hasta el final
            Respuesta = Respuesta.substring(Respuesta.indexOf('p') + 1, Respuesta.length());
            xc = fila.substring(0, fila.indexOf('c'));
            yc = fila.substring(fila.indexOf('c') + 1, fila.indexOf('*'));
            identificador = fila.substring(fila.indexOf('*') + 1, fila.length());
            coordenadaX [i] = Integer.parseInt(xc);
            coordenadaY[i] = Integer.parseInt(yc);
            ids[i] = Integer.parseInt(identificador);
        }
        for(int i=0;i<getNumJugadores();i++){
            System.out.println(coordenadaX[i]+"  "+coordenadaY[i]+" "+ids[i]);
        }
        setX(coordenadaX);
        setY(coordenadaY);
        setIdsmaster(ids);
    }
}

        //devuelve arraya
    public static int[] getX() {
        return x;
    }

    public static void setX(int[] x) {
        Cliente.x = x;
    }
    //devuelve arraya
    public static int[] getY() {
        return y;
    }

    public static void setY(int[] y) {
        Cliente.y = y;
    }

    public int getPosClienteX() {
        return posClienteX;
    }

    public void setPosClienteX(int posClienteX) {
        this.posClienteX = posClienteX;
    }

    public int getPosClienteY() {
        return posClienteY;
    }

    public void setPosClienteY(int posClienteY) {
        this.posClienteY = posClienteY;
    }

    public int getHayServidor() {
        return hayServidor;
    }

    public void setHayServidor(int hayServidor) {
        this.hayServidor = hayServidor;
    }
    public int getHayConexion() {
        return hayConexion;
    }

    public void setHayConexion(int hayConexion) {
        this.hayConexion = hayConexion;
    }

    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public void setNumJugadores(int numJugadores) {
        this.numJugadores = numJugadores;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int[] getIdsmaster() {
        return idsmaster;
    }

    public void setIdsmaster(int[] idsmaster) {
        this.idsmaster = idsmaster;
    }
}