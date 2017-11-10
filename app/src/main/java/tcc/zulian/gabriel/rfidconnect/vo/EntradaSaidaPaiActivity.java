package tcc.zulian.gabriel.rfidconnect.vo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tcc.zulian.gabriel.rfidconnect.R;

public abstract class EntradaSaidaPaiActivity extends AppCompatActivity {

    public EditText edtCodigo, edtDescricao, edtQntdEstoque, edtQntdOperacao;
    public TextView tvQntdOperacao;
    public Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        edtCodigo = (EditText) findViewById(R.id.edtCodigo);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtQntdEstoque = (EditText) findViewById(R.id.edtQntdEstoque);
        edtQntdOperacao = (EditText) findViewById(R.id.edtQntdOperacao);
        tvQntdOperacao = (TextView) findViewById(R.id.tvQntdOperacao);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmarOperacao);

        edtQntdEstoque.setEnabled(false);
        edtQntdEstoque.setTextColor(Color.BLACK);
        edtDescricao.setEnabled(false);
        edtDescricao.setTextColor(Color.BLACK);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtQntdOperacao.getText().toString().isEmpty() || Integer.valueOf(edtQntdOperacao.getText().toString()) == 0) {
                    Toast.makeText(getApplicationContext(), "Quantidades inválidas", Toast.LENGTH_SHORT).show();
                    return;
                } else
                    confirmaOperacao();
            }
        });

        edtCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        if (edtCodigo.getText().toString().isEmpty() || Integer.parseInt(edtCodigo.getText().toString()) <= 0) {
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                            return;
                        } else
                            buscaItem();
                }
            }
        });
    }

    public abstract void buscaItem();
    public abstract void confirmaOperacao();

}
