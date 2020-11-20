package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la sentencia
 */
class ExpresionRelacional : Expresion {
    /**
     * @return the expAritmetica
     */
    /**
     * @param expAritmetica
     * the expAritmetica to set
     */
    var expAritmetica: ExpresionAritmetica? = null
    /**
     * @return the opRelacional
     */
    /**
     * @param opRelacional
     * the opRelacional to set
     */
    var opRelacional: Token? = null
    /**
     * @return the expRelacional
     */
    /**
     * @param expRelacional
     * the expRelacional to set
     */
    var expRelacional: ExpresionRelacional? = null

    /**
     * Constructor con expresion aritmetica
     *
     * @param expAritmetica
     */
    constructor(expAritmetica: ExpresionAritmetica?) : super() {
        this.expAritmetica = expAritmetica
    }

    /**
     * Constructor con expresion aritmetica y operador relacional
     *
     * @param expAritmetica
     * @param opRelacional
     * @param expRelacional
     */
    constructor(expAritmetica: ExpresionAritmetica?, opRelacional: Token?,
                expRelacional: ExpresionRelacional?) : super() {
        this.expAritmetica = expAritmetica
        this.opRelacional = opRelacional
        this.expRelacional = expRelacional
    }

    /**
     * Constructor con expresion relacional
     *
     * @param expRelacional
     */
    constructor(expRelacional: ExpresionRelacional?) : super() {
        this.expRelacional = expRelacional
    }


}