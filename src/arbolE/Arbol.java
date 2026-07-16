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
public class Arbol {
    //Atributos
    Stack<Nodo> arbolNodo;
    Stack<String> caracter;
    //Identificar entre Operador y Operandos
    final String espacios="\t";
    final String aritmeticos="+-*()^=/";
    final String variables="abcdefghijklmnopqrstuvwxyz";
    final String opMultiplica="*";
    
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
    public Arbol(){
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
    }//agregarValex - Analisis semantico
    
    public String regresaValex(String lexema){
        return this.tablaSimbolos.get(lexema);
    }//regresaValex
    
    public void guardar(){//Permite construir el arbol
        paso++;
        
        Nodo izquierdo = (Nodo) arbolNodo.pop();
        Nodo derecho = (Nodo) arbolNodo.pop();
        
        String operador = caracter.peek();
        arbolNodo.push(new Nodo(derecho,caracter.pop(),izquierdo));
        //
        if(operador.equals("+")){//Es la suma
            String reglaE = "E.nodo = new Nodo(+,El.nodo, T.nodo)";
            reglasEjecutadas.add("p"+paso+" "+reglaE);
        }//SUMA
        if(operador.equals("-")){//ES LA RESTA
            String reglaE = "E.nodo = new Nodo(-,El.nodo, T.nodo)";
            reglasEjecutadas.add("p"+paso+" "+reglaE);
        }//RESTA
         if(operador.equals("*")){//ES LA MULTIPLICACION
            String reglaE = "E.nodo = new Nodo(*,El.nodo, T.nodo)";
            reglasEjecutadas.add("p"+paso+" "+reglaE);
        }//MULTIPLICACION
        if(operador.equals("/")){//ES LA DIVISION
            String reglaE = "E.nodo = new Nodo(/,El.nodo, T.nodo)";
            reglasEjecutadas.add("p"+paso+" "+reglaE);
        }//DIVISION
    }//Guardar
    
    //METODO DEL ARBOL
    public Nodo crear(String expresion){
        //1. Considerar la expresion como un conjunto de tokens
        StringTokenizer tokenizer;
        String token;
        //0. Inicializar valores para varias ejecuciones
        paso=0;//Paso de las reglas semanticas        
        //2. Separacion de tokens de la expresion
        tokenizer = new StringTokenizer(expresion, espacios+aritmeticos,true);
        //3. Mientras existan tokens
        while(tokenizer.hasMoreTokens()){
            //4. Omintir espacios en blanco
            token = tokenizer.nextToken();
            System.out.println("Token "+token);
            if(espacios.indexOf(token)>=0){
                //5. Se trata de un identificador
                System.out.println("Omitinedo espacios");
                //
            }else if(aritmeticos.indexOf(token)<0){//no es un operador aritmetico
                //6. Extraer de la pila los terminos que estaban
                arbolNodo.push(new Nodo(token));
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
                  while(!exa.equals(")")&&caracter.empty()
                          &&aritmeticos.indexOf(exa)>=aritmeticos.indexOf(token)){
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
}//class
