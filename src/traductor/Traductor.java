package traductor;

import sintaxis.UnidadCompilacion;

/**
 * Clase que realiza la traducci�n a enguaje Java
 */
public class Traductor {

	private UnidadCompilacion unidadCompilacion;
	
	

	/**
	 * @param unidadCompilacion
	 */
	public Traductor(UnidadCompilacion unidadCompilacion) {
		super();
		this.unidadCompilacion = unidadCompilacion;
	}

	/**
	 * @return the unidadCompilacion
	 */
	public UnidadCompilacion getUnidadCompilacion() {
		return unidadCompilacion;
	}

	/**
	 * M�todo que traduce desde la unidad de compilaci�n
	 * 
	 * @return
	 */
	public String traducir() {
		return unidadCompilacion.traducir();
	}

	/**
	 * @param unidadCompilacion
	 *            the unidadCompilacion to set
	 */
	public void setUnidadCompilacion(UnidadCompilacion unidadCompilacion) {
		this.unidadCompilacion = unidadCompilacion;
	}

}
