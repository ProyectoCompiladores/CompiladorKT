package sintaxis

class ErrorSintactico
/**
 * @param mensaje
 * @param fila
 * @param columna
 * @param columnaReal
 */(
        /**
         * @param mensaje the mensaje to set
         */
        var mensaje: String,
        /**
         * @param fila the fila to set
         */
        var fila: Int,
        /**
         * @param columna the columna to set
         */
        var columna: Int,
        /**
         * @param columnaReal the columnaReal to set
         */
        var columnaReal: Int) {
    /**
     * @return the mensaje
     */
    /**
     * @return the fila
     */
    /**
     * @return the columna
     */
    /**
     * @return the columnaReal
     */

    /* (non-Javadoc)
       * @see java.lang.Object#toString()
       */
    override fun toString(): String {
        return ("ErrorSintactico [mensaje=" + mensaje + ", fila=" + fila + ", columna=" + columna + ", columnaReal="
                + columnaReal + "]")
    }
}