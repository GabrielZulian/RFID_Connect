package tcc.zulian.gabriel.rfidconnect.vo.itens;

import android.os.Bundle;
import android.util.Log;

import tcc.zulian.gabriel.rfidconnect.R;
import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;
import tcc.zulian.gabriel.rfidconnect.dao.itens.asynctasks.ConfirmaEntrada;
import tcc.zulian.gabriel.rfidconnect.dao.itens.asynctasks.VerificaEstoqueEntrada;

/**
 * Created by User on 16/10/2017.
 */
public class EntradaActivity extends EntradaSaidaPaiActivity {

    public static ItemBO itemBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        int codigo = bundle.getInt("codigo");

        edtCodigo.setText(String.valueOf(codigo));
        tvQntdOperacao.setText("Quantidade Entrada");
        btnConfirmar.setText("Confirmar Entrada");
        Log.d("passou", "passou");
        buscaItem();
    }

    @Override
    public void buscaItem() {
        Integer codigo = Integer.parseInt(edtCodigo.getText().toString());
        itemBO = new ItemBO();
        itemBO.setCodigo(codigo);
        new VerificaEstoqueEntrada(EntradaActivity.this, itemBO).execute();
    }

    @Override
    public void confirmaOperacao() {
        Integer quantidadeAtual = Integer.parseInt(edtQntdEstoque.getText().toString());
        Integer quantidadeEntrada = Integer.parseInt(edtQntdOperacao.getText().toString());
        itemBO.getEstoqueBO().setQuantidade(quantidadeAtual+quantidadeEntrada);
        new ConfirmaEntrada(this, itemBO).execute();
    }
}
