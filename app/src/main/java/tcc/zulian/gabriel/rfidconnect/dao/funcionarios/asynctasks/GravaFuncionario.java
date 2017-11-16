package tcc.zulian.gabriel.rfidconnect.dao.funcionarios.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import tcc.zulian.gabriel.rfidconnect.bo.FuncionarioBO;
import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;
import tcc.zulian.gabriel.rfidconnect.vo.funcionarios.GravaFuncionarioActivity;

/**
 * Created by User on 07/11/2017.
 */
public class GravaFuncionario extends AsyncTask<String, Void, String> {
    private FuncionarioBO funcionarioBO;

    ProgressDialog dialog;
    GravaFuncionarioActivity activity;

    public GravaFuncionario(GravaFuncionarioActivity activity, FuncionarioBO funcionarioBO) {
        this.activity = activity;
        this.funcionarioBO = funcionarioBO;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Carregando...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "http://vinhosopra.com.br/json/rfid/cad_funcionario.php";
            String data = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(funcionarioBO.getNome()), "UTF-8" );
            data += "&" + URLEncoder.encode("cpf", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(funcionarioBO.getCpf()), "UTF-8" );
            data += "&" + URLEncoder.encode("funcao", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(funcionarioBO.getFuncao()), "UTF-8" );

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            Log.d("Result", "[" + sb + "]");
            return sb.toString();
        } catch(Exception e){
            Log.d("Erro", e.getMessage() + "Erro");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Resultado", "[" + result + "]");

        try {
            JSONObject jObjectGeral = new JSONObject(result);

            int success = jObjectGeral.getInt("success");

            if (success == 0) {
                Toast.makeText(activity, "OOPs! Algo deu errado.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Inserido com sucesso no banco!", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Toast.makeText(activity, "OOPs! Algo deu errado." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
}
