package sintaxis

import lexico.Token
import sintaxis.Termino
import sintaxis.Sentencia
import javax.swing.tree.DefaultMutableTreeNode
import semantico.TablaSimbolos
import semantico.Simbolo
import java.util.ArrayList

/**
 * Clase que representa una sentencia de retorno
 */
class Retorno
/**
 * Constructor de clase
 *
 * @param retorno
 * @param termino
 */(
        /**
         * @param retorno
         * the retorno to set
         */
        var retorno: Token,
        /**
         * @param termino
         * the termino to set
         */
        var termino: Termino) : Sentencia() {
    /**
     * @return the retorno
     */
    /**
     * @return the termino
     */
    override fun getArbolVisual(): DefaultMutableTreeNode{
        val nodo = DefaultMutableTreeNode("Retorno")
        nodo.add(DefaultMutableTreeNode(retorno.lexema))
        nodo.add(termino.getArbolVisual())
        return nodo
    }

    fun analizarSemantica() {}
    fun llenarTablaSimbolos() {}


    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        if (ambito!!.tipo != termino.getTipo(errores!!, ts!!, ambito!!)) {
            errores.add("El tipo de retorno " + ambito.tipo + " de la función " + ambito.nombre
                    + " no coincide con el retorno " + termino.getTipo(errores, ts, ambito))
        }
        var pivote: Simbolo? = ambito
        while (pivote != null) {
            pivote.retorno = true
            pivote = if (pivote.nombre.contains("condicional") && pivote.retorno == false) {
                break
            } else {
                pivote.ambitoPadre
            }
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        return identacion + "return " + termino.traducir()
    }
}