package tcc.zulian.gabriel.rfidconnect.dao.itens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;
import tcc.zulian.gabriel.rfidconnect.vo.PrincipalActivity;

/**
 * Created by User on 14/08/2017.
 */
public class ItemEstoqueDAO extends AsyncTask<String, Void, String> {
    private Context context;
    private ItemBO itemBO;

    ProgressDialog dialog;
    Activity activity;

    public ItemEstoqueDAO(Activity activity, Context context, ItemBO itemBO) {
        this.activity = activity;
        this.context = context;
        this.itemBO = itemBO;
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
            String link = "http://vinhosopra.com.br/json/rfid/con_item_estoque.php";
            String data = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(
                    String.valueOf(itemBO.getCodigo()), "UTF-8" );

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
                JSONArray jEstoque = jObjectGeral.getJSONArray("estoques");
                JSONObject objeto = jEstoque.getJSONObject(0);
                itemBO.getEstoqueBO().setQuantidade(objeto.getInt("quantidade"));
            }

        } catch (JSONException e) {
            Toast.makeText(context, "OOPs! Algo deu errado." + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        PrincipalActivity.itemBO = itemBO;

        dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Quantidade em estoque");
        builder.setMessage(itemBO.getEstoqueBO().getQuantidade() + "");
        builder.show();

        Log.d("TESTE=", itemBO.getEstoqueBO().getQuantidade() + "");
    }
}
