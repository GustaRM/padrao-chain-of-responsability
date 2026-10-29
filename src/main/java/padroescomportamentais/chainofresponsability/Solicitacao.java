package padroescomportamentais.chainofresponsability;

import java.util.Objects;

/** Equivalente ao "Documento" do exemplo: o pedido que percorre a cadeia. */
public class Solicitacao {

    private TipoProcedimento tipoProcedimento;

    public Solicitacao(TipoProcedimento tipoProcedimento) {
        this.tipoProcedimento = Objects.requireNonNull(tipoProcedimento, "O tipo de procedimento é obrigatório");
    }

    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    public void setTipoProcedimento(TipoProcedimento tipoProcedimento) {
        this.tipoProcedimento = Objects.requireNonNull(tipoProcedimento, "O tipo de procedimento é obrigatório");
    }
}
