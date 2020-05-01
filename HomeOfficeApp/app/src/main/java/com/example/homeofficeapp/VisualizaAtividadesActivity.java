package com.example.homeofficeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VisualizaAtividadesActivity extends AppCompatActivity
{
    FirebaseAuth auth;
    ArrayList<Atividade> listaAtividades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_atividades);

        auth = FirebaseAuth.getInstance();

        recuperarAtividadesUsuario();
    }

    public void recuperarAtividadesUsuario()
    {
        auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AtividadeFinalizada");
        Query query = reference.orderByChild("idFuncionario").equalTo(auth.getUid());

        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Atividade atividade = ds.getValue(Atividade.class);
                    listaAtividades.add(atividade);
                }

                AtividadesAdapter adapter = new AtividadesAdapter(VisualizaAtividadesActivity.this, listaAtividades);

                ListView listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuSair:
                auth.signOut();
                startActivity(new Intent(VisualizaAtividadesActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
