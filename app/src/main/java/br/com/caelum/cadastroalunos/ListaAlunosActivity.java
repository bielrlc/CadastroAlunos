package br.com.caelum.cadastroalunos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastroalunos.adapter.AlunoAdapter;
import br.com.caelum.cadastroalunos.entities.Aluno;
import br.com.caelum.cadastroalunos.task.EnviaAlunosTask;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;
    private List<Aluno> alunos;
    public static final String ALUNO_SELECIONADO = "alunoSelecionado";
    private static final int REQUEST_LIGACAO = 6;
    private Aluno alunoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aluno);

        Button botao = (Button) findViewById(R.id.lista_aluno_botao);

        botao.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent edicao = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                edicao.putExtra(ALUNO_SELECIONADO, aluno);
                startActivity(edicao);
            }
        });

        //Exercicio 5,3
        registerForContextMenu(listaAlunos);

        String permissaoSms = android.Manifest.permission.RECEIVE_SMS;
        if (ActivityCompat.checkSelfPermission(this, permissaoSms) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{permissaoSms},1);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    public void carregaLista(){

        int layout;
        layout = android.R.layout.simple_list_item_1;

        AlunoDAO dao = new AlunoDAO(this);
        alunos = dao.getLista();
        dao.close();

        AlunoAdapter adapter = new AlunoAdapter(this, alunos);
        this.listaAlunos.setAdapter(adapter);
    }

    //Exercicio 5,3
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(info.position);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ListaAlunosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar?")
                        .setPositiveButton("Quero", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                dao.deletar(alunoSelecionado);
                                dao.close();
                                carregaLista();
                            }
                        }).setNegativeButton("Não", null).show();
                return false;
            }
        });

        MenuItem sms = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
        intentSMS.putExtra("sms_body", "Olá Aluno como vai você?");
        sms.setIntent(intentSMS);

        MenuItem mapa = menu.add("Ver no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("geo:0,0?z=14&q=" + alunoSelecionado.getEndereco()));
        mapa.setIntent(intentMapa);

        MenuItem site = menu.add("Abrir o Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("http:" + alunoSelecionado.getSite()));
        site.setIntent(intentSite);

        MenuItem ligar = menu.add("Ligar");
        intentSMS.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String permissaoLigar = Manifest.permission.CALL_PHONE;
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, permissaoLigar) == PackageManager.PERMISSION_GRANTED){
                    fazerLigacao();
                } else {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{permissaoLigar}, REQUEST_LIGACAO);
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[] resultados) {
        if (requestCode == REQUEST_LIGACAO) {
            if (resultados[0] == PackageManager.PERMISSION_GRANTED) {
                fazerLigacao();
            } else {
                Toast.makeText(this, "Não vai ter golpe", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fazerLigacao() {
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
        startActivity(intentLigar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_enviar_notas:
                AlunoDAO dao = new AlunoDAO(this);
                List<Aluno> alunos = dao.getLista();
                dao.close();

                new EnviaAlunosTask(this).execute();

                return true;

            case R.id.menu_receber_provas:
                Intent provas = new Intent(this, ProvasActivity.class);
                startActivity(provas);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
