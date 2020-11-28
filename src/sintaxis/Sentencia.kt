package sintaxis

import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la sentencia
 */
abstract class Sentencia {
    abstract fun getArbolVisual(): DefaultMutableTreeNode
    abstract fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?)
    abstract fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?)
    abstract fun traducir(identacion: String?, global: Boolean): String?

}