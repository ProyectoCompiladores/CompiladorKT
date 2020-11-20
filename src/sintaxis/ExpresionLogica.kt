package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion logica
 */
class ExpresionLogica : Expresion {
    /**
     * @return the expresionRelacional
     */
    /**
     * @param expresionRelacional the expresionRelacional to set
     */
    var expresionRelacional: ExpresionRelacional? = null
    /**
     * @return the opLogico
     */
    /**
     * @param opLogico the opLogico to set
     */
    var opLogico: Token? = null
    /**
     * @return the expresionLogica
     */
    /**
     * @param expresionLogica the expresionLogica to set
     */
    var expresionLogica: ExpresionLogica? = null

    /**
     * Constructor que crea expresion logica con solo una expresion logica
     *
     * @param expresionLogica
     */
    constructor(expresionLogica: ExpresionLogica?) : super() {
        this.expresionLogica = expresionLogica
    }

    /**
     * Constructor que crea expresion logica con solo una expresion relacional
     *
     * @param expresionRelacional
     */
    constructor(expresionRelacional: ExpresionRelacional?) : super() {
        this.expresionRelacional = expresionRelacional
    }

    /**
     * Constructor que crea expresion logica con caso contrario
     *
     * @param expresionRelacional
     * @param operadorLogico
     * @param expresionLogica
     */
    constructor(expresionRelacional: ExpresionRelacional?, opLogico: Token?, expresionLogica: ExpresionLogica?) : super() {
        this.expresionRelacional = expresionRelacional
        this.opLogico = opLogico
        this.expresionLogica = expresionLogica
    }

}