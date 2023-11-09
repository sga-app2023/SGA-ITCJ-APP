package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.db.DatabaseSGA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private DatabaseSGA databaseSGA;
    private String usuario = "";
    private HashMap<String, String> residenciasInicio;
    private HashMap<String, String> residenciasFin;

    private Button btnRegistroUsuario;
    private TextInputEditText txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtMatricula,
                                txtCorreo, txtPassword, txtFechaInicio, txtFechaFinal;
    private AutoCompleteTextView actCarreras, actAreasTrabajo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        usuario = (String) getIntent().getExtras().get(DatosSistema.USUARIO);

        databaseSGA = new DatabaseSGA(RegistroUsuarioActivity.this);
        residenciasInicio = new HashMap<>();
        residenciasFin = new HashMap<>();

        txtNombre = (TextInputEditText) findViewById(R.id.txt_nombre_usuario_reg);
        txtApellidoPaterno = (TextInputEditText) findViewById(R.id.txt_apellido_paterno_reg);
        txtApellidoMaterno = (TextInputEditText) findViewById(R.id.txt_apellido_materno_reg);
        actAreasTrabajo = (AutoCompleteTextView) findViewById(R.id.act_area_trabajo);
        actCarreras = (AutoCompleteTextView) findViewById(R.id.act_carrera_reg);
        txtMatricula = (TextInputEditText) findViewById(R.id.txt_matricula);
        txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_usuario);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_password_usuario);
        txtFechaInicio = (TextInputEditText) findViewById(R.id.txt_fecha_inicio_reg);
        txtFechaFinal = (TextInputEditText) findViewById(R.id.txt_fecha_final_reg);
        btnRegistroUsuario = (Button) findViewById(R.id.btn_registrar_usuario);

        String [] areasArray = getResources().getStringArray(R.array.areasTrabajo);
        String [] carrerasArray = getResources().getStringArray(R.array.carreras);

        ArrayAdapter<String> areasAdapter =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, areasArray);
        ArrayAdapter<String> carrerasAdapter =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, carrerasArray);

        actAreasTrabajo.setAdapter(areasAdapter);
        actCarreras.setAdapter(carrerasAdapter);

        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                residenciasInicio =
                        ComponentesInterfaz.setDatePicker(txtFechaInicio, RegistroUsuarioActivity.this);
            }
        });

        txtFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                residenciasFin =
                        ComponentesInterfaz.setDatePicker(txtFechaFinal, RegistroUsuarioActivity.this);
            }
        });

        btnRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = txtNombre.getText().toString().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().toString().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().toString().trim();
                String area = actAreasTrabajo.getText().toString();
                String carrera = actCarreras.getText().toString();
                String matricula = txtMatricula.getText().toString().trim();
                String correo = txtCorreo.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String fechaInicio = txtFechaInicio.getText().toString();
                String fechaFin = txtFechaFinal.getText().toString();

                String expresion = "^[\\w+_.-]+@[\\w+.-]+$";
                Pattern pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);

                ocultarTeclado(view);

                if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || area.isEmpty()
                        || carrera.isEmpty() || correo.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegistroUsuarioActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pattern.matcher(correo).matches()) {
                    txtCorreo.setError("Correo no válido");
                    return;
                }

                if (matricula.length() < 8) {
                    txtMatricula.setError("Matricula incorrecta");
                    return;
                }

                if (TextUtils.isEmpty(fechaInicio) || TextUtils.isEmpty(fechaFin)) {
                    Toast.makeText(RegistroUsuarioActivity.this, "Falta capturar fecha inicio/fin",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Alumno alumno = new Alumno(nombre, apellidoPaterno, apellidoMaterno, area, usuario,
                        carrera, Integer.parseInt(matricula), residenciasInicio, residenciasFin);

                registrarUsuario(alumno, correo, password);

                txtNombre.setText("");
                txtApellidoPaterno.setText("");
                txtApellidoMaterno.setText("");
                actAreasTrabajo.setText("");
                actCarreras.setText("");
                txtMatricula.setText("");
                txtCorreo.setText("");
                txtPassword.setText("");
                txtFechaInicio.setText("");
                txtFechaFinal.setText("");
            }
        });

    }

    private void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void registrarUsuario(Alumno alumno, String correo, String password) {

        databaseSGA.getAuth().createUserWithEmailAndPassword(correo, password).
                addOnCompleteListener(RegistroUsuarioActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userId = task.getResult().getUser().getUid();
                            databaseSGA.registrarCuenta(alumno, userId);
                            databaseSGA.obtenerUsuario();
                            finish();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "Error al registrar la cuenta.",
                                    Toast.LENGTH_SHORT).show();
                    }
                });

    }
}