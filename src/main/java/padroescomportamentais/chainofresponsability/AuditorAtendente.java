package padroescomportamentais.chainofresponsability;

public class AuditorAtendente extends Auditor {

    public AuditorAtendente(Auditor superior) {
        procedimentosAutorizados.add(TipoProcedimentoConsulta.getTipoProcedimentoConsulta());
        setAuditorSuperior(superior);
    }

    @Override
    public String getDescricaoCargo() {
        return "Atendente";
    }
}
