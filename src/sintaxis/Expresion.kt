package sintaxis

import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase Abstracta que representa la Expresion
 */
abstract class Expresion {
    abstract fun getArbolVisual(): DefaultMutableTreeNode
    abstract fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?)
    abstract fun llenarTablaSimbolos(ts: TablaSimbolos?)
    abstract fun traducir(): String?

}