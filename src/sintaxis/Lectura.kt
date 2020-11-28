package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion Cadena
 */
class Lectura
/**
 * Constructor para leer un dato
 *
 * @param idVariable
 * @param opAsignacion
 * @param leer
 * @param tipoDato
 */(private val idVariable: Token, private val opAsignacion: Token, private val leer: Token, private val tipoDato: Token) : Sentencia() {
    override fun getArbolVisual(): DefaultMutableTreeNode{
        val nodo = DefaultMutableTreeNode("Lectura")
        nodo.add(DefaultMutableTreeNode(idVariable.lexema))
        nodo.add(DefaultMutableTreeNode(opAsignacion.lexema))
        nodo.add(DefaultMutableTreeNode(leer.lexema))
        nodo.add(DefaultMutableTreeNode(tipoDato.lexema))
        return nodo
    }

    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        for (simbolo in ts!!.tablaSimbolos) {
            if (simbolo.ambito != null) {
                if (idVariable.lexema == simbolo.nombre && !simbolo.isEsFuncion
                        && simbolo.ambito == ambito) {
                    if (simbolo.tipo == tipoDato.lexema) {
                        return
                    } else {
                        errores?.add(idVariable.lexema + " El tipo de dato no coinciden con la variable")
                    }
                }
            } else {
                if (idVariable.lexema == simbolo.nombre && !simbolo.isEsFuncion) {
                    if (simbolo.tipo == tipoDato.lexema) {
                        return
                    } else {
                        errores?.add(idVariable.lexema + " El tipo de dato no coinciden con la variable")
                    }
                }
            }
        }
        if (ambito!!.ambitoPadre != null) {
            analizarSemantica(errores, ts, ambito.ambitoPadre!!)
        } else {
            errores?.add(idVariable.lexema + " No se encontró la variable invocada")
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        var asignacion = ""
        when (opAsignacion.lexema) {
            "es" -> asignacion = "="
            "es(+)" -> asignacion = "+="
            "es(-)" -> asignacion = "-="
            "es(/)" -> asignacion = "/="
            "es(*)" -> asignacion = "*="
            "es(%)" -> asignacion = "%="
            else -> {
            }
        }
        var tipo = ""
        tipo = when (tipoDato.lexema) {
            "ltr" -> "JOptionPane.showInputDialog(\"Ingrese valor de caracter\").charAt(0)"
            "ntr" -> "Integer.parseInt(JOptionPane.showInputDialog(\"Ingrese valor entero\"))"
            "pntdec" -> "Double.parseDouble(JOptionPane.showInputDialog(\"Ingrese valor decimal\"))"
            "ltrarr" -> "JOptionPane.showInputDialog(\"Ingrese valor cadena\")"
            "binary" -> "Boolean.parseBoolean(JOptionPane.showInputDialog(\"Ingrese valor entero\")))"
            else -> ""
        }
        var identificador = idVariable.lexema
        identificador = identificador.replace("<".toRegex(), "")
        identificador = identificador.replace(">".toRegex(), "")
        identificador = identificador.replace("-".toRegex(), "_")
        return "$identacion$identificador $asignacion $tipo"
    }
}