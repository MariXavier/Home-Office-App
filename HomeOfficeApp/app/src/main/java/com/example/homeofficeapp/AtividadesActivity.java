package com.example.homeofficeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AtividadesActivity extends AppCompatActivity
{

    TextView textViewData, textViewHora;
    Button buttonSalvar, buttonFinalizaAtividade, buttonVerAtividades;
    EditText editTextAtividades;
    Atividade atividade;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);

        auth = FirebaseAuth.getInstance();

        textViewData = findViewById(R.id.textViewData);
        textViewHora = findViewById(R.id.textViewHora);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonFinalizaAtividade = findViewById(R.id.buttonFinalizaAtividade);
        buttonVerAtividades = findViewById(R.id.buttonVerAtividades);
        editTextAtividades = findViewById(R.id.editTextAtividades);

        SharedPreferences preferencesAtividades = getSharedPreferences("Atividades", MODE_PRIVATE);
        String atividades = preferencesAtividades.getString("Atividades", null);
        editTextAtividades.setText(atividades);

        atividade = new Atividade();
        textViewData.setText(data());
        textViewHora.setText(hora());

        buttonSalvar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //construitor de alertas
                AlertDialog.Builder builder = new AlertDialog.Builder(AtividadesActivity.this);

                //habilita opção cancelar
                builder.setCancelable(true);

                //cria tituto caixa de dialogo
                builder.setTitle("Gravador de atividades");

                //cria mensagem da caixa de dialogo
                builder.setMessage("Deseja salvar as atividades?");

                //cria botao confirma
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        salvaAtividadesLocal();
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonFinalizaAtividade.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(AtividadesActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Gravador de atividades no banco de dados da empresa");
                builder.setMessage("Deseja salvar as atividades no banco de dados da empresa agora?");

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        salvaAtividadeNoFirebase();
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonVerAtividades.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AtividadesActivity.this, VisualizaAtividadesActivity.class));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuSair:
                auth.signOut();
                startActivity(new Intent(AtividadesActivity.this, MainActivity.class));
        }
       return super.onOptionsItemSelected(item);
    }

    public void salvaAtividadesLocal()
    {
        String atividades = editTextAtividades.getText().toString();

        if(atividades.isEmpty() || atividades.length() < 6)
        {
            Toast.makeText(this, "O campo deve ter pelo menos 6 caracteres",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //cria uma variavel que abre o arquivo para edição
            SharedPreferences sharedPreferencesData = getSharedPreferences("dataInicio", MODE_PRIVATE);
            //habilita o editor paa editar o arquivo
            SharedPreferences.Editor editorData = sharedPreferencesData.edit();
            editorData.putString("dataInicio", textViewData.getText().toString());
            editorData.commit();

            SharedPreferences sharedPreferencesHora = getSharedPreferences("horaInicio", MODE_PRIVATE);
            SharedPreferences.Editor editorHora = sharedPreferencesHora.edit();
            editorHora.putString("horaInicio", textViewHora.getText().toString());
            editorHora.commit();

            SharedPreferences sharedPreferencesAtividades = getSharedPreferences("Atividades", MODE_PRIVATE);
            SharedPreferences.Editor editorAtividades = sharedPreferencesAtividades.edit();
            editorAtividades.putString("Atividades", editTextAtividades.getText().toString());
            editorAtividades.commit();

            String msg = "Sua atividade foi salva localmente";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void salvaAtividadeNoFirebase()
    {
        SharedPreferences preferencesAtividades = getSharedPreferences("Atividades", MODE_PRIVATE);
        String atividades = preferencesAtividades.getString("Atividades", null);

        SharedPreferences preferencesDataInicial = getSharedPreferences("dataInicio", MODE_PRIVATE);
        String dataInicial = preferencesDataInicial.getString("dataInicio", null);

        SharedPreferences preferencesHoraInicial = getSharedPreferences("horaInicio", MODE_PRIVATE);
        String horaInicial = preferencesHoraInicial.getString("horaInicio", null);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        String idUsuarioLogado = auth.getUid();
        String emailUsuarioLogado = auth.getCurrentUser().getEmail();
        String data = data();
        String hora = hora();

        atividade.setDataInicio(dataInicial);
        atividade.setHoraInicio(horaInicial);
        atividade.setAtividade(atividades);

        atividade.setDataFim(data);
        atividade.setHoraFim(hora);
        atividade.setEmail(emailUsuarioLogado);
        atividade.setIdFuncionario(idUsuarioLogado);

        atividade.setAtividadeFinalizada();
    }


    public String hora()
    {
        //cria o formato da data no padrão brasileiro
        String formatacao = "HH:mm";

        //cria uma variavel do tipo formato da hora e recebe nosso formato
        SimpleDateFormat formataHora = new SimpleDateFormat(formatacao);

        //busca local onde está
        formataHora.setTimeZone(TimeZone.getDefault());

        //pega o dia do calendario
        Date hora = Calendar.getInstance().getTime();

        //formata a data que pegou do calendário
        String horaFormatada = formataHora.format(hora);

        return horaFormatada;
    }

    public String data()
    {
        //cria o formato da data no padrão brasileiro
        String formatacao = "dd/MM/yyyy";

        //cria uma variavel do tipo formato da data e recebe nosso formato
        SimpleDateFormat formataData = new SimpleDateFormat(formatacao);

        //busca local onde está
        formataData.setTimeZone(TimeZone.getTimeZone("UTC"));

        //pega o dia do calendario
        Date hoje = Calendar.getInstance().getTime();

        //formata a data que pegou do calendário
        String hojeFormatado = formataData.format(hoje);

        return hojeFormatado;
    }
}
