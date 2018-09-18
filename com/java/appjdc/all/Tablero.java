package com.java.appjdc.all;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tablero {
    private SpriteBatch batch;
    int cuadrillax=21,cuadrillay=11;
    private  int espacioX,espacioY;
    //VARIABLES DEL JUEGO
    int tablero[][]=new int[cuadrillay+5][cuadrillax+5];
    Jugador player2;
    //TEXTURAS
    private  Texture bombita,explosion,bombitaMala;

   /*CONSTRUCTOR*/
    public Tablero(int tablero[][],SpriteBatch batch,Jugador player2){
        this.tablero=tablero;
        this.setBatch(batch);
        this.player2=player2;
        espacioX= Gdx.graphics.getWidth() /cuadrillax;
        espacioY=Gdx.graphics.getHeight() /cuadrillay;

        bombita=new Texture("bombita.png");
        bombitaMala =new Texture("bombitaMala.png");
        explosion=new Texture("explosion.png");
    }

    //////////////////////////////SE TRABAJA CON EL ARRAY TABLERO//////////////////////////////////
    ////LIMPIA TABLERO
    public void iniciarEscenarioJuego(int trampas[][]){
        for(int y=0;y<trampas.length;y++){
            for(int x=0;x<trampas[y].length;x++){
                trampas[y][x]=0;
            }
        }
    }
    //////DIBUJA BOMBAS , explosiones
    public void  dibujarBombas(int trampas[][]){
        for(int y=0;y<trampas.length;y++){
            for(int x=0;x<trampas[y].length;x++){
                //si es explosion bomba mala o buena dibuja
                if (trampas[y][x]==4 || trampas[y][x]==2 ) {
                    getBatch().draw(explosion, x * espacioX, y * espacioY, espacioX, espacioY);
                    //si muere en explosion jugador bombita
                    if(player2.getX()/espacioX==x && player2.getY()/espacioY==y) player2.setVida(0);
                }
                //si es bombita dibuja
                if(trampas[y][x]==1) getBatch().draw(bombita,x*espacioX,y*espacioY,espacioX,espacioY);
                //si es bomba mala dibuja
                if(trampas[y][x]==3) getBatch().draw(bombitaMala,x*espacioX,y*espacioY,espacioX,espacioY);
            }
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }
}
