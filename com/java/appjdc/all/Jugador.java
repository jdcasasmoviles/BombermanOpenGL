package com.java.appjdc.all;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Jugador extends Thread  {
    private int x;
    private int y;
    int trampas[][];
    private int velocidad=3;
    private  int espacioX,espacioY;
    int cuadrillax=21,cuadrillay=11;
    private int vida=1;
    //procesador
    Procesador mando=new Procesador();
 //constructor
    public Jugador(int x,int y,int trampas[][],Procesador mando)
    {
        this.setX(x);
        this.setY(y);
        this.trampas=trampas;
        espacioX=Gdx.graphics.getWidth() /cuadrillax;
        espacioY=Gdx.graphics.getHeight() /cuadrillay;
        this.mando=mando;
    }

    @Override
    public void run()
    {
        try{ //simular que llegan aleatoriamente al carril
            Thread.sleep( (int)(1000) );
        }
        catch(InterruptedException e){};
        while(true){
            while (getVida() == 1) {
                teclear();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void teclear(){
        //Para escritorio
        if(Gdx.input.isKeyPressed(Input.Keys.Z)) movimiento(1);
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) movimiento(2);
        else if (Gdx.input.isKeyPressed(Input.Keys.C))  movimiento(3);
        else if (Gdx.input.isKeyPressed(Input.Keys.X))  movimiento(4);
        else if (Gdx.input.isKeyPressed(Input.Keys.M))  movimiento(5);
        //Para android
        else  teclearAndroid(mando.getPosicionX(),mando.getPosicionY());
    }
     public void teclearAndroid(int x,int y){
         //Para el gamePad Android
         //izquierda
         if(   perteneceRegion(x, y,0,2) ) movimiento(1);
         //arriba
         else if (  perteneceRegion(x, y, 2, 4)) movimiento(2);
         //derecha
         else if (perteneceRegion(x,y,4,2))  movimiento(3);
         //abajo
         else if (perteneceRegion(x,y,2,0))  movimiento(4);
         //botonA
         else if (  perteneceRegionA(x,y,15,1))  movimiento(5);
         //no hace nada
         else{}

     }

    public boolean perteneceRegionA(int x ,int y,int i,int j){
        int a,b,c,d;
        a=i*espacioX;
        b=(i+4)*espacioX;
        c=j*espacioY;
        d=(j+4)*espacioY;
        if(   a<x && x<b && c<y && y<d  )  return true;
        else return false;
    }

    public boolean perteneceRegion(int x ,int y,int i,int j){
        int a,b,c,d;
        a=i*espacioX;
        b=(i+2)*espacioX;
        c=j*espacioY;
        d=(j+2)*espacioY;
        if(   a<x && x<b && c<y && y<d  )  return true;
        else return false;
    }

    public void movimiento(int direccion){
        //izquierda
        if(direccion==1){
             if(estoyAfuera(getX() - velocidad, getY())||
                     chocaLadrillo(getX() - velocidad, getY() )) { }
            else  setX(getX() - velocidad);
        }
        //arriba
       else if(direccion==2){
            if(estoyAfuera(getX(), getY() + velocidad) ||
                    chocaLadrillo(getX(), getY() + velocidad) ){}
            else  setY(getY()+velocidad);
        }
        //derecha
        else if(direccion==3){
             if(estoyAfuera(getX() + velocidad, getY()) ||
                     chocaLadrillo(getX() + velocidad, getY()) ){}
             else setX(getX()+velocidad);
        }
        //abajo
        else if(direccion==4){
            if(estoyAfuera(getX(), getY() - velocidad) ||
                    chocaLadrillo(getX(), getY() - velocidad) ){}
            else  setY(getY()-velocidad);
        }
        else if(direccion==5){
        ponerBomba(trampas);
        }
        //no se mueve
        else {
        }

    }

    public boolean estoyAfuera(int x,int y){
        double topeX=Gdx.graphics.getWidth()-espacioX;
        double topeY=Gdx.graphics.getHeight()-espacioY;
        if ((x>=topeX|| x<0) || (y>=topeY|| y<0 )){
            return true;
        }
        return false;
    }

    public boolean chocaLadrillo(int x,int y){
        for(int posy=y ; posy<=y+espacioY /2; posy++){
            for(int posx=x ; posx<=x+espacioX/2 ; posx++){
                //dibuja ladrillo
                for(int j=1;j<cuadrillay;j=j+2) {
                    for (int i=1; i< cuadrillax;i=i+2) {
                        int pLadrilloX=i*espacioX;
                        int pLadrilloY=j*espacioY;
                        if(intersectan(posx,posy,pLadrilloX,pLadrilloY)) return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean intersectan(int posx,int posy,int pLadrilloX,int pLadrilloY){
        if( (  (pLadrilloX<posx) &&  (posx < (pLadrilloX+espacioX))  &&
                (pLadrilloY<posy) && (posy < (pLadrilloY+espacioY) ))
                )  return true;
        return false;
    }

    public void ponerBomba(int trampas[][]){
        int tx=getX()/espacioX;
        int ty=getY()/espacioY;
        Bomba bola=new  Bomba(tx,ty,trampas);
        bola.start();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

}
