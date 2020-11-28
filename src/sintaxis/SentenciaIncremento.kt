package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class SentenciaIncremento
/**
 * Constructor de la sentencia incremento
 *
 * @param identificadorVariable
 * @param incremento
 */(
        /**
         * @param identificadorVariable
         * the identificadorVariable to set
         */
        var identificadorVariable: Token,
        /**
         * @param incremento
         * the incremento to set
         */
        var incremento: Token) : Sentencia() {
    /**
     * @return the identificadorVariable
     */
    /**
     * @return the incremento
     */
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Incremento")
        nodo.add(DefaultMutableTreeNode(identificadorVariable.lexema))
        nodo.add(DefaultMutableTreeNode(incremento.lexema))
        return nodo
    }

    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        for (simbolo in ts!!.tablaSimbolos) {
            if (simbolo.ambito != null) {
                if (identificadorVariable.lexema == simbolo.nombre && !simbolo.isEsFuncion
                        && simbolo.ambito == ambito) {
                    if (simbolo.tipo == "ntr" || simbolo.tipo == "pntdec") {
                        return
                    } else {
                        errores?.add(identificadorVariable.lexema + " La variable no es de tipo numerica")
                        return
                    }
                }
            } else {
                if (identificadorVariable.lexema == simbolo.nombre && !simbolo.isEsFuncion) {
                    if (simbolo.tipo == "ntr" || simbolo.tipo == "pntdec") {
                        return
                    } else {
                        errores?.add(identificadorVariable.lexema + " La variable no es de tipo numerica")
                        return
                    }
                }
            }
        }
        if (ambito!!.ambitoPadre != null) {
            analizarSemantica(errores, ts, ambito.ambitoPadre!!)
        } else {
            errores?.add(identificadorVariable.lexema + " No se encontró la variable")
            return
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        var identificador = ""
        identificador = identificadorVariable.lexema.replace("<".toRegex(), "")
        identificador = identificador.replace(">".toRegex(), "")
        identificador = identificador.replace("-".toRegex(), "_")
        return "$identacion $identificador++"
    }
}