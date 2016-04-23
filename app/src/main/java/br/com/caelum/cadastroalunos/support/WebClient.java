package br.com.caelum.cadastroalunos.support;

import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by android6021 on 16/04/16.
 */
public class WebClient {

    public String doPost(String json){
        URL url = null;
        HttpURLConnection con = null;
        PrintStream saida;
        Scanner retorno;
        String resposta = "";
        try {
            url = new URL("https://www.caelum.com.br/mobile");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);

            saida = new PrintStream(con.getOutputStream());
            saida.println(json);

            con.connect();

            retorno = new Scanner(con.getInputStream());

            if (retorno.hasNext()){
                resposta = retorno.next();
            } else {
                resposta = "Sem resposta";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Excecao Conexao", e.getMessage());
        }
        return resposta;

    }

}
