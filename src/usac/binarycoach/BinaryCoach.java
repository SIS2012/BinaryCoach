package usac.binarycoach;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;

/**
 * @brief Actividad principal, que permitirá al usuario ingresar a la aplicación, o registrarse.
 *
 */
public class BinaryCoach extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_coach);
        //setContentView(new Conversiones(this, new Jugador(1,"abcde","",0,1)));
        //setContentView(new Operaciones(this, new Jugador(1,"abcde","",5,6)));
        jugador = (EditText) findViewById(R.id.eJugador);
        jugador.requestFocus();
        password = (EditText) findViewById(R.id.ePassword);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Entrenador Binario");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_binary_coach, menu);
        return true;
    }
    
    /**
     * @brief Método que captura el evento del botón iniciar.
     * @param v
     */
    public void clickIniciar(View v){
    	verificarJugador();
    }
    
    /**
     * @brief Método que captura el evento del botón salir.
     * @param v
     */
    public void clickSalir(View v){
    	System.exit(0);
    }
    
    /**
     * @brief Método que captura el evento del botón registrar.
     * @param v
     */
    public void clickRegistrar(View v){
    	Intent registro = new Intent(BinaryCoach.this,Registro.class);
    	startActivity(registro);
    }
       
    /**
     * @brief Método que verifica que el jugador se encuentre registrado, para poder iniciar la aplicación.
     */
    public void verificarJugador(){
    	AyudaSQLite asql = new AyudaSQLite(this,"DBJugador",null,1);
    	SQLiteDatabase db = asql.getWritableDatabase();
    	
    	if(db!=null){
    		String args[] = {jugador.getText().toString()};
    		Cursor cursor = db.rawQuery("select * from Jugador where jugador=?",args);
    		if(cursor.moveToFirst()){
    			info_jugador = new Jugador(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
    					cursor.getInt(3),cursor.getInt(4));
    			if(info_jugador.password.equals(password.getText().toString())){
    				Intent juego = new Intent(BinaryCoach.this,Juego.class);
    				juego.putExtra("info", info_jugador);
    		    	startActivity(juego);
    			}else{
    				new AlertDialog.Builder(this).setTitle("Contraseña incorrecta").setMessage("Ingrese su contraseña nuevamente, o regístrese.").setNeutralButton("Aceptar", null).show();  
    			}
    		}else{
    		    new AlertDialog.Builder(this).setTitle("Jugador inexistente").setMessage("El jugador no existe, por favor regístrese.").setNeutralButton("Aceptar", null).show();  
    		}
    		db.close();
    	}
    }
    
    EditText jugador;
    EditText password;
    Jugador info_jugador;
}
