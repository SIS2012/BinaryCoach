package usac.binarycoach;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @brief Clase que ayuda en la creación y administración de la base de datos SQLite
 *
 */
public class AyudaSQLite extends SQLiteOpenHelper {

	String tablaJugador = "CREATE TABLE Jugador (id INTEGER PRIMARY KEY AUTOINCREMENT, jugador TEXT UNIQUE, password TEXT, puntos INTEGER, nivel INTEGER)";
	 	
	public AyudaSQLite(Context contexto, String nombre,
            CursorFactory factory, int version) {
		super(contexto, nombre, factory, version);
}
	/**
	 * @brief Crea la base de datos si no existe
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tablaJugador);
	}
	
	/**
	 * @brief Actualiza la base de datos si existen cambios.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Jugador"); 
		db.execSQL(tablaJugador);
	}
	
}
