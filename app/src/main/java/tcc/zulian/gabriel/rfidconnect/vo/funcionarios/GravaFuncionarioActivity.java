package tcc.zulian.gabriel.rfidconnect.vo.funcionarios;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tcc.zulian.gabriel.rfidconnect.R;
import tcc.zulian.gabriel.rfidconnect.bo.FuncionarioBO;
import tcc.zulian.gabriel.rfidconnect.bo.NFCOperations;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotEnabled;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotSupported;
import tcc.zulian.gabriel.rfidconnect.dao.funcionarios.asynctasks.ConsultaUltimoCodigoFuncionario;
import tcc.zulian.gabriel.rfidconnect.dao.funcionarios.asynctasks.GravaFuncionario;

public class GravaFuncionarioActivity extends AppCompatActivity {

    public EditText edtCodigo;
    EditText edtNome;
    EditText edtCPF;
    EditText edtFuncao;
    Button btnFinalizar;

    NFCOperations nfcOperations;
    private NdefMessage message = null;
    Tag currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grava_funcionario);

        edtCodigo = (EditText) findViewById(R.id.edtCodigoFunc);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtCPF = (EditText) findViewById(R.id.edtCPF);
        edtFuncao = (EditText) findViewById(R.id.edtFuncao);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizarFunc);
        edtCodigo.setEnabled(false);
        edtCodigo.setFocusable(false);
        edtCodigo.setTextColor(Color.BLACK);

        btnFinalizar.setEnabled(false);

        nfcOperations = new NFCOperations(this);
        new ConsultaUltimoCodigoFuncionario(this).execute();

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            nfcOperations.verifyNFC();
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[] {};
            String[][] techList = new String[][] {
                    { android.nfc.tech.Ndef.class.getName() },
                    { android.nfc.tech.NdefFormatable.class.getName() }
            };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
        }
        catch(NFCNotSupported nfcnsup) {
            Toast.makeText(this.getApplicationContext(), "Seu aparelho não suporta NFC", Toast.LENGTH_LONG).show();
        }
        catch(NFCNotEnabled nfcnEn) {
            Toast.makeText(this.getApplicationContext(), "NFC desativado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {;
        super.onPause();
        nfcOperations.disableDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("Nfc", "New intent");
        // It is the time to write the tag
        currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        FuncionarioBO funcionarioBO = new FuncionarioBO();
        String textoASerGravado = null;

        if (edtCodigo.getText().toString().isEmpty() || edtNome.getText().toString().isEmpty()
                || edtCPF.getText().toString().isEmpty() || edtFuncao.getText().toString().isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "String Vazia!", Toast.LENGTH_LONG).show();
            return;
        } else {

            funcionarioBO.setCodigo(Integer.valueOf(edtCodigo.getText().toString()));
            funcionarioBO.setNome(edtNome.getText().toString());
            funcionarioBO.setCpf(edtCPF.getText().toString());
            funcionarioBO.setFuncao(edtFuncao.getText().toString());

            JSONObject json = new JSONObject();

            try {
                json.put("codigo", funcionarioBO.getCodigo());
                json.put("nome", funcionarioBO.getNome());
                json.put("cpf", funcionarioBO.getCpf());
                json.put("funcao", funcionarioBO.getFuncao());
            } catch (JSONException e) {
                Toast.makeText(this.getApplicationContext(), "Erro ao gravar JSON!", Toast.LENGTH_LONG).show();
                return;
            }

            String stringJson = "TIPO=FUNCIONARIO;" + json.toString();

            Log.d("JSON F", stringJson);

            textoASerGravado = stringJson;
        }

        message = nfcOperations.createTextMessage(textoASerGravado);

        if (message != null) {
            nfcOperations.writeTag(currentTag, message);
            Toast.makeText(this.getApplicationContext(), "Tag gravada com sucesso", Toast.LENGTH_LONG).show();
            new GravaFuncionario(GravaFuncionarioActivity.this, funcionarioBO).execute();
            btnFinalizar.setEnabled(true);
            edtNome.setEnabled(false);
            edtFuncao.setEnabled(false);
            edtCPF.setEnabled(false);
        } else {
            Toast.makeText(this.getApplicationContext(), "Erro na gravação", Toast.LENGTH_LONG).show();
        }
    }
}
