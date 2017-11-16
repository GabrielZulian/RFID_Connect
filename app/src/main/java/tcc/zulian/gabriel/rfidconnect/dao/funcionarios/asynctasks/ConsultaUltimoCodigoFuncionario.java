package tcc.zulian.gabriel.rfidconnect.dao.funcionarios.asynctasks;

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

import tcc.zulian.gabriel.rfidconnect.vo.funcionarios.GravaFuncionarioActivity;
import tcc.zulian.gabriel.rfidconnect.vo.itens.GravaItemActivity;

/**
 * Created by User on 14/08/2017.
 */
public class ConsultaUltimoCodigoFuncionario extends AsyncTask<String, Void, String> {
    private Context context;

    ProgressDialog dialog;
    GravaFuncionarioActivity gravaFuncionarioActivity;

    public ConsultaUltimoCodigoFuncionario(GravaFuncionarioActivity gravaFuncionarioActivity) {
        this.gravaFuncionarioActivity = gravaFuncionarioActivity;
        this.context = gravaFuncionarioActivity;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(gravaFuncionarioActivity);
        dialog.setTitle("Carregando...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "http://vinhosopra.com.br/json/rfid/con_funcionario_proximo_codigo.php";
            String data = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(
                    String.valueOf(1), "UTF-8" );

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
        Integer ultimoCod = 0;

        try {
            JSONObject jObjectGeral = new JSONObject(result);

            int success = jObjectGeral.getInt("success");

            if (success == 0) {
                Toast.makeText(context, "OOPs! Algo deu errado.", Toast.LENGTH_LONG).show();
            } else {
                ultimoCod = jObjectGeral.getInt("proximo_codigo");
            }

        } catch (JSONException e) {
            Toast.makeText(context, "OOPs! Algo deu errado." + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        gravaFuncionarioActivity.edtCodigo.setText(String.valueOf(ultimoCod));

        dialog.dismiss();
    }
}
