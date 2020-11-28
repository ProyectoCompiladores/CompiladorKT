package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
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

    /**
     * Método para obtener el arbol visual de una expresion aritmetica
     */

    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Expresion Aritmetica")
        if (termino != null) {
            nodo.add(termino!!.getArbolVisual())
            if (opAritmetico != null) {
                nodo.add(DefaultMutableTreeNode(opAritmetico!!.lexema))
                if (expArt != null) {
                    return expArt!!.getArbolVisual(nodo)
                }
            }
        }
        return nodo
    }

    fun getArbolVisual(nodo: DefaultMutableTreeNode): DefaultMutableTreeNode {
        if (termino != null) {
            nodo.add(termino!!.getArbolVisual())
            if (opAritmetico != null) {
                nodo.add(DefaultMutableTreeNode(opAritmetico!!.lexema))
                if (expArt != null) {
                    return expArt!!.getArbolVisual(nodo)
                }
            }
        }
        return nodo
    }

    fun getTipo(errores: ArrayList<String?>, ts: TablaSimbolos, ambito: Simbolo): String {
        if (termino!!.getTipo(errores, ts, ambito) == "pntdec") {
            return "pntdec"
        } else if (expArt != null) {
            if (expArt!!.getTipo(errores, ts, ambito) == "pntdec") {
                return "pntdec"
            }
        }
        return "ntr"
    }

    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        if (expArt != null) {
            expArt!!.analizarSemantica(errores, ts, ambito)
        }
        if (termino != null) {
            termino!!.analizarSemantica(errores!!, ts!!, ambito!!)
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(): String {
        var operador = ""
        if (opAritmetico != null) {
            when (opAritmetico!!.lexema) {
                "(+)" -> operador = "+"
                "(-)" -> operador = "-"
                "(/)" -> operador = "/"
                "(*)" -> operador = "*"
                "(%)" -> operador = "%"
                else -> {
                }
            }
        }
        return if (expArt != null) {
            termino!!.traducir() + operador + expArt!!.traducir()
        } else {
            termino!!.traducir()
        }
    }
}