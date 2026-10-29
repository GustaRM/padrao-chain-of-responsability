package padroescomportamentais.chainofresponsability;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Papel "Handler" do Chain of Responsibility.
 * Cada auditor autoriza os tipos de procedimento da sua lista; se a solicitação
 * não for da sua competência, repassa ao auditor superior.
 */
public abstract class Auditor {

    public static final String SEM_AUTORIZACAO = "Sem autorização";

    protected final List<TipoProcedimento> procedimentosAutorizados = new ArrayList<>();
    private Auditor auditorSuperior;

    public Auditor getAuditorSuperior() {
        return auditorSuperior;
    }

    public final void setAuditorSuperior(Auditor auditorSuperior) {
        for (Auditor atual = auditorSuperior; atual != null; atual = atual.auditorSuperior) {
            if (atual == this) {
                throw new IllegalArgumentException("A cadeia de auditores não pode conter ciclos");
            }
        }
        this.auditorSuperior = auditorSuperior;
    }

    public abstract String getDescricaoCargo();

    public String autorizar(Solicitacao solicitacao) {
        Objects.requireNonNull(solicitacao, "A solicitação é obrigatória");
        if (procedimentosAutorizados.contains(solicitacao.getTipoProcedimento())) {
            return getDescricaoCargo();
        }
        if (auditorSuperior != null) {
            return auditorSuperior.autorizar(solicitacao);
        }
        return SEM_AUTORIZACAO;
    }
}
