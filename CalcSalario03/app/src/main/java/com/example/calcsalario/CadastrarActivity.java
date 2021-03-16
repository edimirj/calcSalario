package com.example.calcsalario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calcsalario.Util.UtilFirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailCadastro, edtSenhaCadastro;
    private Button btnCadastrar;
    private TextView irParaLogin;

    // Variaveis do FireBaseAuth para que possa ser feito o cadastro
    private FirebaseAuth mAuth;

    // Variavel para que possa ser feito a verificação se o usuario já tiver cadastrado
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Identificando IDs
        edtEmailCadastro = findViewById(R.id.edtEmailCadastro);
        edtSenhaCadastro = findViewById(R.id.edtSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        irParaLogin = findViewById(R.id.irParaLogin);

        // Instanciando a variavel do FireBaseAuth (É obrigatorio ser feito dentro do onCreate)
        mAuth = FirebaseAuth.getInstance();

        // Ação click
        btnCadastrar.setOnClickListener(this);
        irParaLogin.setOnClickListener(this);

        // Verificando se o usuario estiver logado
        verificacaoLogado();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCadastrar:
                // Recebendo dados
                String email = edtEmailCadastro.getText().toString();
                String senha = edtSenhaCadastro.getText().toString();

                // Verificando se campos estão preenchidos
                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(this, "Erro - Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    // Verificando se há conexão com internet
                   if(UtilFirebaseAuth.verificandoRede(getBaseContext())){
                       cadastrarUsuario(email, senha);
                       finish();
                   }else{
                       Toast.makeText(this, "Erro - Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
                   }
                }
                break;
            case R.id.irParaLogin:
                startActivity(new Intent(getBaseContext(), Login.class));
                break;
        }
    }

    private void cadastrarUsuario(String email, String senha) {

        // Cadastrando novo usuario
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Retorna true se tiver feito o cadastro e false se tiver ocorrido algum erro
                boolean cadastroSucesso = task.isSuccessful();

                // Verifica se foi possivel cadastrar
                if(cadastroSucesso){
                    Toast.makeText(CadastrarActivity.this, "Sucesso - Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }else{
                    // Verfica possiveis erros que possam ter ocorrido
                    UtilFirebaseAuth.tratandoErros(getBaseContext(), task.getException().toString());
                }
            }
        });
    }

    private void verificacaoLogado(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Variavel para resgatar informações do usuario
                FirebaseUser mUser = firebaseAuth.getCurrentUser();

                if(mUser == null){
                    // Usuario não logado
                }else{
                    // Usuario Logado
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}