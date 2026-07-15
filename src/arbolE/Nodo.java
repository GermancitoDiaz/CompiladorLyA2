/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arbolE;

/**
 *
 * @author thege
 * CLASE DIAZ BRISEÑO GERMAN
 * NOMBRE
 * PARTE 1. ANALISIS SINTACTICO
 * PARTE 2. ANALISIS SEMANTICO
 * PARTE 3. CODIGO INTERMEDIO
 * PARTE 4. CODIGO OBJETO
 */
public class Nodo {
    //Atributos
    private String dato;
    private Nodo padre;
    private Nodo izquierdo;
    private Nodo derecho;
    private String codigoIntermedio;
    private String lugar;
    
  public Nodo(String dato){
      this.dato=dato;
  }//constructor
  public Nodo(Nodo derecho, String dato, Nodo izquierdo){
      this.derecho=derecho;
      this.dato=dato;
      this.izquierdo=izquierdo;
      this.padre=null;
      this.codigoIntermedio="";
      this.lugar="";
  }//constructor

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public Nodo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public Nodo getDerecho() {
        return derecho;
    }

    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }

    public String getCodigoIntermedio() {
        return codigoIntermedio;
    }

    public void setCodigoIntermedio(String codigoIntermedio) {
        this.codigoIntermedio = codigoIntermedio;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    String getValor() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
  
}
