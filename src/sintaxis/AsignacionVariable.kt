package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa las asignaciones de variable.
 */
class AsignacionVariable
/**
 * Constructor de la asignación de variable
 *
 * @param identificadorVariable
 * @param operadorAsignacion
 * @param termino
 */(
        /**
         * @param identificadorVariable
         * the identificadorVariable to set
         */
        var identificadorVariable: Token, var operadorAsignacion: Token,
        /**
         * @param termino
         * the termino to set
         */
        var termino: Termino) : Sentencia() {

    /**
     * Método para obtener el árbol de visual de la asignación de variable.
     */
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Asignación variable")
        nodo.add(DefaultMutableTreeNode(identificadorVariable.lexema))
        nodo.add(DefaultMutableTreeNode(operadorAsignacion.lexema))
        nodo.add(termino.getArbolVisual())
        return nodo
    }


    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        var idEncontrado = false
        for (simbolo in ts!!.tablaSimbolos) {
            if (simbolo.ambito != null) {
                if (identificadorVariable.lexema == simbolo.nombre && !simbolo.isEsFuncion
                        && simbolo.ambito == ambito) {
                    if (simbolo.tipo == termino.getTipo(errores!!, ts!!, ambito!!)) {
                        idEncontrado = true
                        break
                    } else {
                        errores.add(identificadorVariable.lexema
                                + " Los tipos de dato no coinciden o exceden memopria")
                        idEncontrado = true
                        break
                    }
                }
            } else {
                if (identificadorVariable.lexema == simbolo.nombre && !simbolo.isEsFuncion) {
                    if (simbolo.tipo == termino.getTipo(errores!!, ts!!, ambito!!) || simbolo.tipo == "pntdec" && termino.getTipo(errores, ts, ambito) == "ntr") {
                        idEncontrado = true
                        break
                    } else {
                        errores.add(identificadorVariable.lexema
                                + " Los tipos de dato no coinciden o exceden memopria")
                        idEncontrado = true
                        break
                    }
                }
            }
        }
        if (!idEncontrado) {
            if (ambito!!.ambitoPadre != null) {
                analizarSemantica(errores, ts, ambito!!.ambitoPadre!!)
            } else {
                errores!!.add(identificadorVariable.lexema + " No se encontró la variable")
            }
        }
        termino.analizarSemantica(errores!!, ts!!, ambito!!)
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        var variable = identificadorVariable.lexema
        variable = variable.replace("<".toRegex(), "")
        variable = variable.replace(">".toRegex(), "")
        variable = variable.replace("-".toRegex(), "_")
        var asignacion = ""
        asignacion = when (operadorAsignacion.lexema) {
            "es" -> "="
            "es(+)" -> "+="
            "es(-)" -> "-="
            "es(/)" -> "/="
            "es(*)" -> "*="
            "es(%)" -> "%="
            else -> ""
        }
        return identacion + variable + " " + asignacion + " " + termino.traducir()
    }
}