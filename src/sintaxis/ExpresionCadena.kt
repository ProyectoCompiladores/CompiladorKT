package sintaxis

import semantico.Simbolo
import semantico.TablaSimbolos
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
     * @param termino
     */
    constructor(termino: Termino) : super() {
        this.termino = termino
    }

    /**
     * Constructor para una expresion de cadena con uno o mas terminos
     *
     * @param termino
     * @param expCadena
     */
    constructor(termino: Termino, expCadena: ExpresionCadena?) : super() {
        this.termino = termino
        this.expCadena = expCadena
    }
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Expresion Cadena")
        nodo.add(termino.getArbolVisual())
        if (expCadena != null) {
            expCadena!!.getArbolVisual(nodo)
        }
        return nodo
    }

    fun getArbolVisual(nodo: DefaultMutableTreeNode): DefaultMutableTreeNode {
        nodo.add(termino.getArbolVisual())
        if (expCadena != null) {
            expCadena!!.getArbolVisual(nodo)
        }
        return nodo
    }



    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        if (termino.getTipo(errores!!, ts!!, ambito!!) != "ltrarr") {
            return
        } else if (expCadena != null) {
            if (expCadena!!.getTipo(errores, ts, ambito) == "ltrarr") {
            } else {
                errores.add("Falta alguna cadena para concatenar")
                return
            }
        } else {
            errores.add("Falta alguna cadena para concatenar")
            return
        }
    }

    fun getTipo(errores: ArrayList<String?>, ts: TablaSimbolos, ambito: Simbolo): String {
        return if (termino.getTipo(errores, ts, ambito) != "ltrarr") {
            "ltrarr"
        } else if (expCadena != null) {
            if (expCadena!!.getTipo(errores, ts, ambito) == "ltrarr") {
                "ltrarr"
            } else {
                errores.add("Falta alguna cadena para concatenar")
                "sr"
            }
        } else {
            "sr"
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(): String {
        return if (expCadena != null) {
            termino.traducir() + " + " + expCadena!!.traducir()
        } else {
            termino.traducir()
        }
    }

}