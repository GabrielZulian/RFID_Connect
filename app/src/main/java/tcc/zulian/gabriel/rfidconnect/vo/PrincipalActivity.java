package tcc.zulian.gabriel.rfidconnect.vo;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tcc.zulian.gabriel.rfidconnect.R;
import tcc.zulian.gabriel.rfidconnect.bo.FuncionarioBO;
import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;
import tcc.zulian.gabriel.rfidconnect.bo.NFCOperations;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotEnabled;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotSupported;
import tcc.zulian.gabriel.rfidconnect.dao.funcionarios.asynctasks.ControleAcesso;
import tcc.zulian.gabriel.rfidconnect.dao.itens.ItemEstoqueDAO;
import tcc.zulian.gabriel.rfidconnect.vo.funcionarios.GravaFuncionarioActivity;
import tcc.zulian.gabriel.rfidconnect.vo.itens.EntradaActivity;
import tcc.zulian.gabriel.rfidconnect.vo.itens.GravaItemActivity;
import tcc.zulian.gabriel.rfidconnect.vo.itens.SaidaActivity;

public class PrincipalActivity extends AppCompatActivity {

    Button btnControleAcesso, btnSaida, btnEntrada, btnVerificarEstoque, btnGravarTag, btnLimparTag;
    TextView textView;
    NFCOperations nfcOperations;
    Tag currentTag;

    public static ItemBO itemBO = null;
    public FuncionarioBO funcionarioBO = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnControleAcesso = (Button) findViewById(R.id.btnAcesso);
        btnSaida = (Button) findViewById(R.id.btnSaida);
        btnEntrada = (Button) findViewById(R.id.btnEntrada);
        btnVerificarEstoque = (Button) findViewById(R.id.btnVerificaEstoque);
        btnGravarTag = (Button) findViewById(R.id.btnGravar);
        btnLimparTag = (Button) findViewById(R.id.btnLimpar);
        textView = (TextView) findViewById(R.id.textViewTag);

        btnControleAcesso.setEnabled(false);
        btnEntrada.setEnabled(false);
        btnSaida.setEnabled(false);
        btnVerificarEstoque.setEnabled(false);

        nfcOperations = new NFCOperations(this);

        btnControleAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (funcionarioBO != null)
                    new ControleAcesso(PrincipalActivity.this, PrincipalActivity.this.getApplicationContext(), funcionarioBO).execute();
                else
                    Toast.makeText(PrincipalActivity.this.getApplicationContext(), "Tag funcionário não lida", Toast.LENGTH_LONG).show();
            }
        });

        btnEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemBO != null) {
                    Intent intent = new Intent(PrincipalActivity.this, EntradaActivity.class);
                    intent.putExtra("codigo", itemBO.getCodigo());
                    startActivity(intent);
                } else
                    Toast.makeText(PrincipalActivity.this.getApplicationContext(), "Tag de item não lida", Toast.LENGTH_LONG).show();
            }
        });

        btnSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemBO != null) {
                    Intent intent = new Intent(PrincipalActivity.this, SaidaActivity.class);
                    intent.putExtra("codigo", itemBO.getCodigo());
                    startActivity(intent);
                } else
                    Toast.makeText(PrincipalActivity.this.getApplicationContext(), "Tag de item não lida", Toast.LENGTH_LONG).show();
            }
        });

        btnVerificarEstoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemBO != null)
                    new ItemEstoqueDAO(PrincipalActivity.this, PrincipalActivity.this.getApplicationContext(), itemBO).execute();
                else
                    Toast.makeText(PrincipalActivity.this.getApplicationContext(), "Tag de item não lida", Toast.LENGTH_LONG).show();
            }
        });

        btnGravarTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence tipo[] = new CharSequence[] {"Item", "Funcionário"};

                AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                builder.setTitle("O que deseja gravar na tag?");
                builder.setItems(tipo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int op) {
                        if (op == 0) {
                            Intent intent = new Intent(PrincipalActivity.this, GravaItemActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PrincipalActivity.this, GravaFuncionarioActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        btnLimparTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaida.setEnabled(false);
                btnEntrada.setEnabled(false);
                btnVerificarEstoque.setEnabled(false);
                btnControleAcesso.setEnabled(false);
                funcionarioBO = null;
                itemBO = null;
                currentTag = null;

                textView.setText("Aproxime a Tag para realizar a leitura e desbloquear as operações");
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
    protected void onPause() {
        super.onPause();
        nfcOperations.disableDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String msg = nfcOperations.readTag(currentTag);

        if (msg!= null && !msg.isEmpty())
            msg = msg.substring(3, msg.length());

        String tipo = msg.substring(0, msg.indexOf(";"));

        msg = msg.substring(msg.indexOf(";")+1, msg.length());

        if (tipo.equals("TIPO=PRODUTO")) {
            btnSaida.setEnabled(true);
            btnEntrada.setEnabled(true);
            btnVerificarEstoque.setEnabled(true);
            btnControleAcesso.setEnabled(false);
            itemBO = new ItemBO();
            try {
                JSONObject object = new JSONObject(msg);

                Log.d("msg", msg);

                itemBO.setCodigo(object.getInt("codigo"));
                itemBO.setDescricao(object.getString("descricao"));
                itemBO.setUnidade(object.getString("unidade"));
                itemBO.setPeso(object.getDouble("peso"));
                itemBO.setNumeroSerial(object.getString("numero_serial"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            textView.setText("ID=" + itemBO.getCodigo() + "\n" +
                    "Descrição=" + itemBO.getDescricao() + "\n" +
                    "Unidade=" + itemBO.getUnidade() + "\n" +
                    "Peso=" + itemBO.getPeso() + "\n" +
                    "EAN-13=" + itemBO.getNumeroSerial());

        } else if (tipo.equals("TIPO=FUNCIONARIO")) {
            btnControleAcesso.setEnabled(true);
            btnEntrada.setEnabled(false);
            btnSaida.setEnabled(false);
            btnVerificarEstoque.setEnabled(false);
            funcionarioBO = new FuncionarioBO();
            try {
                JSONObject object = new JSONObject(msg);

                Log.d("json", object.toString());

                funcionarioBO.setCodigo(object.getInt("codigo"));
                funcionarioBO.setNome(object.getString("nome"));
                funcionarioBO.setCpf(object.getString("cpf"));
                funcionarioBO.setFuncao(object.getString("funcao"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            textView.setText("ID=" + funcionarioBO.getCodigo() + "\n" +
                    "Nome=" + funcionarioBO.getNome() + "\n" +
                    "CPF=" + funcionarioBO.getCpf() + "\n" +
                    "Função=" + funcionarioBO.getFuncao());
        }
    }
}