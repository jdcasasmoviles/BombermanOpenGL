package com.java.appjdc.all;

import com.badlogic.gdx.InputAdapter;
public class Procesador extends InputAdapter {
    private int posicionX;
    private int posicionY;
    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
       // System.out.println("Psocion   " + i + "  " + (-i1 + 480) + "  Has usado el dedo   " + i2 + "  boton  " + i3 + " ");
        return false;
    }
    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        //System.out.println("Psocion xxx     "+i+"  "+(-i1+480)+"  Has usado el dedo   "+i2+"  boton  "+i3+" ");
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
       // System.out.println("DRAUGG Psocion   " + i + "  " + (-i1 + 480) + "  Has usado el dedo   " + i2 );
        // para manejo de mando virtual
        setPosicionX(i);
        setPosicionY(-i1+480);
        return false;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }
}
