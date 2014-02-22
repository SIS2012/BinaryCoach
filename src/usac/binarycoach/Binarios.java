package usac.binarycoach;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import android.util.Log;

/**
 * @brief Clase que genera los bits binarios aleatorios.
 *
 */
public class Binarios {
	
	/**
	 * @brief Obtiene los números que ya se han generado.
	 * @param repetidos
	 */
	public Binarios(String[] anterior){
		this.anterior = anterior;
	}
	
	/**
	 * @brief Método que genera los números binarios.
	 * @param bits
	 * @return Retorna la secuencia de bits del numero binario.
	 */
	public String[] generarBinario(int bits){
			decimal = 0;
			int i=0;
			while(i<bits){
				int pos = new Random().nextInt(8);
				if(numero[pos].equals("0")){
					numero[pos]="1";
					decimal += potencia[pos];//Math.pow(2, potencia[pos]);
					i++;
				} 
			}
			
			/*if(!repetidos.contains(decimal)){
				repetidos.add(decimal);
				break;
			}*/
			/*if(!Arrays.equals(anterior, numero)){
				break;
			}*/
		
		return numero;
	}
	
	/**
	 * @brief Método que devuelve el número en formato decimal.
	 * @return Valor decimal
	 */
	public String obtenerDecimal(){
		return ""+decimal;
	}
	
	/**
	 * Función que valida si la respuesta fue correcta.
	 * @param respuesta
	 * @return Verdadero o falso.
	 */
	public boolean validarRespuesta(String[] respuesta){
		return Arrays.equals(numero, respuesta);
	}
	
	int decimal=0;
	String[] numero = {"0","0","0","0","0","0","0","0"};
	//private int[] potencia = {7,6,5,4,3,2,1,0};
	private int[] potencia = {128,64,32,16,8,4,2,1};
	//private LinkedList<Integer> repetidos;// = new LinkedList<Integer>();
	String[] anterior;
}
