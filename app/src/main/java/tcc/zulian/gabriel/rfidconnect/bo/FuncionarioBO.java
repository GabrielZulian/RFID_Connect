package tcc.zulian.gabriel.rfidconnect.bo;

/**
 * Created by User on 25/09/2017.
 */
public class FuncionarioBO {
    private Integer codigo;
    private String nome, cpf, funcao;

    public FuncionarioBO() {
        this.codigo = 0;
        this.nome = "";
        this.cpf = "";
        this.funcao = "";
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}
