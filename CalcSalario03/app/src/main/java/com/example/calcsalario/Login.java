package com.example.calcsalario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calcsalario.Util.UtilFirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailLogin, edtSenhaLogin;
    private Button btnLogin;
    private TextView irParaCadastro;

    // Variaveis do FireBaseAuth para que possa ser feito o cadastro
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Identificando IDs
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtSenhaLogin = findViewById(R.id.edtSenhaLogin);
        btnLogin = findViewById(R.id.btnLogin);
        irParaCadastro = findViewById(R.id.irParaCadastro);

        // Instanciando a variavel do FireBaseAuth (É obrigatorio ser feito dentro do onCreate)
        mAuth = FirebaseAuth.getInstance();

        // Ação click
        btnLogin.setOnClickListener(this);
        irParaCadastro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                // Recebendo dados
                String email = edtEmailLogin.getText().toString();
                String senha = edtSenhaLogin.getText().toString();

                // Verificando se campos estão preenchidos
                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(this, "Erro - Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    // Verificando se há conexão com internet
                    if(UtilFirebaseAuth.verificandoRede(getBaseContext())){
                        realizarLogin(email, senha);
                        finish();
                    }else{
                        Toast.makeText(this, "Erro - Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.irParaCadastro:
                startActivity(new Intent(getBaseContext(), CadastrarActivity.class));
                finish();
                break;
        }
    }

    private void realizarLogin(String email, String senha) {

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Retorna true se tiver feito o login e false se tiver ocorrido algum erro
                boolean loginSucesso = task.isSuccessful();

                // Verifica se foi possivel cadastrar
                if(loginSucesso){
                    Toast.makeText(Login.this, "Sucesso - Logado com Sucesso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }else{
                    // Verfica possiveis erros que possam ter ocorrido
                    UtilFirebaseAuth.tratandoErros(getBaseContext(), task.getException().toString());
                }
            }
        });
    }


}