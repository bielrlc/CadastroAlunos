package br.com.caelum.cadastroalunos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.cadastroalunos.entities.Aluno;

/**
 * Created by android6021 on 16/04/16.
 */
public class AlunoAdapter extends BaseAdapter {

    private List<Aluno> alunos;
    private ListaAlunosActivity activity;

    public AlunoAdapter(ListaAlunosActivity activity, List<Aluno> alunos) {
        this.alunos = alunos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View layout = activity.getLayoutInflater().inflate(R.layout.item, parent, false);

        Aluno aluno = alunos.get(position);

        TextView nome = (TextView) layout.findViewById(R.id.item_nome);
        nome.setText(aluno.getNome());

        Bitmap bm = null;
        if (aluno.getCaminhoFoto() != null){
            bm = BitmapFactory.decodeFile(aluno.getCaminhoFoto());
            bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
        } else {
            bm = BitmapFactory.decodeResource(activity.getResources(),R.drawable.ic_no_image);
        }

        ImageView foto = (ImageView) layout.findViewById(R.id.item_foto);
        foto.setImageBitmap(bm);

        //EXERCICIO OPCIONAL
        if (position % 2 == 0){
            layout.setBackgroundColor(activity.getResources().getColor(R.color.linha_par));
        } else {
            layout.setBackgroundColor(activity.getResources().getColor(R.color.linha_impar));
        }

        return layout;
    }
}
