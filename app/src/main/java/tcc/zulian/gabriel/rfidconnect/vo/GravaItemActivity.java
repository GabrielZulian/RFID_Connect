package tcc.zulian.gabriel.rfidconnect.vo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tcc.zulian.gabriel.rfidconnect.R;
import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;
import tcc.zulian.gabriel.rfidconnect.bo.NFCOperations;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotEnabled;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotSupported;
import tcc.zulian.gabriel.rfidconnect.dao.GravaItemDAO;

public class GravaItemActivity extends AppCompatActivity {

    EditText edtDescricao, edtUnidade, edtPeso, edtSerial;
    Button btnFinalizar;

    NFCOperations nfcOperations;
    private NdefMessage message = null;
    Tag currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grava_item);

        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtUnidade = (EditText) findViewById(R.id.edtUnidade);
        edtPeso = (EditText) findViewById(R.id.edtPeso);
        edtSerial = (EditText) findViewById(R.id.edtSerial);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizarItem);

        nfcOperations = new NFCOperations(this);
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

        ItemBO itemBO = new ItemBO();
        String textoASerGravado = null;

        if (edtDescricao.getText().toString().equals("") || edtUnidade.getText().toString().equals("")
                || edtPeso.getText().toString().equals("") || edtSerial.getText().toString().equals("")) {
            Toast.makeText(this.getApplicationContext(), "String Vazia!", Toast.LENGTH_LONG).show();
            return;
        } else {


            itemBO.setDescricao(edtDescricao.getText().toString());
            itemBO.setUnidade(edtUnidade.getText().toString());
            itemBO.setPeso(Double.parseDouble(edtPeso.getText().toString()));
            itemBO.setNumeroSerial(edtSerial.getText().toString());

            textoASerGravado = "TIPO=PRODUTO;{\"descricao\":\""+ edtDescricao.getText().toString() + "\"" +
                    ",\"unidade\":\"" + edtUnidade.getText().toString() + "\"" +
                    ",\"peso\":\"" + edtPeso.getText().toString() + "\"" +
                    ",\"numero_serial\":\"" + edtSerial.getText().toString() + "\"}";
        }

        message = nfcOperations.createTextMessage(textoASerGravado);

        if (message != null) {
            nfcOperations.writeTag(currentTag, message);
            Toast.makeText(this.getApplicationContext(), "Tag gravada com sucesso", Toast.LENGTH_LONG).show();
            new GravaItemDAO(GravaItemActivity.this, GravaItemActivity.this.getApplicationContext(), itemBO).execute();
        } else {
            Toast.makeText(this.getApplicationContext(), "Erro na gravação", Toast.LENGTH_LONG).show();
        }
    }
}

