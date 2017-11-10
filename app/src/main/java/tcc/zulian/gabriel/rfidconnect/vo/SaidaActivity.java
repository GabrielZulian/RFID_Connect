package tcc.zulian.gabriel.rfidconnect.vo;

import android.os.Bundle;

import tcc.zulian.gabriel.rfidconnect.bo.ItemBO;
import tcc.zulian.gabriel.rfidconnect.dao.ItemConfirmaSaidaDAO;
import tcc.zulian.gabriel.rfidconnect.dao.ItemVerificaEstoqueSaidaDAO;

/**
 * Created by User on 16/10/2017.
 */
public class SaidaActivity extends EntradaSaidaPaiActivity {

    public static ItemBO itemBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvQntdOperacao.setText("Quantidade Saída");
        btnConfirmar.setText("Confirmar Saída");
    }

    @Override
    public void buscaItem() {
        Integer codigo = Integer.parseInt(edtCodigo.getText().toString());
        itemBO = new ItemBO();
        itemBO.setCodigo(codigo);
        new ItemVerificaEstoqueSaidaDAO(this, itemBO).execute();
    }

    @Override
    public void confirmaOperacao() {
        Integer quantidadeAtual = Integer.parseInt(edtQntdEstoque.getText().toString());
        Integer quantidadeEntrada = Integer.parseInt(edtQntdOperacao.getText().toString());
        itemBO.getEstoqueBO().setQuantidade(quantidadeAtual+quantidadeEntrada);
        new ItemConfirmaSaidaDAO(this, itemBO).execute();
    }
}
