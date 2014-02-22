package usac.binarycoach;

import java.io.Serializable;

public class Jugador implements Serializable{
	/**
	 * @brief Almacena la información del jugador.
	 */
	private static final long serialVersionUID = 1L;
	public Jugador(int id, String jugador, String password, int puntos, int nivel){
		this.id=id;
		this.jugador=jugador;
		this.password=password;
		this.puntos=puntos;
		this.nivel=nivel;
	}
	int id, puntos, nivel;
	String jugador, password;
}
