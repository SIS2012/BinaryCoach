package usac.binarycoach;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 
 * @brief Esta clase dibuja y maneja las conversiones de binarios
 * 
 *
 */
public class Conversiones extends View implements OnTouchListener {

	static int x, y;
	final static int NORMAL=0;
	final static int AYUDA=1;
	final static int CORRECTO=2;
	int color_salida=NORMAL;
	Tiempo tiempo = new Tiempo();
	int segundos = 0;

	Paint paint;
	String[] potencias = { "128", " 64", " 32", " 16", "  8", "  4", "  2", "  1" };
	String[] nBinario = { "0", "0", "0", "0", "0", "0", "0", "0" };
	String[] anterior = { "0", "0", "0", "0", "0", "0", "0", "0" };
	String[] nBinarioRes = { "0", "0", "0", "0", "0", "0", "0", "0" };
	/*---------*/
	String[] nBinario2 = { "0", "0", "0", "0", "0", "0", "0", "0" };
	String[] resBooleano = { "0", "0", "0", "0", "0", "0", "0", "0" };
	final static int AND = 0;
	final static int OR = 1;
	final static int XOR = 2;
	int tipoOperacion;
	String op = "";
	
	String nDecimal = "";
	int nivel = 1;
	int puntos = 0;
	Jugador jugador;
	Bitmap rojo[] = {BitmapFactory.decodeResource(getResources(), R.drawable.cr),BitmapFactory.decodeResource(getResources(), R.drawable.ur)};
	Bitmap verde[] = {BitmapFactory.decodeResource(getResources(), R.drawable.cv),BitmapFactory.decodeResource(getResources(), R.drawable.uv)};
	Bitmap cyan[] = {BitmapFactory.decodeResource(getResources(), R.drawable.cc),BitmapFactory.decodeResource(getResources(), R.drawable.uc)};
	Bitmap bEvaluar = BitmapFactory.decodeResource(getResources(), R.drawable.evaluar);	
	Bitmap bSiguiente = BitmapFactory.decodeResource(getResources(), R.drawable.siguiente);
	Bitmap bEvaluarD = BitmapFactory.decodeResource(getResources(), R.drawable.evaluar2);	
	Bitmap bSiguienteD = BitmapFactory.decodeResource(getResources(), R.drawable.siguiente2);
	Bitmap fondojuego = BitmapFactory.decodeResource(getResources(), R.drawable.fondo_juego);
	Bitmap incorrecto = BitmapFactory.decodeResource(getResources(), R.drawable.incorrecto);
	Bitmap correcto = BitmapFactory.decodeResource(getResources(), R.drawable.correcto);
	Bitmap potenciasBitmap[] = {BitmapFactory.decodeResource(getResources(), R.drawable.p128),BitmapFactory.decodeResource(getResources(), R.drawable.p64),
			BitmapFactory.decodeResource(getResources(), R.drawable.p32), BitmapFactory.decodeResource(getResources(), R.drawable.p16),
			BitmapFactory.decodeResource(getResources(), R.drawable.p8), BitmapFactory.decodeResource(getResources(), R.drawable.p4),
			BitmapFactory.decodeResource(getResources(), R.drawable.p2), BitmapFactory.decodeResource(getResources(), R.drawable.p1)};
	Bitmap dashboard1 = BitmapFactory.decodeResource(getResources(), R.drawable.dashboard);
	Bitmap dashboard2 = BitmapFactory.decodeResource(getResources(), R.drawable.dashboard2);
	Bitmap bIniciar = BitmapFactory.decodeResource(getResources(), R.drawable.iniciar);
	Bitmap bIniciar2 = BitmapFactory.decodeResource(getResources(), R.drawable.iniciar2);
	Bitmap or = BitmapFactory.decodeResource(getResources(), R.drawable.or);
	Bitmap and = BitmapFactory.decodeResource(getResources(), R.drawable.and);
	Bitmap xor = BitmapFactory.decodeResource(getResources(), R.drawable.xor);
	Bitmap x1 = BitmapFactory.decodeResource(getResources(), R.drawable.x);
	Bitmap x2 = BitmapFactory.decodeResource(getResources(), R.drawable.x2);
	
