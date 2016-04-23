package br.com.caelum.cadastroalunos;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastroalunos.entities.Aluno;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;
    private String localArquivoFoto;
    private static final int REQUEST_FOTO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        this.helper = new FormularioHelper(this);
        Aluno aluno = (Aluno) getIntent().getSerializableExtra(ListaAlunosActivity.ALUNO_SELECIONADO);
        if (aluno != null){
            this.helper.colocaNoFormulario(aluno);
        }

        Button foto = helper.getFotoButton();
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                //CHAMANDO A CÂMERA DO ANDROID
                Intent tirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uriFoto = Uri.fromFile(new File(localArquivoFoto));
                tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(tirarFoto, REQUEST_FOTO);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario:

                if (helper.temNome()){

                    Aluno aluno = helper.pegaDadosFormulario();
                    AlunoDAO dao = new AlunoDAO(this);

                    dao.insereOuAltera(aluno);

                    dao.close();
                    finish();
                    return true;

                } else {
                    helper.mostraErro();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_FOTO){
            if (resultCode == RESULT_OK){
                helper.carregaImagem(localArquivoFoto);
            } else {
                localArquivoFoto = null;
            }
        }
    }
}
