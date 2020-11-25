package sintaxis
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa el cuerpo de la clase, (Funciones y variables)
 */
class CuerpoClase {
    /**
     * @return the funcion
     */
    /**
     * @param funcion
     * the funcion to set
     */
    // Variables
    var funcion: Funcion? = null
    /**
     * @return the declaracionVariable
     */
    /**
     * @param declaracionVariable
     * the declaracionVariable to set
     */
    var declaracionVariable: DeclaracionVariable? = null
    /**
     * @return the cuerpoClase
     */
    /**
     * @param cuerpoClase
     * the cuerpoClase to set
     */
    var cuerpoClase: CuerpoClase? = null

    /**
     * Constructor con una funcion y posibilidad de agregar mas cuerpos de clase
     *
     * @param funcion
     * @param cuerpoClase
     */
    constructor(funcion: Funcion?, cuerpoClase: CuerpoClase?) : super() {
        this.funcion = funcion
        this.funcion = funcion
        this.cuerpoClase = cuerpoClase
    }

    /**
     * Constructor con una funcion y posibilidad de agregar mas cuerpos de clase
     *
     * @param declaracionVariable
     * @param cuerpoClase
     */
    constructor(declaracionVariable: DeclaracionVariable?, cuerpoClase: CuerpoClase?) : super() {
        this.declaracionVariable = declaracionVariable
        this.cuerpoClase = cuerpoClase
    }

    /**
     * Constructor con solo una funcion
     *
     * @param funcion
     */
    constructor(funcion: Funcion?) : super() {
        this.funcion = funcion
    }

    /**
     * Constructor con solo una declaracion de variable
     *
     * @param declaracionVariable
     */
    constructor(declaracionVariable: DeclaracionVariable?) : super() {
        this.declaracionVariable = declaracionVariable
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return if (funcion != null) {
            if (cuerpoClase != null) "CuerpoClase [funcion=" + funcion + "cuerpoClase=" + cuerpoClase + "]" else {
                "CuerpoClase [funcion=$funcion]"
            }
        } else if (declaracionVariable != null) {
            if (cuerpoClase != null) "CuerpoClase [declaracionVariable=" + declaracionVariable + "cuerpoClase=" + cuerpoClase + "]" else {
                "CuerpoClase [declaracionVariable=$declaracionVariable]"
            }
        } else {
            ""
        }
    }


}