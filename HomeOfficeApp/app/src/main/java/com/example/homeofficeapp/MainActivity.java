package com.example.homeofficeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    EditText editTextEmail, editTextSenha;
    Button buttonLogin, buttonNovo;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if(auth.getUid() != null)
        {
            Intent atividades = new Intent(MainActivity.this, AtividadesActivity.class);
            startActivity(atividades);
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonNovo = findViewById(R.id.buttonNovo);

        buttonNovo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                abreCadastro();
            }
        });
    }

    public void abreCadastro()
    {
        Intent cadastro = new Intent(MainActivity.this, CadastroUsuariosActivity.class);
        startActivity(cadastro);
    }

    public boolean validaCamposLogin()
    {
        String email, senha;

        email = editTextEmail.getText().toString();
        senha = editTextSenha.getText().toString();

        if(email.isEmpty() || !email.contains("@") || email.length() < 6)
        {
            Toast.makeText(this, "Preencha o campo e-mail corretamente", Toast.LENGTH_LONG).show();
            editTextEmail.requestFocus();
            return false;
        }
        else if(senha.isEmpty() || senha.length() < 6)
        {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_LONG).show();
            editTextSenha.requestFocus();
            return false;
        }
        else
        { return true; }
    }

    public void login(View view)
    {
        //FirebaseAuth auth = FirebaseAuth.getInstance();
        String email, senha;

        email = editTextEmail.getText().toString();
        senha = editTextSenha.getText().toString();

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Intent atividades = new Intent(MainActivity.this, AtividadesActivity.class);
                    startActivity(atividades);
                }
                else
                {
                    String mensagem = "Falhar no login";
                    Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
