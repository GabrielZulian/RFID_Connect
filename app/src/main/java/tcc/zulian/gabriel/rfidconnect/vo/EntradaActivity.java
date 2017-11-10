package tcc.zulian.gabriel.rfidconnect.vo;

import android.os.Bundle;

import tcc.zulian.gabriel.rfidconnect.R;
import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;

/**
 * Created by User on 16/10/2017.
 */
public class EntradaActivity extends EntradaSaidaPaiActivity {

    public static ItemBO itemBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        tvQntdOperacao.setText("Quantidade Entrada");
        btnConfirmar.setText("Confirmar Entrada");
    }

    @Override
    public void buscaItem() {
        Integer codigo = Integer.parseInt(edtCodigo.getText().toString());
        itemBO = new ItemBO();
        itemBO.setCodigo(codigo);
//        new ItemVerificaEstoqueItemSaidaDAO(EntradaActivity.this, this, itemBO).execute();

        edtDescricao.setText(itemBO.getDescricao());
        edtQntdEstoque.setText(itemBO.getEstoqueBO().getQuantidade());
    }

    @Override
    public void confirmaOperacao() {

    }
}
