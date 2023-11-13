package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.db.DatabaseSGA;
import com.example.rsu_itcjapp.listView.DatosItemMenu;
import com.example.rsu_itcjapp.listView.MenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private TextView twHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ListView listView = (ListView) findViewById(R.id.list_view);
        twHeader = (TextView) findViewById(R.id.tw_header);

        if (DatosSistema.DatosUsuario.alumno != null) {
            crearOpcionesAlumno(listView);
        } else if (DatosSistema.DatosUsuario.usuario != null) {
            crearOpcionesCoordinador(listView);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(DatosSistema.DatosUsuario.alumno == null && DatosSistema.DatosUsuario.usuario == null) {
            Intent pantallaPrincipal = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(pantallaPrincipal);
            finish();
        }
    }


    public void crearOpcionesAlumno(ListView listAlumno) {
        ArrayList<DatosItemMenu> alumnoAdapter = new ArrayList<>();

        twHeader.setText(getResources().getString(R.string.tw_alumno));

        HashMap<String, Integer> layouts = new HashMap<>();
        layouts.put(DatosSistema.REC, DatosSistema.Layouts.RECICLAJE);
        layouts.put(DatosSistema.MPT, DatosSistema.Layouts.MARCADORESPILAS);
        layouts.put(DatosSistema.RSP, DatosSistema.Layouts.RESIDUOSPELIGROSOS);
        layouts.put(DatosSistema.STAR, DatosSistema.Layouts.SISTEMADERIEGO);

        alumnoAdapter.add(new DatosItemMenu(DatosSistema.DatosUsuario.alumno.getArea(),
                R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DatosItemMenu(DatosSistema.EMAIL, R.drawable.ic_baseline_email_24));
        alumnoAdapter.add(new DatosItemMenu(DatosSistema.PERFIL, R.drawable.ic_baseline_account_circle_24));
        alumnoAdapter.add(new DatosItemMenu(DatosSistema.CERRAR_SESION, R.drawable.ic_baseline_exit_to_app_24));

        MenuAdapter listaMenuAdapterAlumno = new MenuAdapter(this, alumnoAdapter);

        AdapterView.OnItemClickListener itemClickListenerAlumno = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int posicion, long id) {
                Integer layout = null;
                boolean cerrarApp = false;

                switch ((int) id) {
                    case 0:
                        layout = layouts.get(DatosSistema.DatosUsuario.alumno.getArea());
                        break;
                    case 1:
                        layout = DatosSistema.Layouts.ENVIARCORREO;
                        break;
                    case 2:
                        layout = DatosSistema.Layouts.VERPERFIL;
                        break;
                    case 3:
                        cerrarApp = true;
                        break;
                }

                if (cerrarApp) {
                    cerrarSesion();
                    return;
                }

                if (layout == null) {
                    layout = DatosSistema.Layouts.RESIDUOSPELIGROSOS;
                }

                Intent opcionesMenu = new Intent(MenuActivity.this, OpcionesAlumnoActivity.class);
                opcionesMenu.putExtra(DatosSistema.LAYOUT, layout);
                startActivity(opcionesMenu);
            }
        };

        listAlumno.setAdapter(listaMenuAdapterAlumno);
        listAlumno.setOnItemClickListener(itemClickListenerAlumno);
    }

    public void crearOpcionesCoordinador(ListView listCoordinador) {
        ArrayList<DatosItemMenu> coordinadorAdapter = new ArrayList<>();

        twHeader.setText(getResources().getString(R.string.tw_coordinador));

        coordinadorAdapter.add(new DatosItemMenu(DatosSistema.AVISO, R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DatosItemMenu(DatosSistema.REPORTE_BIMESTRAL, R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DatosItemMenu(DatosSistema.PERFIL, R.drawable.ic_baseline_account_circle_24));
        coordinadorAdapter.add(new DatosItemMenu(DatosSistema.CERRAR_SESION, R.drawable.ic_baseline_exit_to_app_24));

        MenuAdapter listaMenuAdapterCoordinador = new MenuAdapter(this, coordinadorAdapter);

        AdapterView.OnItemClickListener itemClickListenerCoord = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Integer layout = null;
                boolean cerrarApp = false;

                switch((int) id) {
                    case 0:
                        layout = DatosSistema.Layouts.GENERARAVISO;
                        break;
                    case 1:
                        layout = DatosSistema.Layouts.ALUMNOSSERVICIO;
                        break;
                    case 2:
                        layout = DatosSistema.Layouts.VERPERFIL;
                        break;
                    case 3:
                        cerrarApp = true;
                        break;
                }

                if (cerrarApp) {
                    cerrarSesion();
                    return;
                }

                Intent opcionesMenuCoord = new Intent(MenuActivity.this, OpcionesCoordActivity.class);
                opcionesMenuCoord.putExtra(DatosSistema.LAYOUT, layout);
                startActivity(opcionesMenuCoord);
            }
        };

        listCoordinador.setAdapter(listaMenuAdapterCoordinador);
        listCoordinador.setOnItemClickListener(itemClickListenerCoord);
    }

    public void cerrarSesion() {
        DatabaseSGA databaseSGA = new DatabaseSGA(MenuActivity.this);
        databaseSGA.getAuth().signOut();
        Toast.makeText(MenuActivity.this, "Sesión finalizada.", Toast.LENGTH_SHORT).show();
        Intent pantallaInicio = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(pantallaInicio);
        finish();
    }
}