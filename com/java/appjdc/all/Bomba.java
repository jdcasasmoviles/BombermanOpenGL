package com.java.appjdc.all;
import com.badlogic.gdx.Gdx;
public class Bomba  extends Thread {
    private int x;
    private int y;
    private  int espacioX,espacioY;
    int cuadrillax=21,cuadrillay=11;
    int detonacion=5000;
   int trampas[][];

    public Bomba (int x,int y,int trampas[][])
    {
        this.setX(x);
        this.setY(y);
        this.trampas=trampas;
        espacioX= Gdx.graphics.getWidth() /cuadrillax;
        espacioY=Gdx.graphics.getHeight() /cuadrillay;
    }
    //inicio run()
    @Override
    public void run()
    {
        try{
            //Pone bomba
            trampas[getY()][getX()]=1;
            //tiempo que pasa para que detone
            Thread.sleep( (int)(detonacion) );
            explosion();
        }
        catch(InterruptedException e){};

        try {
            Thread.sleep((int) (500));
            normalidad();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     void explosion(){

        for(int i=0;i<5;i++){
            if((getX() - (i - 2))>=0 && getY()>=0
                    &&  trampas[getY()][(getX() - (i - 2))]  !=1 &&  trampas[getY()][(getX() - (i - 2))]  !=3
                    )
                trampas[getY()][(getX() - (i - 2))] = 2;
        }
        for(int i=0;i<5;i++) {
            if ((getY() - (i - 2)) >= 0 && getX() >= 0
                    &&  trampas[(getY() - (i - 2))][getX()]   !=1 && trampas[(getY() - (i - 2))][getX()]  !=3)
            trampas[(getY() - (i - 2)) ][getX()] = 2;
        }
         trampas[getY()][getX()]=2;
    }

    void normalidad(){
        for(int i=0;i<5;i++){
            if((getX() - (i - 2))>=0 && getY()>=0
                    &&  trampas[getY()][(getX() - (i - 2))]  !=1 &&  trampas[getY()][(getX() - (i - 2))]  !=3
                    ) trampas[getY()][(getX() - (i - 2))] = 0;
        }
        for(int i=0;i<5;i++) {
            if ((getY() - (i - 2)) >= 0 && getX() >= 0
                    &&  trampas[(getY() - (i - 2))][getX()]   !=1 && trampas[(getY() - (i - 2))][getX()]  !=3
                    ) trampas[(getY() - (i - 2))][getX()] = 0;
        }
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
}
