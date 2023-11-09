package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.db.DatabaseSGA;
import com.example.rsu_itcjapp.datos.Usuario;
import com.example.rsu_itcjapp.listView.DatosItemReporte;
import com.example.rsu_itcjapp.listView.ReportesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OpcionesCoordActivity extends AppCompatActivity {

    private ReportesAdapter adapterReporteBim;
    private ArrayList<DatosItemReporte> datosItemReportesBim;

    private DatabaseSGA databaseSGA;
    private Calendar fechaActual;
    private HashMap<String, String> fechaServicio;
    private SimpleDateFormat formatoFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutSeleccionado = (Integer) getIntent().getExtras().get(DatosSistema.LAYOUT);
        setContentView(layoutSeleccionado);

        databaseSGA = new DatabaseSGA(OpcionesCoordActivity.this);
        fechaServicio = new HashMap<>();

        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechaActual = Calendar.getInstance();

        seleccionarLayout(layoutSeleccionado);
    }

    private void seleccionarLayout(int layout){
        switch(layout) {
            case DatosSistema.Layouts.GENERARAVISO:
                generarAviso();
                break;
            case DatosSistema.Layouts.ALUMNOSSERVICIO:
                mostrarReporteBimestral();
                break;
            case DatosSistema.Layouts.VERPERFIL:
                mostrarDatosCuenta();
                break;
        }
    }

    private void generarAviso() {
        final String path = "aviso/";

        TextInputEditText txtTitulo = (TextInputEditText) findViewById(R.id.tie_titulo_aviso);
        TextInputEditText txtCorreo = (TextInputEditText) findViewById(R.id.tie_correo_aviso);
        TextInputEditText txtInformacion = (TextInputEditText) findViewById(R.id.tie_informacion_aviso);

        Button btnGuardarAviso = (Button) findViewById(R.id.btn_guardar_aviso);

        btnGuardarAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tituloAviso = txtTitulo.getText().toString().trim();
                String correo = txtCorreo.getText().toString().trim();
                String info = txtInformacion.getText().toString().trim();

                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (tituloAviso.isEmpty()) {
                   txtTitulo.setError("Campo vacío");
                   return;
                }

                if (correo.isEmpty()) {
                    txtCorreo.setError("Campo vacío");
                    return;
                }

                if (info.isEmpty()) {
                    txtInformacion.setError("Campo vacío");
                    return;
                }

                HashMap<String, String> aviso = new HashMap<>();

                aviso.put("correoDeContacto", correo);
                aviso.put("informacion", info);
                aviso.put("titulo", tituloAviso);

                databaseSGA.getDbRef().child(path).setValue(aviso)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(OpcionesCoordActivity.this,
                                            "Aviso guardado correctamente.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NotNull Exception e) {
                                Toast.makeText(OpcionesCoordActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                txtCorreo.setText("");
                txtInformacion.setText("");
                txtTitulo.setText("");
            }
        });
    }

    private int calcularReporte(HashMap<String, String> fechaServicio) {
        int rep = 0;
        try {
            final String fechaServicioAlum = fechaServicio.get("dia")+"/"+fechaServicio.get("mes")
                    +"/"+fechaServicio.get("anho");

            Calendar fechaServicioInicio = Calendar.getInstance();

            Date dateServicio = formatoFecha.parse(fechaServicioAlum);
            fechaServicioInicio.setTime(dateServicio);

            int anhos = fechaActual.get(Calendar.YEAR) - fechaServicioInicio.get(Calendar.YEAR);
            int dias = fechaActual.get(Calendar.DAY_OF_YEAR) - fechaServicioInicio.get(Calendar.DAY_OF_YEAR);

            if (anhos > 0 || dias > 240) return rep;

            if (dias >= 180) rep = 3;
            else if (dias >= 120) rep = 2;
            else if (dias >= 60) rep = 1;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return rep;
    }

    private void mostrarReporteBimestral () {

        final String reporte = "Reporte ";

        ListView listReportesBim = (ListView) findViewById(R.id.list_reportes_bimestral);

        datosItemReportesBim = new ArrayList<>();

        databaseSGA.getDbRef().child(DatosSistema.USUARIOS)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();
                datosItemReportesBim.clear();
                for (DataSnapshot dataSnapshot : snapshotIterable) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    if(usuario != null
                            && usuario.getTipo().equals(DatosSistema.USUARIO_ALUMNO)) {

                        Alumno alumno = dataSnapshot.getValue(Alumno.class);
                        fechaServicio = alumno.getResidenciasInicio();

                        if (fechaServicio != null) {

                            int rep = calcularReporte(fechaServicio);

                            if (rep > 0) {
                                String nombreCompleto = alumno.getNombre() + " " +
                                        alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno();

                                DatosItemReporte datosItemReporte = new DatosItemReporte(nombreCompleto,
                                        String.valueOf(alumno.getMatricula()), alumno.getArea(),
                                        reporte + rep);

                                datosItemReportesBim.add(datosItemReporte);
                            }
                        }
                    }
                    adapterReporteBim.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                Toast.makeText(OpcionesCoordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapterReporteBim = new ReportesAdapter(getApplicationContext(), datosItemReportesBim);
        listReportesBim.setAdapter(adapterReporteBim);
    }

    private void mostrarDatosCuenta(){
        Usuario docente = DatosSistema.DatosUsuario.usuario;

        ((TextInputLayout) findViewById(R.id.til_carrera_inf)).setVisibility(View.GONE);
        ((TextInputLayout) findViewById(R.id.til_matricula_inf)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.layout_fechas)).setVisibility(View.GONE);

        ((TextInputEditText) findViewById(R.id.txt_nombre_inf)).setText(docente.getNombre());
        ((TextInputEditText) findViewById(R.id.txt_apellidoPaterno_inf)).setText(docente.getApellidoPaterno());
        ((TextInputEditText) findViewById(R.id.txt_apellidoMaterno_inf)).setText(docente.getApellidoMaterno());
        ((TextInputEditText) findViewById(R.id.txt_area_inf)).setText(docente.getArea());
        ((TextInputEditText) findViewById(R.id.txt_correo_inf)).setText(databaseSGA.getUser().getEmail());

    }

}