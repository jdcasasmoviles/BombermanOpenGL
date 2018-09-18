package com.java.appjdc.all;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainJDCn extends ApplicationAdapter {
    private String ip;
    private int idCliente;
    //TEXTURAS
    private  SpriteBatch batch;
    private  Texture j1,j2,j3,j4,cpuM,uni,ladrillo,over,inicio,cargando,salida;
    private Texture ganaste,winner,ganador,reset;
    private Texture arriba,abajo,izquierda,derecha,botonA;
    //VARIABLES DE CONTROL DE GDX
    int cuadrillax=21,cuadrillay=11;
    private  int espacioX,espacioY;
    int intervalo=100;
    int contador=0;
    int numReset=0;
    int numCpus=5;
    //VARIABLES DEL JUEGO
    int tablero[][]=new int[cuadrillay+5][cuadrillax+5];
    mostro[] cpus=new mostro[numCpus];
    private Procesador menu=new Procesador();
    Jugador player;
    Tablero escenario;
    private int bombaGanadora=0;
    //VARIABLES DEL SERVIDOR-CLIENTE --->En este caso no es cliente 1,
    //SINO CLIENTE 2..........N
    int numClientes=2;
    Cliente unico;
    /*CONSTRUCTOR*/
    public MainJDCn( String ip){
        setIp(ip);
        System.out.println("YYYYY    ipServido CLIENTEr 2 :  " + getIp());
    }
    @Override
    public void create () {
         unico=new Cliente(getIp());
        //CARGA IMAGENES
        ladrillo=new Texture("ladrillo.png");
        reset=new Texture("reset.png");
        over=new Texture("over.png");
        cpuM=new Texture("cpu.png");
        uni=new Texture("uni.png");
        inicio=new Texture("inicio.png");
        cargando=new Texture("cargando.png");
        winner=new Texture("winner.png");
        ganaste=new Texture("ganaste.png");
        salida=new Texture("salida.png");
        //IMAGENES PARA JUGADORES
        j1=new Texture("j1.png");
        j2=new Texture("j2.png");
        j3=new Texture("j3.png");
        j4=new Texture("j4.png");
        //IMAGENES PARA EL MANDO
        derecha=new Texture("derecha.png");
        abajo=new Texture("abajo.png");
        izquierda=new Texture("izquierda.png");
        arriba=new Texture("arriba.png");
        botonA=new Texture("botonA.png");
        //GUARDA IMAGENES DE ESPACIOS QUE USA EL ARRAY TABLERO
        espacioX=Gdx.graphics.getWidth() /cuadrillax;
        espacioY=Gdx.graphics.getHeight() /cuadrillay;
        //ESTO ES PARA DIBUJAR
        batch= new SpriteBatch();
    }

    @Override
    public void render () {
        super.render();
        Gdx.gl.glClearColor(46, 100, 54, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(menu);
        //////////////////////COMIENZA EL DIBUJO DEL JUEGO BOMBERMAN
        getBatch().begin();
        //PANTALLA DE INICIO
        if(contador<=intervalo)pantallaInicio();
        //PANTALLA CARGANDO
        if(intervalo<contador && contador<2*intervalo )  pantallaCargando();
        //Inicia hilo de jugadores y cpus , hilo cliente
        if(contador==2*intervalo && numReset==0)  inicializaJugadorMostroCliente();
        //DIBUJA ESCENARIO  DE JUEGO CON JUGADORES ,MOSTROS,BOMBAS,ESPLOSIONES
        if(contador>2*intervalo){
            //POSICION QUE SE ENVIA-CLIENTE-SERVIDOR, SI ES QUE HAY CONEXION
            if(unico.getHayServidor()!=0 && unico.getHayConexion()!=0){
                unico.setPosClienteX(player.getX());
                unico.setPosClienteY(player.getY());
            }
            //POSICION QUE RECIBE EL CLIENTE DEL SERVIDOR
            if(unico.getHayServidor()!=0 && unico.getHayConexion()!=0){
                //SI HAY MAS CLIENTES que lo dibuje en escenario
                for(int i=0;i<unico.getNumJugadores();i++){
                   // if((i+1)!=unico.getIdCliente() )
                    dibujaJugadorPlayer(i);
                }
                setIdCliente(unico.getIdCliente());
                for(int i=0;i<unico.getNumJugadores();i++){
                    System.out.println(unico.getX()[i]+"  "+unico.getY()[i]);
                }

            }
            //SE DIBUJA PANTALLA ESCENARIO DEL JUEGO
            pantallaEscenarioJuego();
            //CONDICIONES PARA GANAR O PERDER EL JUEGO
            condicionesJuego();
        }
        getBatch().end();
        contador++;
    }

    @Override
    public void dispose() {
        ganaste.dispose();
        ladrillo.dispose();
        over.dispose();
        cpuM.dispose();
        uni.dispose();
        inicio.dispose();
        cargando.dispose();
        salida.dispose();
        winner.dispose();
        arriba.dispose();
        abajo.dispose();
        derecha.dispose();
        izquierda.dispose();
        reset.dispose();
        botonA.dispose();
        j1.dispose();
        j2.dispose();
        j3.dispose();
        j4.dispose();
        getBatch().dispose();
    }
    ///////CONDICIONES QUE SE DA PARA QUE SE GALE O PIERDA EL JUEGO
    public void condicionesJuego(){
        condicionGanador(player.getX(), player.getY());
        if(bombaGanadora==1)   pantallaGanador();
            //si game over es true
        else if(player.getVida()==0)  pantallaPierde();
    }
    public void condicionGanador(int x ,int y){
        int a,b,c,d,i=11,j=6;
        a=i*espacioX; b=(i+1)*espacioX; c=j*espacioY; d=(j+1)*espacioY;
        if(   a<=x && x<=b && c<=y && y<=d  )   setBombaGanadora(1);
    }

    //INICIALIZA JUGADOR,MOSTROS Y CLIENTE
    public void inicializaJugadorMostroCliente(){
        //CUANDO JUEGA POR PRIMERA VEZ
        if(numReset==0){
            //INSTANCIA JUGADORES  EN POSICION INICIAL 0,240
            int xaleatorio=(int)(Math.random()*21);;
            player=new Jugador(xaleatorio*espacioX,10*espacioY,tablero,menu);
            //PARA INSTANCIAR TABLERO
            escenario=new Tablero(tablero,batch,player);
            //INSTANCIA MOSTROS
            for(int i=0;i<numCpus ;i++)
            {
                cpus[i]=new mostro(8*espacioX,4*espacioY,tablero);
            }
            //INICIA HILOS DE MOSTROS ,JUGADORES,CLIENTE
            //INICIA JUGADOR
            player.start();
            //INICIA MOSTROS
            for(int i=0;i<numCpus ;i++)
            {  cpus[i].start();
            }
            // INICIA CLIENTE
            unico.start();
        }
        //CUANDO LO RESETEA
        else{
            System.out.println("seie varoables");
            //SETEA VARIABLES DEL JUEGO
            contador=0;
            setBombaGanadora(0);
            player.setVida(1);
            int xaleatorio=(int)(Math.random()*21);
            player.setX(xaleatorio*espacioX);player.setY(10*espacioY);
            for(int i=0;i<numCpus ;i++)
            {
                cpus[i].setX(8*espacioX);cpus[i].setY(4*espacioY);
            }
        }
        escenario.iniciarEscenarioJuego(tablero);
    }

    ///////////PANTALLAS DE DIBUJO DEL JUEGO////////////////////////////////////////////////////////////////////
    public void dibujaJugadorPlayer(int i){
        if(i+1==1)  getBatch().draw(j1, unico.getX()[i], unico.getY()[i], espacioX, espacioY);
        else if(i+1==2)  getBatch().draw(j2, unico.getX()[i], unico.getY()[i], espacioX, espacioY);
        else if(i+1==3)  getBatch().draw(j3, unico.getX()[i], unico.getY()[i], espacioX, espacioY);
        else if(i+1==4)  getBatch().draw(j4, unico.getX()[i], unico.getY()[i], espacioX, espacioY);
        else  getBatch().draw(j2, unico.getX()[i], unico.getY()[i], espacioX, espacioY);
    }

    public void dibujaJugador(){
        if(getIdCliente()==1)  getBatch().draw(j1,player.getX(),player.getY(), espacioX, espacioY);
        else if(getIdCliente()==2)  getBatch().draw(j2,player.getX(),player.getY(), espacioX, espacioY);
        else if(getIdCliente()==3)  getBatch().draw(j3,player.getX(),player.getY(), espacioX, espacioY);
        else if(getIdCliente()==4)  getBatch().draw(j4,player.getX(),player.getY(), espacioX, espacioY);
        else  getBatch().draw(j1,player.getX(),player.getY(), espacioX, espacioY);
    }

    public void pantallaEscenarioJuego() {
        //dibuja escenario de juego
        dibujaTablero(espacioX, espacioY);
        escenario.dibujarBombas(tablero) ;
        /////////////// escenario. dibujarExplosiones(trampas);
        dibujaJugador();
        //dibuja salida
        getBatch().draw(salida, 11 * espacioX, 6 * espacioY, espacioX, espacioY);
        //dibuja cpus
        for(int i=0;i<numCpus ;i++)
        {
            getBatch().draw(cpuM, cpus[i].getX(), cpus[i].getY(), espacioX, espacioY);
        }
        //izquierda
        getBatch().draw(izquierda, 0,2*espacioY, 2*espacioX, 2*espacioY);
        //arriba
        getBatch().draw(arriba, 2*espacioX,4*espacioY, 2*espacioX, 2*espacioY);
        //derecha
        getBatch().draw(derecha, 4*espacioX,2*espacioY, 2*espacioX, 2*espacioY);
        //abajo
        getBatch().draw(abajo, 2*espacioX,0, 2*espacioX, 2*espacioY);
        //boton A
        getBatch().draw(botonA, 15*espacioX,1*espacioY, 4*espacioX, 4*espacioY);
    }

    ///PANTALLA DE DIBUJO PARA EL GANADOR DEL JUEGO
    public void pantallaGanador(){
        player.setVida(0);
        getBatch().draw(winner, 5 * espacioX, 5 * espacioY, 12 * espacioX, 6 * espacioY);
        getBatch().draw(ganaste, 5 * espacioX, 3 * espacioY, 12 * espacioX, 6 * espacioY);
        getBatch().draw(reset, 9* espacioX, 1* espacioY, 4* espacioX, 4* espacioY);
        reset();
    }

    ///PANTALLA DE DIBUJO PARA EL PERDEDOR DEL JUEGO
    public void pantallaPierde(){
        getBatch().draw(over, 5 * espacioX, 4 * espacioY, 12 * espacioX, 6 * espacioY);
        getBatch().draw(reset, 9* espacioX, 1* espacioY, 4* espacioX, 4* espacioY);
        reset();
    }
    // PANTALLA DE CARGANDO EL JUEGO
    public void pantallaCargando(){
        getBatch().draw(cargando, 0, 0, 22 * espacioX, 12 * espacioY);
    }
    //PANTALLA DE INICIO DEL JUEGO
    public void pantallaInicio(){
        getBatch().draw(inicio, 0, 0, 22 * espacioX, 12 * espacioY);
    }


    /////////////BOTON RESET QUE SE MUESTRA CUANDO GANA O PIERDE//////////////////////////////////////////////
    //BOTON RESET
    public void reset(){
        //SI PRESIONA BOTON RESET
        if(sipresionoRseset(menu.getPosicionX(),menu.getPosicionY(),9,1)) {
            numReset++;
            inicializaJugadorMostroCliente();
        }
    }

    //SI SE PRESIONA EL BOTON RESET
    public boolean sipresionoRseset(int x ,int y,int i,int j){
        int a,b,c,d;
        a=i*espacioX;  b=(i+4)*espacioX;  c=j*espacioY; d=(j+4)*espacioY;
        if(   a<x && x<b && c<y && y<d  )  return true;
        else return false;
    }

    public void dibujaTablero(float espacioX,float espacioY) {
        int x=0,y=0;
        //dibuja ladrillo
        for(y=1;y<cuadrillay;y=y+2) {
            for (x =1; x < cuadrillax;x=x+2) {
                getBatch().draw(ladrillo, x * espacioX, y * espacioY, espacioX, espacioY);
            }
        }
        //muro de arriba
        for(x=0;x<cuadrillax+1;x++) {
            getBatch().draw(ladrillo, x * espacioX, cuadrillay * espacioY, espacioX, espacioY);

        }
        //muro de derecha
        for(y=0;y<cuadrillay;y++) {
            getBatch().draw(ladrillo, cuadrillax * espacioX, y * espacioY, espacioX, espacioY);

        }
    }
    ////////////////////////////////ENCAPSULAMIENTO DE VARIABLES///////////////////////////
    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public int getBombaGanadora() {
        return bombaGanadora;
    }

    public void setBombaGanadora(int bombaGanadora) {
        this.bombaGanadora = bombaGanadora;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
