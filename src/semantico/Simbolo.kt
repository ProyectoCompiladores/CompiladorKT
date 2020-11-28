package semantico

import java.util.*

/**
 * Clase que representa un simbolo dentro del lenguaje
 */
class Simbolo {
    /**
     * @return the nombre
     */
    /**
     * @param nombre the nombre to set
     */
    var nombre: String
    /**
     * @return the tipo
     */
    /**
     * @param tipo the tipo to set
     */
    var tipo: String
    var ambito: Simbolo? = null
    var ambitoPadre: Simbolo? = null
    var isEsFuncion: Boolean
    /**
     * @return the numeroCIclo
     */
    /**
     * @param numeroCiclo the numeroCIclo to set
     */
    var numeroCiclo = 0
    /**
     * @return the numeroCondicional
     */
    /**
     * @param numeroCondicional the numeroCondicional to set
     */
    var numeroCondicional = 0
    /**
     * @return the tipos
     */
    /**
     * @param tipos the tipos to set
     */
    var tipos: ArrayList<String>? = null
    var retorno = false

    /**
     * @param nombre
     * @param tipo
     * @param ambito
     */
    constructor(nombre: String, tipo: String, ambito: Simbolo?) : super() {
        this.nombre = nombre
        this.tipo = tipo
        this.ambito = ambito
        isEsFuncion = false
    }

    /**
     * @param nombre
     * @param tipo
     * @param tipos
     */
    constructor(nombre: String, tipo: String, tipos: ArrayList<String>?) : super() {
        this.nombre = nombre
        this.tipo = tipo
        this.tipos = tipos
        isEsFuncion = true
    }

}