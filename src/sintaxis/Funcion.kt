package sintaxis

import lexico.Token
import sintaxis.Ciclo
import sintaxis.Condicional
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa una función
 */
class Funcion {
    /**
     * @return the visibilidad
     */
    /**
     * @param visibilidad
     * the visibilidad to set
     */
    var visibilidad: Token? = null
    /**
     * @return the tipoRetorno
     */
    /**
     * @param tipoRetorno
     * the tipoRetorno to set
     */
    var tipoRetorno: TipoRetorno
    /**
     * @return the identificadorFuncion
     */
    /**
     * @param identificadorFuncion
     * the identificadorFuncion to set
     */
    var identificadorFuncion: Token
    /**
     * @return the palabraReservadaFuncion
     */
    /**
     * @param palabraReservadaFuncion
     * the palabraReservadaFuncion to set
     */
    var palabraReservadaFuncion: Token
    /**
     * @return the listaParametros
     */
    /**
     * @param listaParametros
     * the listaParametros to set
     */
    var listaParametros: ArrayList<Parametro>? = null
    /**
     * @return the listaSentencias
     */
    /**
     * @param listaSentencias
     * the listaSentencias to set
     */
    var listaSentencias: ArrayList<Sentencia>? = null


    /**
     * Funcion con visibilidad, lista de parametros y sentencias
     *
     * @param visibilidad
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     * @param listaParametros
     * @param listaSentencias
     */
    constructor(visibilidad: Token?, tipoRetorno: TipoRetorno, identificadorFuncion: Token,
                palabraReservadaFuncion: Token, listaParametros: ArrayList<Parametro>?, listaSentencias: ArrayList<Sentencia>?) : super() {
        this.visibilidad = visibilidad
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
        this.listaParametros = listaParametros
        this.listaSentencias = listaSentencias
    }

    /**
     * Funcion Sin Visibilidad con Lista De Parametros y Sentencias
     *
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     * @param listaParametros
     * @param listaSentencias
     */
    constructor(tipoRetorno: TipoRetorno, identificadorFuncion: Token, palabraReservadaFuncion: Token,
                listaParametros: ArrayList<Parametro>?, listaSentencias: ArrayList<Sentencia>?) : super() {
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
        this.listaParametros = listaParametros
        this.listaSentencias = listaSentencias
    }

    /**
     * Funcion Sin visibilidad y con lista de Sentencias
     *
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     * @param listaSentencias
     */
    constructor(tipoRetorno: TipoRetorno, identificadorFuncion: Token, palabraReservadaFuncion: Token,
                listaSentencias: ArrayList<Sentencia>?) : super() {
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
        this.listaSentencias = listaSentencias
    }

    /**
     * @param visibilidad
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     * @param listaParametros
     */
    constructor(visibilidad: Token?, tipoRetorno: TipoRetorno, identificadorFuncion: Token,
                listaParametros: ArrayList<Parametro>?, palabraReservadaFuncion: Token) : super() {
        this.visibilidad = visibilidad
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
        this.listaParametros = listaParametros
    }

    /**
     *
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param listaParametros
     * @param palabraReservadaFuncion
     */
    constructor(tipoRetorno: TipoRetorno, identificadorFuncion: Token, listaParametros: ArrayList<Parametro>?,
                palabraReservadaFuncion: Token) : super() {
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
        this.listaParametros = listaParametros
    }

    /**
     * Funcion con visibilidad y con lista de Sentencias
     *
     * @param visibilidad
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     * @param listaSentencias
     */
    constructor(visibilidad: Token?, tipoRetorno: TipoRetorno, identificadorFuncion: Token,
                palabraReservadaFuncion: Token, listaSentencias: ArrayList<Sentencia>?) : super() {
        this.visibilidad = visibilidad
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
        this.listaSentencias = listaSentencias
    }

    /**
     * Funcion con solo Visibilidad
     *
     * @param visibilidad
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     */
    constructor(visibilidad: Token?, tipoRetorno: TipoRetorno, identificadorFuncion: Token,
                palabraReservadaFuncion: Token) : super() {
        this.visibilidad = visibilidad
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
    }

    /**
     * Funcion sin Visibilidad, sin lista Parametros y sin lista Sentencias
     *
     * @param tipoRetorno
     * @param identificadorFuncion
     * @param palabraReservadaFuncion
     */
    constructor(tipoRetorno: TipoRetorno, identificadorFuncion: Token, palabraReservadaFuncion: Token) : super() {
        this.tipoRetorno = tipoRetorno
        this.identificadorFuncion = identificadorFuncion
        this.palabraReservadaFuncion = palabraReservadaFuncion
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        if (visibilidad != null) {
            return if (listaParametros != null) {
                if (listaSentencias != null) {
                    ("Funcion [visibilidad=" + visibilidad + ", tipoRetorno=" + tipoRetorno
                            + ", identificadorFuncion=" + identificadorFuncion + ", palabraReservadaFuncion="
                            + palabraReservadaFuncion + ", listaParametros=" + listaParametros + ", listaSentencias="
                            + listaSentencias + "]")
                } else {
                    ("Funcion [visibilidad=" + visibilidad + ", tipoRetorno=" + tipoRetorno
                            + ", identificadorFuncion=" + identificadorFuncion + ", palabraReservadaFuncion="
                            + palabraReservadaFuncion + ", listaParametros=" + listaParametros + "]")
                }
            } else {
                if (listaSentencias != null) {
                    ("Funcion [visibilidad=" + visibilidad + ", tipoRetorno=" + tipoRetorno
                            + ", identificadorFuncion=" + identificadorFuncion + ", palabraReservadaFuncion="
                            + palabraReservadaFuncion + ", listaSentencias=" + listaSentencias + "]")
                } else {
                    ("Funcion [visibilidad=" + visibilidad + ", tipoRetorno=" + tipoRetorno
                            + ", identificadorFuncion=" + identificadorFuncion + ", palabraReservadaFuncion="
                            + palabraReservadaFuncion + "]")
                }
            }
        } else {
            if (listaParametros != null) {
                return if (listaSentencias != null) {
                    ("Funcion [" + " tipoRetorno=" + tipoRetorno + ", identificadorFuncion="
                            + identificadorFuncion + ", palabraReservadaFuncion=" + palabraReservadaFuncion
                            + ", listaParametros=" + listaParametros + ", listaSentencias=" + listaSentencias + "]")
                } else {
                    ("Funcion [tipoRetorno=" + tipoRetorno + ", identificadorFuncion=" + identificadorFuncion
                            + ", palabraReservadaFuncion=" + palabraReservadaFuncion + ", listaParametros="
                            + listaParametros + "]")
                }
            } else {
                if (listaSentencias != null) {
                    return ("Funcion [tipoRetorno=" + tipoRetorno + ", identificadorFuncion=" + identificadorFuncion
                            + ", palabraReservadaFuncion=" + palabraReservadaFuncion + ", listaSentencias="
                            + listaSentencias + "]")
                }
            }
        }
        return ("Funcion [tipoRetorno=" + tipoRetorno + ", identificadorFuncion=" + identificadorFuncion
                + ", palabraReservadaFuncion=" + palabraReservadaFuncion + "]")
    }

}