	int sTap = R.raw.tap;
	int sError = R.raw.error;
	int sCambioNivel = R.raw.cambio_de_nivel;
	int sResuelto = R.raw.resuelto;
	int sInicio = R.raw.inicio;
	HashMap<Integer, Integer> soundPoolMap;
	SoundPool soundPool;

	boolean iniciado = false;
	boolean presionadoE = false;
	boolean presionadoS = false;
	boolean presionadoI = false;
	boolean presionadoX = false;
	//private LinkedList<Integer> repetidos = new LinkedList<Integer>();
	private LinkedList<Integer> niveles = new LinkedList<Integer>();
		
	boolean ayudaTiempo = false;
	boolean ayuda = false;
	boolean siguiente = false;
	boolean sonidoInicio = true;

	int opCBooleanas = 0; // número de operaciones correctas booleanas.

	// generador de números
	Binarios binarios = new Binarios(nBinarioRes);
	/*--------*/
	Binarios binarios2 = new Binarios(nBinarioRes);
	
	/**
	 * @brief Constructor de la clase Conversiones
	 * @param context
	 * @param jugador	Variable que almacenará la información del jugador. 
	 */
	public Conversiones(Context context, Jugador jugador)//, AttributeSet attributeSet)

	{

		super(context);//, attributeSet);
		
		inicializarSonido(context);
		
		this.jugador = jugador;
		puntos = jugador.puntos;
		nivel = jugador.nivel;
		
		paint = new Paint();

		paint.setAntiAlias(true);

		// to make it focusable so that it will receive touch events properly
		setFocusable(true);

		// agregar el touchListener
		this.setOnTouchListener(this); 

		iniciado = false;
	}

	private void inicializarSonido(Context context){
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>(4);    
		soundPoolMap.put( sTap, soundPool.load(context, R.raw.tap, 1) );
		soundPoolMap.put( sError, soundPool.load(context, R.raw.error, 2) );
		soundPoolMap.put( sCambioNivel, soundPool.load(context, R.raw.cambio_de_nivel, 3) );
		soundPoolMap.put( sResuelto, soundPool.load(context, R.raw.resuelto, 4) );
		soundPoolMap.put( sInicio, soundPool.load(context, R.raw.inicio, 5) );
	}
	
	private void ejecutarSonido(int id){
		float volumen = (float) 0.5;
		soundPool.play(soundPoolMap.get(id), volumen, volumen, 1, 0, 1f);
	}
	
	private void iniciar(){
		if(nivel<8){
			generarBinarios();
		}else{
			generarBinariosOp();
		}
		
		//iniciar Thread de tiempo
		tiempo.start();
		//tiempo.iniciar();
	}
	
	/**
	 * @brief Método que obtiene el valor binario y decimal de la instancia de la clase Binario
	 */
	private void generarBinarios(){
		nBinarioRes = binarios.generarBinario(nivel);
		nDecimal = binarios.obtenerDecimal();
	}
	
	/**
	 * @brief Método que obtiene el valor binario y decimal de la instancia de la clase Binario
	 */
	private void generarBinariosOp(){
		nBinario = binarios.generarBinario(5);
		nBinario2 = binarios2.generarBinario(4);
		obtenerResultado();
	}
	
	/**
	 * @brief Obtiene el resultado esperado de la operación booleana.
	 */
	private void obtenerResultado(){
		switch(tipoOperacion){
			case AND:
				for(int i=0;i<8;i++){
					if(nBinario[i].equals(nBinario2[i]) && nBinario2[i].equals("1")){
						nBinarioRes[i]="1";
					}else{
						nBinarioRes[i]="0";
					}
				}
				break;
			case OR:
				for(int i=0;i<8;i++){
					if(nBinario[i].equals("1") || nBinario2[i].equals("1")){
						nBinarioRes[i]="1";
					}else{
						nBinarioRes[i]="0";
					}
				}
				break;
			case XOR:
				for(int i=0;i<8;i++){
					if((nBinario[i].equals("1") || nBinario2[i].equals("1")) && !nBinario[i].equals(nBinario2[i])){
						nBinarioRes[i]="1";
					}else{
						nBinarioRes[i]="0";
					}
				}
				break;
		}
		//resBooleano = nBinarioRes;
	}
	
