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

/**
 * Created by User on 14/08/2017.
 */
public class ControleAcesso extends AsyncTask<String, Void, String> {

    private Context context;
    FuncionarioBO funcionarioBO;
    ProgressDialog dialog;
    Activity activity;

    public ControleAcesso(Activity activity, Context context, FuncionarioBO funcionarioBO) {
        this.activity = activity;
        this.context = context;
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
            String link = "http://vinhosopra.com.br/json/rfid/insere_acesso.php";
            String data = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(
                    String.valueOf(funcionarioBO.getCodigo()), "UTF-8" );

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
                Toast.makeText(context, "OOPs! Algo deu errado.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Acesso enviado com sucesso!", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Toast.makeText(context, "OOPs! Algo deu errado." + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        dialog.dismiss();
    }
}
