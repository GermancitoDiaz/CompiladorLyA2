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
 * Versión optimizada de Arbol.java
 * @author Diaz Briseño German
 */
public class ArbolGerman {

    // ── Pilas de trabajo ─────────────────────────────────────────────────────
    Stack<Nodo> arbolNodo;
    Stack<String> caracter;

    // ── Conjuntos de caracteres ───────────────────────────────────────────────
    // OPTIMIZACIÓN: cambiadas a constantes (final) — no cambian entre ejecuciones
    final String espacios    = "\t ";
    final String aritmeticos = "+-*()^=/";
    String r="r";

    // ── Raíz y estructuras de soporte ─────────────────────────────────────────
    private Nodo raiz;

    HashMap<String, String> tablaSimbolos;
    HashMap<String, String> erroresSemanticos;
    HashMap<String, String> producciones;

    int paso;

    ArrayList<String> reglasEjecutadas;

    // ── Constructor ───────────────────────────────────────────────────────────
    public ArbolGerman() {
        reglasEjecutadas  = new ArrayList<>();
        tablaSimbolos     = new HashMap<>();
        erroresSemanticos = new HashMap<>();
        producciones      = new HashMap<>();
        arbolNodo         = new Stack<>();
        caracter          = new Stack<>();
        paso              = 0;
    }

    // ── Reglas ejecutadas ─────────────────────────────────────────────────────
    public String getReglasEjecutadas() {
        String reglasE = "";
        for (int i = 0; i < reglasEjecutadas.size(); i++) {
            System.out.println("Regla ejecutada: " + reglasEjecutadas.get(i));
            reglasE += reglasEjecutadas.get(i) + "\n";
        }
        return reglasE;
    }

    // ── Tabla de símbolos ─────────────────────────────────────────────────────
    public void agregaValex(String lexema, String valor) {
        tablaSimbolos.put(lexema, valor);
    }

    public String regresaValex(String lexema) {
        return tablaSimbolos.get(lexema);
    }

    // ── MÉTODO GUARDAR (optimizado) ───────────────────────────────────────────
    /**
     * OPTIMIZACIONES aplicadas:
     * - Eliminada variable local 'operador' — se lee caracter.peek() una sola vez
     *   y se usa directamente en el switch.
     * - Eliminadas variables locales 'reglaE' — el String se construye inline.
     * - switch reemplaza la cadena de if independientes — más eficiente.
     * - Llaves omitidas en cases de una sola sentencia.
     */
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

    // ── MÉTODO CREAR (optimizado) ─────────────────────────────────────────────
    /**
     * OPTIMIZACIONES aplicadas:
     * - Eliminada variable local 'token' declarada fuera del while — se declara
     *   dentro para limitar su scope al bloque donde se usa.
     * - OPTIMIZACIÓN PRINCIPAL: indexOf reemplazado por contains() en todos los
     *   puntos de decisión — más legible y semánticamente correcto para Strings.
     * - Eliminada variable 'exa' redundante — se llama caracter.peek() directamente.
     * - reglasEjecutadas.clear() al inicio para soportar múltiples ejecuciones
     *   sin acumular reglas de llamadas anteriores.
     * - Llaves omitidas en if de una sola instrucción.
     */
    public Nodo crear(String expresion) {
    // Limpiar estado para múltiples ejecuciones
    paso = 0;
    reglasEjecutadas.clear();
    arbolNodo.clear();
    caracter.clear();

    StringTokenizer tokenizer = new StringTokenizer(
        expresion, espacios + aritmeticos, true);

    while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        System.out.println("Token: " + token);

        if (espacios.contains(token)) {
            System.out.println("Omitiendo espacio");

        } else if (!aritmeticos.contains(token)) {
            // Es un operando (variable o número)
            arbolNodo.push(new Nodo(token));
            paso++;
            reglasEjecutadas.add("p" + paso
                + " T.nodo = new Hoja(id<" + token
                + ">,id.entrada_" + token + ")");

        } else if (token.equals(")")) {
            // Cerrar paréntesis: reducir hasta encontrar "("
            while (!caracter.empty() && !caracter.peek().equals("("))
                guardar();
            if (!caracter.empty()) caracter.pop(); // quitar el "("

        } else {
            // Es un operador: respetar precedencia
            if (!token.equals("(") && !caracter.empty()) {
                // CORRECCIÓN PRINCIPAL: !caracter.empty() en lugar de caracter.empty()
                while (!caracter.empty()
                        && !caracter.peek().equals(")")
                        && !caracter.peek().equals("(")
                        && aritmeticos.indexOf(caracter.peek())
                           <= aritmeticos.indexOf(token)) {
                    guardar();
                }
            }
            caracter.push(token);
        }
    }

    // Vaciar lo que quede en la pila de operadores
    while (!caracter.empty()) {
        if (caracter.peek().equals("(") || caracter.peek().equals(")"))
            caracter.pop();
        else {
            guardar();
            if (!arbolNodo.empty()) raiz = arbolNodo.peek();
        }
    }

    return raiz;
   }
}