	/**
	 * Función que valida si la respuesta fue correcta.
	 * @return Verdadero o falso.
	 */
	public boolean validarRespuesta(){
		return Arrays.equals(resBooleano, nBinarioRes);
	}
	
	/**
	 * @brief Método que dibujará en pantalla.
	 * @param Canvas
	 */
	public void onDraw(Canvas canvas) {
		
		canvas.drawBitmap(fondojuego, 0, 0, paint);
		
		if(sonidoInicio){
			ejecutarSonido(R.raw.resuelto);
			sonidoInicio = false;
		}
		
		if(nivel<8){
			// Número decimal
			paint.setTextSize(27);
			paint.setColor(Color.YELLOW);
			canvas.drawText("Convierte a binario el siguente número decimal: ", 50, 90, paint);//+nDecimal+" a binario", 50, 90, paint);
			paint.setColor(Color.MAGENTA);
			paint.setTextSize(35);
			canvas.drawText(nDecimal, 635, 90, paint); 	
	
			int pos = 0;
			for (int i = 50; i < 685; i += 80) {
	
				int num;
				
				if(nBinario[pos].equals("0")){
					num = 0;
				}else{
					num = 1;
				}
				
				// posiciones
				switch(color_salida){
				case NORMAL:
					canvas.drawBitmap(cyan[num], i+5, 220, paint);
					break;
				case AYUDA:
					if(nBinarioRes[pos].equals(nBinario[pos])){
						canvas.drawBitmap(cyan[num], i+5, 220, paint); 
					}else{
						canvas.drawBitmap(rojo[num], i+5, 220, paint);
					}
					break;
				case CORRECTO:
						canvas.drawBitmap(verde[num], i+5, 220, paint);
					break;
				}
				
				// potencias
				canvas.drawBitmap(potenciasBitmap[pos], i+5, 185, paint);
				
				pos++;		
				
			}
		}else{
			//Operaciones Booleanas
			paint.setTextSize(27);
			paint.setColor(Color.YELLOW);
			canvas.drawText("Realiza la siguiente operación booleana: ", 50, 70, paint);//+nDecimal+" a binario", 50, 90, paint);
			paint.setColor(Color.MAGENTA);
			paint.setTextSize(35);
			
			switch(tipoOperacion){
				case AND:
					op="AND";
					break;
				case OR:
					op="OR";
					break;
				case XOR:
					op="XOR";
					break;
			}
					
			canvas.drawText(op, 540, 70, paint); 


			int pos = 0;
			for (int i = 50; i < 685; i += 80) {
				
				paint.setTextSize(30);
				// posiciones
				paint.setStyle(Paint.Style.STROKE);
				
				paint.setColor(Color.CYAN);
				canvas.drawBitmap(cyan[Integer.parseInt(nBinario[pos])], i+5, 100, paint);
				canvas.drawBitmap(cyan[Integer.parseInt(nBinario2[pos])], i+5, 190, paint);
				
				switch(color_salida){
					case NORMAL:
						canvas.drawBitmap(cyan[Integer.parseInt(resBooleano[pos])], i+5, 290, paint);
					break;
					case AYUDA:
						if(resBooleano[pos].equals(nBinarioRes[pos])){
							canvas.drawBitmap(cyan[Integer.parseInt(resBooleano[pos])], i+5, 290, paint);
						}else{
							canvas.drawBitmap(rojo[Integer.parseInt(resBooleano[pos])], i+5, 290, paint);
						}
						break;
					case CORRECTO:
						canvas.drawBitmap(verde[Integer.parseInt(resBooleano[pos])], i+5, 290, paint);
						break;
				}
				
				paint.setColor(Color.rgb(255, 165, 0));
				canvas.drawLine(55, 283, 695, 283, paint);
				

				// número binario
				paint.setColor(Color.CYAN);
				paint.setTextSize(90);
				pos++;
			}
		}
		
		
	
		//Dashboards
		canvas.drawBitmap(dashboard1, 745, 20,paint);
		canvas.drawBitmap(dashboard2, 745, 142,paint);
		canvas.drawBitmap(dashboard1, 745, 364,paint);

		
		// X cerrar
		if(!presionadoX){
			canvas.drawBitmap(x1, 984, 2,paint);
		}else{
			canvas.drawBitmap(x2, 984, 2,paint);
		}
		
		// botón Iniciar
		if(!iniciado && !presionadoI){
			canvas.drawBitmap(bIniciar, 50, 400, paint);
		}else{
			if(!iniciado && presionadoI){
				canvas.drawBitmap(bIniciar2, 50, 400, paint);
			}
		}
		
		// botón Evaluar
		if(!presionadoE && iniciado){
			canvas.drawBitmap(bEvaluar, 50, 400, paint);
		}else{
			if(iniciado){
				canvas.drawBitmap(bEvaluarD, 50, 400, paint);
			}
		}

		// botón Siguiente
		if(siguiente && !ayuda){
			if(!presionadoS){
				canvas.drawBitmap(bSiguiente, 230, 400, paint);
			}else{
				canvas.drawBitmap(bSiguienteD, 230, 400, paint);
			}
		}
		

		// Ayuda
		if (ayuda) {
			if(nivel<8){
				String cadAyuda = "";
				paint.setTextSize(25);
				paint.setColor(Color.RED);
				paint.setStyle(Paint.Style.FILL);
				if(!ayudaTiempo){
					canvas.drawText("Cambia el valor de las casillas marcadas con rojo.", 50, 150, paint);
				}else{
					canvas.drawText("Se acabó el tiempo, cambia el valor de las casillas",50,135,paint);
					canvas.drawText("marcadas con rojo.", 50, 160, paint);
				}
				
				canvas.drawBitmap(incorrecto, 799, 285 , paint);
				
				for (int i = 0; i < 8; i++) {
					if (nBinarioRes[i].equals("1")) {
						cadAyuda += "(1*" + potencias[i] + ")";
					} else {
						cadAyuda += "(0*" + potencias[i] + ")";
					}
					if (i < 7)
						cadAyuda += "+";
				}
				canvas.drawText(cadAyuda.replaceAll(" ", ""), 50, 355, paint);
			}else{
				//Operaciones Booleanas
				canvas.drawBitmap(incorrecto, 799, 285 , paint);
			    
				switch(tipoOperacion){
					case OR: 
						canvas.drawBitmap(or, 610, 400 , paint);
					break;
					case AND:
						canvas.drawBitmap(and, 610, 400 , paint);
						break;
					case XOR:
						canvas.drawBitmap(xor, 610, 400 , paint);
						break;
				}
				
				paint.setTextSize(21);
				paint.setColor(Color.RED);
				paint.setStyle(Paint.Style.FILL);
				if(!ayudaTiempo){
					canvas.drawText("Cambia el valor de las casillas marcadas", 220, 420, paint);
					canvas.drawText("con rojo según la tabla de verdad.", 220, 440, paint);
				}else{
					canvas.drawText("Se acabó el tiempo, cambia el valor de ", 220, 415, paint);
					canvas.drawText("las casillas marcadas con rojo según ", 220, 435, paint);
					canvas.drawText("la tabla de verdad.",220,455,paint);
				}
			}
		}
		
		if (siguiente){
			canvas.drawBitmap(correcto, 799, 285 , paint);
		}

		
		//Información
		paint.setTextSize(25);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		
		canvas.drawText("Nivel", 760, 60, paint);		
		paint.setTextSize(45);
		canvas.drawText(""+nivel, 851, 110, paint);

		paint.setTextSize(25);
		canvas.drawText("Tiempo", 760, 185, paint);
		
		paint.setTextSize(45);
		canvas.drawText(stringTiempo(segundos), 801, 260, paint);

		paint.setTextSize(25);
		canvas.drawText("Jugador: "+jugador.jugador, 760, 400, paint);
		canvas.drawText("Puntaje: "+puntos, 760, 450, paint);


	}

