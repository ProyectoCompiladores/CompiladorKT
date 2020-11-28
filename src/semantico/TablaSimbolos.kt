package semantico

import java.util.*

/**
 * Clase que representa la tabla de simbolos almacenados dentro del codigo
 */
class TablaSimbolos(errores: ArrayList<String?>) {
    /**
     * @return the tablaSimbolos
     */
    /**
     * @param tablaSimbolos
     * the tablaSimbolos to set
     */
    var tablaSimbolos: ArrayList<Simbolo>
    /**
     * @return the errores
     */
    /**
     * @param errores
     * the errores to set
     */
    var errores: ArrayList<String?>
    fun agregarSimbolo(nombre: String, tipo: String, ambito: Simbolo) {
        val s = getSimbolo(nombre, tipo, ambito)
        if (s != null) {
            tablaSimbolos.add(s)
        } else {
            errores.add("El Identificador" + nombre + "dentro de " + ambito + "esta repetido")
        }
    }

    fun agregarFuncion(nombre: String, tipo: String, tipos: ArrayList<String>?): Simbolo? {
        val s = getSimbolo(nombre, tipo, tipos)
        return if (s != null) {
            tablaSimbolos.add(s)
            s
        } else {
            errores.add("La funcion" + nombre + "esta repetida")
            null
        }
    }

    fun getSimbolo(nombre: String, tipo: String, ambito: Simbolo): Simbolo? {
        for (simbolo in tablaSimbolos) {
            if (simbolo.nombre == nombre && simbolo.tipo == tipo && simbolo.ambito!!.nombre == ambito!!.nombre && simbolo.ambito!!.tipo == tipo && !simbolo.isEsFuncion) {
                return null
            }
        }
         if (ambito != null) {
            if (ambito.ambitoPadre != null) {
                getSimbolo(nombre, tipo, ambito.ambitoPadre!!)
            } else {
                Simbolo(nombre, tipo, ambito)
            }
        }
        return Simbolo(nombre, tipo, ambito)
    }

    fun getSimbolo(nombre: String, tipo: String, tipos: ArrayList<String>?): Simbolo? {
        for (simbolo in tablaSimbolos) {
            if (simbolo.nombre == nombre && simbolo.tipo == tipo && simbolo.tipos!!.containsAll(tipos!!) && !simbolo.isEsFuncion) {
                return null
            }
        }
        return Simbolo(nombre, tipo, tipos)
    }

    init {
        tablaSimbolos = ArrayList()
        this.errores = errores
    }
}