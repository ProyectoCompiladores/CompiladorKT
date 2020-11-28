package semantico

import sintaxis.UnidadCompilacion
import java.util.*

/**
 * Clase que realiza el análisis semántico
 */
class AnalizadorSemantico(
        /**
         * @param unidadCompilacion the unidadCompilacion to set
         */
        var unidadCompilacion: UnidadCompilacion, ts: TablaSimbolos?,
        /**
         * @param errores the errores to set
         */
        var errores: ArrayList<String?>?) {
    /**
     * @return the unidadCompilacion
     */
    /**
     * @return the ts
     */
    /**
     * @param ts the ts to set
     */
    var ts: TablaSimbolos

    /**
     * @return the errores
     */
    fun llenarTablaSimbolos() {
        unidadCompilacion.llenarTablaSimbolos(ts)
    }

    fun analizarSemantica() {
        unidadCompilacion.analizarSemantica(ts, errores)
    }

    /**
     * @param unidadCompilacion
     * @param ts
     * @param errores
     */
    init {
        this.ts = TablaSimbolos(errores!!)
    }
}