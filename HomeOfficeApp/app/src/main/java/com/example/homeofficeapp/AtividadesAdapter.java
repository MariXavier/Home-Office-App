package com.example.homeofficeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AtividadesAdapter extends ArrayAdapter<Atividade>
{
    public AtividadesAdapter(Context context, ArrayList<Atividade> listaAtividades) {

        super(context, 0, listaAtividades);
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent)
    {

        Atividade atv = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adaptar_lista_atividades, parent, false);

        }

        TextView txtPeriodo = (TextView) convertView.findViewById(R.id.txtPeriodo);
        TextView txtDescricaoAtividade = (TextView) convertView.findViewById(R.id.txtDescricaoAtividade);

        String periodo = atv.dataInicio + " " + atv.horaInicio + " até " + atv.dataFim + " " + atv.horaFim;

        txtPeriodo.setText(periodo);
        txtDescricaoAtividade.setText(atv.atividade);

        return convertView;

    }
}
