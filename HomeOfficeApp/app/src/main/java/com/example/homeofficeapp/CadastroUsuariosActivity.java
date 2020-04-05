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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuariosActivity extends AppCompatActivity
{
    EditText editTextCadastroNome, editTextCadastroEmail, editTextCadastroSenha;
    Button buttonLogin, buttonCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuarios);

        editTextCadastroNome = findViewById(R.id.editTextCadastroNome);
        editTextCadastroEmail = findViewById(R.id.editTextCadastroEmail);
        editTextCadastroSenha = findViewById(R.id.editTextCadastroSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
    }

    public boolean validaCamposCadastro()
    {
        String nome, email, senha;

        nome = editTextCadastroNome.getText().toString();
        email = editTextCadastroEmail.getText().toString();
        senha = editTextCadastroSenha.getText().toString();

        if(nome.isEmpty() || nome.length() < 3)
        {
            Toast.makeText(this, "O campo nome deve ter pelo menos 3 caracteres", Toast.LENGTH_LONG).show();
            editTextCadastroNome.requestFocus();
            return false;
        }
        else if(email.isEmpty() || !email.contains("@") || email.length() < 6)
        {
            Toast.makeText(this, "Preencha o campo e-mail corretamente", Toast.LENGTH_LONG).show();
            editTextCadastroEmail.requestFocus();
            return false;
        }
        else if(senha.isEmpty() || senha.length() < 6)
        {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_LONG).show();
            editTextCadastroSenha.requestFocus();
            return false;
        }
        else
        { return true; }
    }

    public void cadastraUsuario(View view)
    {
        if(validaCamposCadastro())
        {
            final String email, senha, nome;
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuario");

            email = editTextCadastroEmail.getText().toString();
            senha = editTextCadastroSenha.getText().toString();
            nome = editTextCadastroNome.getText().toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        ClasseUsuario usuario = new ClasseUsuario();
                        usuario.setId(task.getResult().getUser().getUid());
                        usuario.setEmail(email);
                        usuario.setNome(nome);
                        usuario.setSenha(senha);
                        reference.child(usuario.getId()).setValue(usuario);

                        Intent atividades = new Intent(CadastroUsuariosActivity.this, AtividadesActivity.class);
                        startActivity(atividades);
                    }
                    else
                    {
                        String mensagem = "Falhar ao cadastrar";
                        Toast.makeText(CadastroUsuariosActivity.this, mensagem, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
