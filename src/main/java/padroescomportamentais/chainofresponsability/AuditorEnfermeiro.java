package padroescomportamentais.chainofresponsability;

public class AuditorEnfermeiro extends Auditor {

    public AuditorEnfermeiro(Auditor superior) {
        procedimentosAutorizados.add(TipoProcedimentoExame.getTipoProcedimentoExame());
        setAuditorSuperior(superior);
    }

    @Override
    public String getDescricaoCargo() {
        return "Enfermeiro Auditor";
    }
}
