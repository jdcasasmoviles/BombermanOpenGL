package com.java.appjdc.all;

import com.badlogic.gdx.Gdx;

public class mostro extends Thread  {
    private int x;
    private int y;
    int trampas[][];
    private  int espacioX,espacioY;
    int cuadrillax=21,cuadrillay=11;
    private int velocidad=30;
    private int vida=1;
    //constructor
    public mostro(int x,int y,int trampas[][])
    {
        this.setX(x);
        this.setY(y);
        this.trampas=trampas;
        espacioX= Gdx.graphics.getWidth() /cuadrillax;
        espacioY=Gdx.graphics.getHeight() /cuadrillay;
    }


    @Override
    public void run()
    {
        try{ //simular que llegan aleatoriamente al carril
            Thread.sleep( (int)(800) );
        }
        catch(InterruptedException e){};
        while (getVida()==1) {
            try {
                Thread.sleep(800);
                int n=(int)(Math.random()*5)+1;
                movimiento(n);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
       // mostroMuerto();
    }


    public void movimiento(int direccion){
        //izquierda
        if(direccion==1){
            if(estoyAfuera(getX() - velocidad, getY())) { }
            else  setX(getX() - velocidad);
        }
        //arriba
        else if(direccion==2){
            if(estoyAfuera(getX(), getY() + velocidad) ){}
            else  setY(getY()+velocidad);
        }
        //derecha
        else if(direccion==3){
            if(estoyAfuera(getX() + velocidad, getY())){}
            else setX(getX()+velocidad);
        }
        //abajo
        else if(direccion==4){
            if(estoyAfuera(getX(), getY() - velocidad) ){}
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
        //bomba Mala
        // trampas[ty][tx]=3;
        BombaMalvada bola=new  BombaMalvada(tx,ty,trampas);
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

