package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la unidad de compilacion, (Raiz del arbol de analisis lexico)
 */
class UnidadCompilacion
/**
 * Constructor de la unidad de compilacion
 *
 *  [palabraReservadaClase]:
 * Palabra reservada del lexico {Clase}
 *  [identificadorClase]
 *  [cuerpoClase]
 */(
        /**
         * @param palabraReservadaClase
         * the palabraReservadaClase to set
         */
        // Variables
        var palabraReservadaClase: Token,
        /**
         * @param identificadorClase
         * the identificadorClase to set
         */
        var identificadorClase: Token,
        /**
         * @param cuerpoClase
         * the cuerpoClase to set
         */
        var cuerpoClase: CuerpoClase) {
    /**
     * @return the palabraReservadaClase
     */
    /**
     * @return the identificadorClase
     */
    /**
     * @return the cuerpoClase
     */

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return ("UnidadCompilacion [palabraReservadaClase=" + palabraReservadaClase + ", identificadorClase="
                + identificadorClase + ", cuerpoClase=" + cuerpoClase + "]")
    }




}