	/**
	 * @brief Función que convertirá los segundos enteros a string.
	 * @param segundos	Variable int que almacena los segundos de resolución del ejercicio
	 * @return Variable String que se dibujará en pantalla
	 */
	private String stringTiempo(int segundos){
		String tiempo="";
		if(segundos<10){
			tiempo="00:0"+segundos;
		}else{
			tiempo="00:"+segundos;
		}
		return tiempo;
	}
	
	/**
	 * @brief Función que evalua si un bit es 0 o 1 al haber un evento de touch
	 * @param val	variable int 0 o 1
	 * @return Si el parámetro es 1, retorna 0, y si es 0 retorna 1
	 */
	private String digito(int val) {
		return "" + Math.abs(Math.abs(val) - 1);
	}
	
	
	/**
	 * @brief Función que obtiene el evento del touch
	 * @param view
	 * @param event
	 */
	public boolean onTouch(View view, MotionEvent event)

	{
		x = (int) event.getX();
		y = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			presionadoE = false;
			presionadoS = false;
			presionadoI = false;
			presionadoX = false;
			
			if(nivel<8){
				// Coordenadas de digitos binarios
				if (y > 220 && y < 300) {
					if (x > 55 && x < 130) {
						nBinario[0] = digito(Integer.parseInt(nBinario[0]));
						ejecutarSonido(R.raw.tap);
					} else {
						if (x > 135 && x < 210) {
							nBinario[1] = digito(Integer.parseInt(nBinario[1]));
							ejecutarSonido(R.raw.tap);
						} else {
							if (x > 215 && x < 290) {
								nBinario[2] = digito(Integer.parseInt(nBinario[2]));
								ejecutarSonido(R.raw.tap);
							} else {
								if (x > 295 && x < 370) {
									nBinario[3] = digito(Integer
											.parseInt(nBinario[3]));
									ejecutarSonido(R.raw.tap);
								} else {
									if (x > 375 && x < 450) {
										nBinario[4] = digito(Integer
												.parseInt(nBinario[4]));
										ejecutarSonido(R.raw.tap);
									} else {
										if (x > 455 && x < 530) {
											nBinario[5] = digito(Integer
													.parseInt(nBinario[5]));
											ejecutarSonido(R.raw.tap);
										} else {
											if (x > 535 && x < 610) {
												nBinario[6] = digito(Integer
														.parseInt(nBinario[6]));
												ejecutarSonido(R.raw.tap);
											} else {
												if (x > 615 && x < 690) {
													nBinario[7] = digito(Integer
															.parseInt(nBinario[7]));
													ejecutarSonido(R.raw.tap);
												}
											}
										}
									}
								}
							}
						}
					}
				}
	
				// botón de evaluar
				if (x > 50 && y > 400 && x < 200 && y < 450 && iniciado) {
					ejecutarSonido(R.raw.tap);
					if (binarios.validarRespuesta(nBinario)) {
						int mod;
						if(segundos<=30 && segundos>25){
							mod=1;
						}else{
							if(segundos<=25 && segundos>20){
								mod=2;
							}else{
								if(segundos<=20 && segundos>15){
									mod=3;
								}else{
									if(segundos<=15 && segundos>10){
										mod=4;
									}else{
										if(segundos<=10 && segundos>5){
											mod=5;
										}else{
											mod=6;
										}
									}
								}
							}
						}
						
						if(!ayuda && color_salida!=CORRECTO) puntos += (nivel * mod);
						siguiente = true;
						ayuda = false;
						ayudaTiempo = false;
						color_salida=CORRECTO;
						ejecutarSonido(R.raw.resuelto);
						tiempo.detener();
						actualizarJugador();
					} else {
						tiempo.detener();
						ejecutarSonido(R.raw.error);
						ayuda = true;
						color_salida=AYUDA;
					}
				}else{
					// botón iniciar
					if (x > 50 && y > 400 && x < 200 && y < 450 && !iniciado) {
						ejecutarSonido(R.raw.tap);
						iniciado = true;
						iniciar();
					}
				}
				
				// botón siguiente
				if (x > 230 && y > 400 && x < 380 && y < 450 && siguiente && !ayuda) {
					ejecutarSonido(R.raw.tap);
					if(puntos>=nivel*100 && puntos<=(nivel+1)*100){
						nivel++;
						if(!niveles.contains(nivel)){
							ejecutarSonido(R.raw.cambio_de_nivel);
							niveles.add(nivel);
							//repetidos.clear();
						}else{
							nivel--;
						}
					}
					
					if(nivel<8){
						binarios = new Binarios(nBinarioRes);
						nBinarioRes = binarios.generarBinario(nivel);
						nDecimal = binarios.obtenerDecimal();
						for (int i = 0; i < 8; i++) {
							nBinario[i] = "0";
						}
						color_salida=NORMAL;
						siguiente = false;
					}else{
						binarios = new Binarios(nBinarioRes);
						binarios2 = new Binarios(nBinarioRes);
						for (int i = 0; i < 8; i++) {
							resBooleano[i] = "0";
						}
						nBinario = binarios.generarBinario(5);
						nBinario2 = binarios2.generarBinario(4);
						
						color_salida=NORMAL;
						siguiente = false;
						
						tipoOperacion = new Random().nextInt(3);
						
						obtenerResultado();
					}
								
					tiempo = new Tiempo();
					tiempo.start();
					//tiempo.iniciar();
				}
	
				invalidate();
			}else{
				// Coordenadas de digitos binarios (respuesta)
				if (y > 290 && y < 370) {
					if (x > 55 && x < 130) {
						resBooleano[0] = digito(Integer.parseInt(resBooleano[0]));
						ejecutarSonido(R.raw.tap);
					} else {
						if (x > 135 && x < 210) {
							resBooleano[1] = digito(Integer.parseInt(resBooleano[1]));
							ejecutarSonido(R.raw.tap);
						} else {
							if (x > 215 && x < 290) {
								resBooleano[2] = digito(Integer.parseInt(resBooleano[2]));
								ejecutarSonido(R.raw.tap);
							} else {
								if (x > 295 && x < 370) {
									resBooleano[3] = digito(Integer.parseInt(resBooleano[3]));
									ejecutarSonido(R.raw.tap);
								} else {
									if (x > 375 && x < 450) {
										resBooleano[4] = digito(Integer.parseInt(resBooleano[4]));
										ejecutarSonido(R.raw.tap);
									} else {
										if (x > 455 && x < 530) {
											resBooleano[5] = digito(Integer.parseInt(resBooleano[5]));
											ejecutarSonido(R.raw.tap);
										} else {
											if (x > 535 && x < 610) {
												resBooleano[6] = digito(Integer.parseInt(resBooleano[6]));
												ejecutarSonido(R.raw.tap);
											} else {
												if (x > 615 && x < 690) {
													resBooleano[7] = digito(Integer.parseInt(resBooleano[7]));
													ejecutarSonido(R.raw.tap);
												}
											}
										}
									}
								}
							}
						}
					}
				}

				// botón de evaluar
				if (x > 50 && y > 400 && x < 200 && y < 450 && iniciado) {
					ejecutarSonido(R.raw.tap);
					if (validarRespuesta()) {
						if(!ayuda  && color_salida!=CORRECTO) puntos += 20;
						siguiente = true;
						ayuda = false;
						ayudaTiempo =false;
						ejecutarSonido(R.raw.resuelto);
						opCBooleanas ++;
						color_salida=CORRECTO;
						tiempo.detener();
						
						if(opCBooleanas==10){
							AlertDialog.Builder mensaje = new AlertDialog.Builder(this.getContext());
							mensaje.setTitle("Aviso");
							mensaje.setMessage("Has completado los ejercicios de números binarios, ¿Deseas iniciar de nuevo?");
							mensaje.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
							      public void onClick(DialogInterface dialog, int which) {
							    	  nivel=1;
									  puntos=0;
							      } });  
							mensaje.setNegativeButton("No", new DialogInterface.OnClickListener() {
							      public void onClick(DialogInterface dialog, int which) {
							    	  opCBooleanas=0;
							      } });
							
							AlertDialog m = mensaje.create();
							
							m.show();
						}
						
						actualizarJugador();
					} else {
						ejecutarSonido(R.raw.error);
						ayuda = true;
						color_salida=AYUDA;
						tiempo.detener();
					}
				}else{
					// botón iniciar
					if (x > 50 && y > 400 && x < 200 && y < 450 && !iniciado) {
						ejecutarSonido(R.raw.tap);
						iniciado = true;
						iniciar();
					}
				}
				
