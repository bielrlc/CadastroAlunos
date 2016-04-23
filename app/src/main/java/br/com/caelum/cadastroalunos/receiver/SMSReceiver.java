package br.com.caelum.cadastroalunos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastroalunos.R;

/**
 * Created by android6021 on 16/04/16.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent data) {
        Bundle bundle = data.getExtras();
        Object[] mensagens = (Object[]) bundle.get("pdus");

        byte[] mensagem = (byte[]) mensagens[0];

        String formato = (String) bundle.get("format");

        SmsMessage sms = SmsMessage.createFromPdu(mensagem, formato);

        String telefone = sms.getDisplayMessageBody();

        AlunoDAO dao = new AlunoDAO(context);
        if (dao.isAluno(telefone)){
            Toast.makeText(context, "Chegou SMS" + sms.getDisplayOriginatingAddress(), Toast.LENGTH_LONG).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
    }
}
