package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion Aritmetica
 */
class ExpresionAritmetica : Expresion {

    // @return the termino
    // [termino] the termino to set

    var termino: Termino? = null

    // @return the opAritmetico

    // [opAritmetico] the opAritmetico to set

    var opAritmetico: Token? = null

    // @return the expArt

    //  [expArt] the expArt to set

    var expArt: ExpresionAritmetica? = null

    /**
     * Constructor que identifica la expresion Aritmetica conformada solo por un
     * Termino
     * <ExpresionAritmetica>::= <Termino>
     *  [termino]
    </Termino></ExpresionAritmetica> */
    constructor(termino: Termino?) {
        this.termino = termino
    }

    /**
     * Constructor que identifica la expresion aritmetica conformada por un termino,
     * una expresion aritmetica y un operador aritmetico
     *
     * <ExpresionAritmetica>::= <Termino> operadorAritmetico <ExpresionAritmetica>
     *
     *  [termino]
     *  [opAritmetico]
     *  [expArt]
    </ExpresionAritmetica></Termino></ExpresionAritmetica> */
    constructor(termino: Termino?, opAritmetico: Token?, expArt: ExpresionAritmetica?) {
        this.termino = termino
        this.opAritmetico = opAritmetico
        this.expArt = expArt
    }

    /**
     * Constructor que identifica la expresion aritmetica conformadad por una
     * expresion aritmetica
     *
     * [expArt]
     */
    constructor(expArt: ExpresionAritmetica?) : super() {
        this.expArt = expArt
    }



    fun getArbolVisual(nodo: DefaultMutableTreeNode): DefaultMutableTreeNode {
        if (termino != null) {
            nodo.add(termino!!.arbolVisual)
            if (opAritmetico != null) {
                nodo.add(DefaultMutableTreeNode(opAritmetico!!.lexema))
                if (expArt != null) {
                    return expArt!!.getArbolVisual(nodo)
                }
            }
        }
        return nodo
    }


}

