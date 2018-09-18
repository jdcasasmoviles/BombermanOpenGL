package com.java.appjdc.all;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.InetAddress;

public class Servidor extends Thread   {
 static   int numClientes=10;
static ServerSocket ServidorM = null;
static Socket[] clientes=new Socket[numClientes];
static BufferedReader[] entrada=new BufferedReader[numClientes];
static DataOutputStream[] salida=new DataOutputStream[numClientes];
    static DataOutputStream[] salidaId=new DataOutputStream[numClientes];
//VARIABLES DEL JUEGO;
    private static int[] x=new int[numClientes];
    private static int[] y=new int[numClientes];
    //NUMERO DE JUGADORES
    private int numJugadores;
//IP SERVIDOR
InetAddress address = InetAddress.getLocalHost();
    private String ipServidorIniciado;
//constructor
public Servidor(int numJugadores) throws IOException{
ServidorM = new ServerSocket(8080);
    //setIpServidorIniciado(ipSeervidor());
    this.numJugadores = numJugadores;
    System.out.println("servidor : Socket escuchando en puerto 8080");
}
    @Override
    public void run()
    {
    try {

        //se acepta los clientes y se envia su id
        for(int i=0;i<numJugadores;i++){
          clientes[i] = ServidorM.accept(); System.out.println("servidor : Se conecto un cliente "+ (i+1));
            salidaId[i] = new DataOutputStream(clientes[i].getOutputStream());
            EnviaIdsAlCliente(i+1);
        }
        while(true){
       //recibe datos del cliente
            for(int i=0;i<numJugadores;i++){
                //cosas que envia el cliente al servidor
                entrada[i] = new BufferedReader(new InputStreamReader(clientes[i].getInputStream()));
            }
	    RecibeDatodCliente();
        //envia datos al cliente
            for(int i=0;i<numJugadores;i++){
                //cosas que envia el servidor hacia el cliente
                salida[i] = new DataOutputStream(clientes[i].getOutputStream());
            }
        EnviaDatosAlCliente();          
        }
    } 
    catch (IOException ex) {
        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
    }    
    }

    public void EnviaIdsAlCliente(int id) throws IOException{
        salidaId[id-1].writeUTF("" + id);
        System.out.println("ENVIA ID : " + id);
    }

        //RECIBE  DATOS DEL CLIENTE :CLIENTE->SERVIDOR
    public void RecibeDatodCliente() throws IOException{
         //recibe datos del cliente
        for(int i=0;i<numJugadores;i++) {
            String recibe ="";
            recibe=entrada[i].readLine();
            System.out.println("SERVIDOR RECIBE DATOS DEL CLIENTE  " + (i+1) + "  XXXX: \n" + recibe);
            //posicion x y idcliente
            //pos x
            x[i] = Integer.parseInt(recibe.substring(0, recibe.lastIndexOf("c")));
            //pos y
            String ss = recibe.substring(recibe.lastIndexOf("c") + 1, recibe.length());
            y[i] = Integer.parseInt(ss.substring(0, ss.lastIndexOf("*")));
            //id
            int id=Integer.parseInt(recibe.substring(recibe.lastIndexOf("*")+1));
        }

        for(int i=0;i<numJugadores;i++) {
            System.out.println("SERVIDOR RECIBE DATOS DEL CLIENTE LLL"+(i+1)+"  : \n "+x[i]+"  "+y[i]);
        }

    }


    //s-->c
    public void EnviaDatosAlCliente() throws IOException{
     
      for(int i=0;i<numJugadores;i++){
          //posiciones de todos los jugadores
          String  envio="";
          for(int j=0;j<getNumJugadores();j++){
              envio=envio+x[j]+"c"+y[j]+"*"+(j+1)+"p";
          }
        salida[i].writeUTF(envio);
          System.out.println("ENVIA DATOS AL CLIENTE  "+(i+1) +" : \n "+envio);
        }

    }

    public static int[] getX() {
        return x;
    }

    public static void setX(int[] x) {
        Servidor.x = x;
    }

    public static int[] getY() {
        return y;
    }

    public static void setY(int[] y) {
        Servidor.y = y;
    }

    public String getIpServidorIniciado() {
        return ipServidorIniciado;
    }

    public void setIpServidorIniciado(String ipServidorIniciado) {
        this.ipServidorIniciado = ipServidorIniciado;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public void setNumJugadores(int numJugadores) {
        this.numJugadores = numJugadores;
    }
}