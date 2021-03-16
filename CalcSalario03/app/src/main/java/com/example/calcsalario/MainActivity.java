package com.example.calcsalario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static final String SALARIO_BRUTO = "com.example.calcsalario.SALARIO_BRUTO";
    public static final String DEPENDENTES = "com.example.calcsalario.DEPENDENTES";
    public static final String DESCONTOS = "com.example.calcsalario.DESCONTOS";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCalcular = (Button)findViewById(R.id.btnCalcular);

        final EditText edtTxtSalario = (EditText)findViewById(R.id.edtTxtSalarioBruto);
        final EditText edtTxtDependentes = (EditText)findViewById(R.id.edtTxtDependentes);
        final EditText edtTxtDescontos = (EditText)findViewById(R.id.edtTxtDescontos);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateFields();

                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);

                intent.putExtra(SALARIO_BRUTO, Double.parseDouble(edtTxtSalario.getText().toString()));
                intent.putExtra(DEPENDENTES, Integer.parseInt(edtTxtDependentes.getText().toString()));
                intent.putExtra(DESCONTOS, Double.parseDouble(edtTxtDescontos.getText().toString()));

                startActivity(intent);

                 }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Bem vindo de volta " + ((FirebaseUser) user).getEmail() + "!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, CadastrarActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private FirebaseAuth mAuth;

    private void validateFields() {
        EditText edtTxtSalario = (EditText)findViewById(R.id.edtTxtSalarioBruto);
        EditText edtTxtDependentes = (EditText)findViewById(R.id.edtTxtDependentes);
        EditText edtTxtDescontos = (EditText)findViewById(R.id.edtTxtDescontos);

        if(edtTxtSalario.getText().length() <=0)
        {
        edtTxtSalario.setText("0");
        }
        if(edtTxtDependentes.getText().length() <=0)
        {
            edtTxtDependentes.setText("0");
        }
        if(edtTxtDescontos.getText().length() <=0)
        {
            edtTxtDescontos.setText("0");
        }
    }
}