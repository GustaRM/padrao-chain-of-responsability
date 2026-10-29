package padroescomportamentais.chainofresponsability;

public class AuditorDiretorClinico extends Auditor {

    public AuditorDiretorClinico(Auditor superior) {
        procedimentosAutorizados.add(TipoProcedimentoInternacao.getTipoProcedimentoInternacao());
        setAuditorSuperior(superior);
    }

    @Override
    public String getDescricaoCargo() {
        return "Diretor Clínico";
    }
}
