package sintaxis

import lexico.Token
import sintaxis.Ciclo
import sintaxis.Condicional
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa un cilclo
 */
class Ciclo : Sentencia {
    /**
     * @return the ciclo
     */
    /**
     * @param ciclo
     * the ciclo to set
     */
    var ciclo: Token
    /**
     * @return the mientras
     */
    /**
     * @param mientras
     * the mientras to set
     */
    var mientras: Token
    /**
     * @return the expresionLogica
     */
    /**
     * @param expresionLogica
     * the expresionLogica to set
     */
    var expresionLogica: ExpresionLogica
    /**
     * @return the sentencias
     */
    /**
     *  [sentencias]
     */
    var sentencias: ArrayList<Sentencia>? = null

    /**
     * Constructor con sintencias
     *
     *  [ciclo]
     *  [mientras]
     *  [expresionLogica]
     *  [sentencias]
     */
    constructor(ciclo: Token, mientras: Token, expresionLogica: ExpresionLogica, sentencias: ArrayList<Sentencia>?) : super() {
        this.ciclo = ciclo
        this.mientras = mientras
        this.expresionLogica = expresionLogica
        this.sentencias = sentencias
    }

    /**
     * Constructor sin sentencias
     *
     *  [ciclo]
     *  [mientras]
     *  [expresionLogica]
     */
    constructor(ciclo: Token, mientras: Token, expresionLogica: ExpresionLogica) : super() {
        this.ciclo = ciclo
        this.mientras = mientras
        this.expresionLogica = expresionLogica
    }



}