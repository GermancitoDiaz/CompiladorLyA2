/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arbolE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author thege
 */



public class ArbolAgenteIA {
    //Atributos
    public String emu86;

    Stack<Nodo> arbolNodo;
    Stack<String> caracter;
    //Identificar entre Operador y Operandos
    final String espacios="\t";
    final String aritmeticos="+-*()^=/";
    final String variables="abcdefghijklmnopqrstuvwxyz";
    final String opMultiplica="*";
    String reglaSemantica=""; 
    String r="";
    private Nodo raiz;
    // 30 Junio
    String[] temporales={"T1","T2","T3","T4","T5"};
    
    HashMap<String, String> tablaSimbolos;
    HashMap<String, String> erroresSemanticos;
    HashMap<String, String> producciones;
    int paso;
    //01 Julio
    ArrayList <String> reglasEjecutadas;
    //constructor
    public ArbolAgenteIA(){
        emu86 = ";DIAZ GERMAN \n" +
                ".MODEL SMALL \n" +
                ".STACK \n" +
                ".DATA \n" ;
        reglasEjecutadas = new ArrayList <String>();
        tablaSimbolos = new HashMap();
        erroresSemanticos = new HashMap();
        producciones = new HashMap();
        
        arbolNodo = new Stack<Nodo>();
        caracter = new Stack<String>();
        
        paso=0;
    }//constructor
    
    public String getReglasEjecutadas(){
        String reglasE="";
        for(int i=0; i<reglasEjecutadas.size();i++){
            System.out.println("Reglas ejecutadas"+
                    reglasEjecutadas.get(i));
            reglasE+=reglasEjecutadas.get(i)+"\n";
        }//for
        return reglasE;
    }
    
    public void agregaValex (String lexema, String valor){
        tablaSimbolos.put(lexema, valor);
    }//agregarValex - Analisis semantico
    
    public String regresaValex(String lexema){
        return this.tablaSimbolos.get(lexema);
    }//regresaValex
    
    public void guardar() {
        paso++;
        r = "r" + paso;
        // OPTIMIZACIÓN: eliminada variable 'operador' redundante;
        // se obtiene el operador directamente con peek() antes de pop()
        Nodo izquierdo = arbolNodo.pop();
        Nodo derecho   = arbolNodo.pop();
        String operador      = caracter.pop();

        arbolNodo.push(new Nodo(derecho, operador, izquierdo));

        // OPTIMIZACIÓN: switch en lugar de 4 if independientes
        String reglaE = "E.nodo = new Nodo(" + operador + ", E1.nodo, T.nodo";
        reglasEjecutadas.add("p" + paso + " " + reglaE);
    }
    
    //METODO DEL ARBOL
    public Nodo crear(String expresion){
        //1. Considerar la expresion como un conjunto de tokens
        StringTokenizer tokenizer;
        String token;
        String valor=" ";
        //0. Inicializar valores para varias ejecuciones
        paso=0;//Paso de las reglas semanticas  
        reglaSemantica = ""; r ="";
        //2. Separacion de tokens de la expresion
        tokenizer = new StringTokenizer(expresion, espacios+aritmeticos+"/",true);
        //3. Mientras existan tokens
        while(tokenizer.hasMoreTokens()){
            //4. Omintir espacios en blanco
            token = tokenizer.nextToken();
            if(espacios.contains(token)) continue;
                //5. Se trata de un identificador
            if(aritmeticos.indexOf(token)<0){
           //no es un operador aritmetico
                //6. Extraer de la pila los terminos que estaban
                arbolNodo.push(new Nodo(token));
                emu86+=token+" dw "+valor+" \n ";
                paso++;
                String regla ="T.nodo = new Hoja(id<"+token+">,id.entrada_"+token+")";
                reglasEjecutadas.add("p"+paso+""+regla);
            }else  if(token.equals(")")){
                //7. Tratar tokens que no son parentesis
                    
                while(!caracter.empty()&& !caracter.peek().equals("(")){
                    guardar();
                            
                }//while
                caracter.pop();
                //if
            }else{
              if(!token.equals("(")&&!caracter.empty()){
                  String exa=(String)caracter.peek();
                  while(!caracter.empty() && !exa.equals("(") 
                          && aritmeticos.indexOf(exa)>=aritmeticos.indexOf(token)){
                            guardar();
                            if(!caracter.empty()){
                                exa=(String)caracter.peek();
                            }//IF !caracter.empty
                  }//while !exa
              }//if-token
              caracter.push(token);
            }//if else
            //8. Guardar el token
        }//while
        while(!caracter.empty()){
            if(caracter.peek().equals(")")){
                caracter.pop();
            }else{
                guardar();
                raiz=(Nodo) arbolNodo.peek();
            }//else
        }//while !caracter.empty
        return raiz;
    }//crear
    
    private int obtenerPrioridad(String operador){
        switch (operador){
            case "^":
                return 3;
            case "*": case "/":
                return 2;
            case "+": case "-":
                return 1;
            case "=":
                return 0;
            default:
                return -1;//Para parentesis u otros caracteres
        }//Switch
    }//Operador prioridad
    
    public Nodo convertirAGAD(Nodo raizAST) {
        HashMap<String, Nodo> tabla = new HashMap<>();
        return convertir(raizAST, tabla);
    }

    private Nodo convertir(Nodo n, HashMap<String, Nodo> tabla) {
        if (n == null) return null;

        if (n.getIzquierdo() == null && n.getDerecho() == null) {
            String clave = "HOJA#" + n.getDato();
            Nodo existente = tabla.get(clave);
            if (existente != null) return existente; // reutiliza
            tabla.put(clave, n);
            return n;
        }

        // Procesar hijos primero (post-orden): así al llegar al padre
        // ya sabemos si los hijos son nodos compartidos o no.
        Nodo izqNuevo = convertir(n.getIzquierdo(), tabla);
        Nodo derNuevo = convertir(n.getDerecho(), tabla);

        // Reasignar hijos (puede que ahora apunten a nodos ya existentes)
        n.setIzquierdo(izqNuevo);
        n.setDerecho(derNuevo);

        String clave = n.getDato() + "#" 
                     + System.identityHashCode(izqNuevo) + "#" 
                     + System.identityHashCode(derNuevo);

        Nodo existente = tabla.get(clave);
        if (existente != null) return existente; 

        tabla.put(clave, n);
        return n;
    }
}//class