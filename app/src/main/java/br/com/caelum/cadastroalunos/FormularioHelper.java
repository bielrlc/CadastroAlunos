package br.com.caelum.cadastroalunos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.caelum.cadastroalunos.entities.Aluno;

/**
 * Created by android6021 on 02/04/16.
 */
public class FormularioHelper {

    private EditText nome;
    private EditText telefone;
    private EditText endereco;
    private EditText site;
    private RatingBar nota;
    private Aluno aluno;
    private ImageView foto;
    private Button fotoButton;

    public FormularioHelper(FormularioActivity activity) {

        this.nome = (EditText)activity.findViewById(R.id.formulario_nome);
        this.telefone = (EditText)activity.findViewById(R.id.formulario_telefone);
        this.endereco = (EditText)activity.findViewById(R.id.formulario_endereco);
        this.site = (EditText)activity.findViewById(R.id.formulario_site);
        this.nota = (RatingBar)activity.findViewById(R.id.formulario_nota);
        this.foto = (ImageView) activity.findViewById(R.id.formulario_foto);
        this.fotoButton = (Button) activity.findViewById(R.id.formulario_foto_button);
        this.aluno = new Aluno();
    }

    public Aluno pegaDadosFormulario(){

        aluno.setNome(nome.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setNota(Double.valueOf(nota.getProgress()));
        aluno.setCaminhoFoto((String) foto.getTag());
        return aluno;
    }


    public boolean temNome() {
        return !nome.getText().toString().isEmpty();
    }

    public void mostraErro() {
        nome.setError("Campo nome não pode ser vazio");
    }

    public void colocaNoFormulario(Aluno aluno){

        nome.setText(aluno.getNome());
        telefone.setText(aluno.getTelefone());
        endereco.setText(aluno.getEndereco());
        site.setText(aluno.getSite());
        nota.setProgress(aluno.getNota().intValue());
        if (aluno.getCaminhoFoto() != null){
            this.carregaImagem(aluno.getCaminhoFoto());
        }
        this.aluno = aluno;
    }

    public Button getFotoButton() {
        return fotoButton;
    }

    public void carregaImagem(String localArquivoFoto) {
        Bitmap imagem = BitmapFactory.decodeFile(localArquivoFoto);
        Bitmap imagemReduzida = Bitmap.createScaledBitmap(imagem, 400, 300, true);
        foto.setImageBitmap(imagemReduzida);
        foto.setTag(localArquivoFoto);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
