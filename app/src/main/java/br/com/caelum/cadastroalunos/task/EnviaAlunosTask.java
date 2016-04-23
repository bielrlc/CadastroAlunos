package br.com.caelum.cadastroalunos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastroalunos.entities.Aluno;
import br.com.caelum.cadastroalunos.converters.AlunoConverter;
import br.com.caelum.cadastroalunos.support.WebClient;

/**
 * Created by android6021 on 16/04/16.
 */
public class EnviaAlunosTask extends AsyncTask<Object, Object, String> {

    private Context contexto;
    private ProgressDialog pd;

    public EnviaAlunosTask(Context context) {
        this.contexto = context;
    }

    @Override
    protected void onPreExecute() {
        pd = ProgressDialog.show(contexto, "Aguarde...", "Enviando Dados", true, false);
    }

    @Override
    protected String doInBackground(Object... params) {
        AlunoDAO dao = new AlunoDAO(contexto);
        List<Aluno> alunos = dao.getLista();
        dao.close();

        String json = new AlunoConverter().toJSON(alunos);

        WebClient client = new WebClient();
        return client.doPost(json);
    }

    @Override
    protected void onPostExecute(String resposta) {
        pd.dismiss();
        Toast.makeText(contexto, resposta, Toast.LENGTH_LONG).show();
    }
}
