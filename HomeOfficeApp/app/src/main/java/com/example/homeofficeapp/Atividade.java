package com.example.homeofficeapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Atividade
{
    String dataInicio;
    String dataFim;
    String horaInicio;
    String horaFim;
    String idFuncionario;
    String email;
    String atividade;

    public Atividade() { }

    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }

    public String getDataFim() { return dataFim; }
    public void setDataFim(String dataFim) { this.dataFim = dataFim; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFim() { return horaFim; }
    public void setHoraFim(String horaFim) { this.horaFim = horaFim; }

    public String getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(String idFuncionario) { this.idFuncionario = idFuncionario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAtividade() { return atividade; }
    public void setAtividade(String atividade) { this.atividade = atividade; }

    public void setAtividadeFinalizada()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AtividadeFinalizada");
        reference.push().setValue(this);
    }

}