				// botón siguiente
				if (x > 230 && y > 400 && x < 380 && y < 450 && siguiente && !ayuda) {
					ejecutarSonido(R.raw.tap);
					binarios = new Binarios(nBinarioRes);
					binarios2 = new Binarios(nBinarioRes);
					for (int i = 0; i < 8; i++) {
						resBooleano[i] = "0";
					}
					nBinario = binarios.generarBinario(5);
					nBinario2 = binarios2.generarBinario(4);
					
					color_salida=NORMAL;
					siguiente = false;
					
					if(puntos==nivel*100){
						nivel++;
						//repetidos.clear();
					}
					
					tipoOperacion = new Random().nextInt(3);
					
					obtenerResultado();
					
					tiempo = new Tiempo();
					tiempo.start();
					//tiempo.iniciar();
					
				}

				invalidate();
			}
			// botón salir
			if(x>984 && x<1024 && y>1 && y<40){
				ejecutarSonido(R.raw.tap);
				System.exit(0);
			}
			/*----*/
		}else{
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				//botón evaluar
				if (x > 50 && y > 400 && x < 200 && y < 450 && iniciado) {
					presionadoE = true;
					invalidate();
				}else{
					if (x > 50 && y > 400 && x < 200 && y < 450 && !iniciado) {
						presionadoI = true;
						invalidate();
					}
				}
				// botón siguiente
				if (x > 230 && y > 400 && x < 380 && y < 450 && siguiente) {
					presionadoS = true;
					invalidate();
				}
				
				// botón X
				if(x>984 && x<1024 && y>1 && y<40){
					presionadoX = true;
					invalidate();
				}
			}
		}
		return true;
	}
	
	/**
	 * @brief Método que actualiza la información del nivel, puntos, etc. del jugador al finalizar cada ejercicio.
	 */
	public void actualizarJugador(){
    	AyudaSQLite asql = new AyudaSQLite(super.getContext(),"DBJugador",null,1);
    	SQLiteDatabase db = asql.getWritableDatabase();    	
    	if(db!=null){	
			ContentValues args = new ContentValues();
			String filtro = "id="+jugador.id;
			args.put("puntos", puntos);
			args.put("nivel", nivel);
			db.update("Jugador", args, filtro, null);
    		db.close();
    	}
    }
	
	/**
	 * 
	 * @brief Clase que ejecuta el hilo que maneja el tiempo de resolución de problemas.
	 *
	 */
	class Tiempo extends Thread{
		boolean ejecutando = false;
		
		public void run(){
			iniciar();
			while(ejecutando && segundos!=0){
				segundos--;
				postInvalidate();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(segundos==0 && color_salida!=CORRECTO){
				ejecutarSonido(R.raw.error);
				ayuda = true;
				ayudaTiempo = true;
				color_salida=AYUDA;
				postInvalidate();
			}
		}
		
		public void iniciar(){
			ejecutando = true;
			if(nivel<8){
				segundos = (nivel * 5)+1;
			}else{
				segundos = 16;
			}
		}
		
		public void detener(){
			ejecutando = false;
		}
		
	}

}
