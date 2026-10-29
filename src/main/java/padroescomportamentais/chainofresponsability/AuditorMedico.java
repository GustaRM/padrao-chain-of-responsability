package padroescomportamentais.chainofresponsability;

public class AuditorMedico extends Auditor {

    public AuditorMedico(Auditor superior) {
        procedimentosAutorizados.add(TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia());
        setAuditorSuperior(superior);
    }

    @Override
    public String getDescricaoCargo() {
        return "Médico Auditor";
    }
}
