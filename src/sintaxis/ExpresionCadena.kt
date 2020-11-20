package sintaxis

import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion Cadena
 */
class ExpresionCadena : Expresion {
    private var termino: Termino
    private var expCadena: ExpresionCadena? = null

    /**
     * Constructor para una expresion de cadena con solo un termino
     *
     *  [termino]
     */
    constructor(termino: Termino) : super() {
        this.termino = termino
    }

    /**
     * Constructor para una expresion de cadena con uno o mas terminos
     *
     *  [termino]
     * [expCadena]
     */
    constructor(termino: Termino, expCadena: ExpresionCadena?) : super() {
        this.termino = termino
        this.expCadena = expCadena
    }



}