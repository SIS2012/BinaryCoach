package usac.binarycoach;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

/**
 * @brief Actividad que mostrará la pantalla de registro.
 *
 */
public class Registro extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);       
        jugador = (EditText) findViewById(R.id.eJugador);
        jugador.requestFocus();
        password = (EditText) findViewById(R.id.ePassword);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Nuevo Juagador - Entrenador Binario");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_registro, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * @brief Método que captura el evento del botón registrar.
     * @param v
     */
    public void clickRegistrar(View v){
    	try{
    		ingresarJugador();
    		verificarJugador();
    	}catch(Exception e){
    		new AlertDialog.Builder(this).setTitle("Aviso").setMessage("El jugador ya existe.").setNeutralButton("Aceptar", null).show();
    	}
    }
    
    /**
     * @brief Método que ingresa los datos del usuario a la base de datso SQLite.
     */
    public void ingresarJugador(){
    	AyudaSQLite asql = new AyudaSQLite(this,"DBJugador",null,1);
    	SQLiteDatabase db = asql.getWritableDatabase();
    	
    	if(db!=null){
    		if(!jugador.getText().toString().equals("") && !password.getText().toString().equals("")){
    			ContentValues registro = new ContentValues();
    			registro.put("jugador", jugador.getText().toString());
    			registro.put("password", password.getText().toString());
    			registro.put("puntos", 0);
    			registro.put("nivel", 1);
    			db.insertOrThrow("Jugador", null, registro);
    		}else{
    			new AlertDialog.Builder(this).setTitle("Aviso").setMessage("Debe ingresar un jugador y una contraseña.").setNeutralButton("Aceptar", null).show();
    		}
    		db.close();
    	}
    	//finish();
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
    				Intent juego = new Intent(Registro.this,Juego.class);
    				juego.putExtra("info", info_jugador);
    		    	startActivity(juego);
    		    	finish();
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
