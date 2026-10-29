package padroescomportamentais.chainofresponsability;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditorTest {

    private Auditor atendente;
    private Auditor enfermeiro;
    private Auditor medico;
    private Auditor diretor;

    private final TipoProcedimento consulta = TipoProcedimentoConsulta.getTipoProcedimentoConsulta();
    private final TipoProcedimento exame = TipoProcedimentoExame.getTipoProcedimentoExame();
    private final TipoProcedimento cirurgia = TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia();
    private final TipoProcedimento internacao = TipoProcedimentoInternacao.getTipoProcedimentoInternacao();
    private final TipoProcedimento experimental = TipoProcedimentoExperimental.getTipoProcedimentoExperimental();

    /** Cadeia completa: Atendente -> Enfermeiro -> Médico -> Diretor Clínico. */
    @BeforeEach
    void montarCadeia() {
        diretor = new AuditorDiretorClinico(null);
        medico = new AuditorMedico(diretor);
        enfermeiro = new AuditorEnfermeiro(medico);
        atendente = new AuditorAtendente(enfermeiro);
    }

    // ---------- cadeia completa a partir do início ----------

    @Test
    void consultaEhAutorizadaPeloAtendente() {
        assertEquals("Atendente", atendente.autorizar(new Solicitacao(consulta)));
    }

    @Test
    void exameEhAutorizadoPeloEnfermeiro() {
        assertEquals("Enfermeiro Auditor", atendente.autorizar(new Solicitacao(exame)));
    }

    @Test
    void cirurgiaEhAutorizadaPeloMedico() {
        assertEquals("Médico Auditor", atendente.autorizar(new Solicitacao(cirurgia)));
    }

    @Test
    void internacaoEhAutorizadaPeloDiretorClinico() {
        assertEquals("Diretor Clínico", atendente.autorizar(new Solicitacao(internacao)));
    }

    @Test
    void procedimentoExperimentalNaoTemAutorizacao() {
        assertEquals("Sem autorização", atendente.autorizar(new Solicitacao(experimental)));
    }

    // ---------- cada elo isolado ----------

    @Test
    void cadaAuditorAutorizaSeuProprioTipoSemPrecisarDaCadeia() {
        assertEquals("Atendente", new AuditorAtendente(null).autorizar(new Solicitacao(consulta)));
        assertEquals("Enfermeiro Auditor", new AuditorEnfermeiro(null).autorizar(new Solicitacao(exame)));
        assertEquals("Médico Auditor", new AuditorMedico(null).autorizar(new Solicitacao(cirurgia)));
        assertEquals("Diretor Clínico", new AuditorDiretorClinico(null).autorizar(new Solicitacao(internacao)));
    }

    @Test
    void ultimoEloSemSuperiorRetornaSemAutorizacao() {
        assertEquals("Sem autorização", diretor.autorizar(new Solicitacao(consulta)));
        assertNull(diretor.getAuditorSuperior());
    }

    @Test
    void solicitacaoSoPercorreAuditoresAcimaDeQuemRecebeu() {
        // consulta é do Atendente, que está ABAIXO do médico na cadeia
        assertEquals("Sem autorização", medico.autorizar(new Solicitacao(consulta)));
        assertEquals("Sem autorização", enfermeiro.autorizar(new Solicitacao(consulta)));
    }

    @Test
    void solicitacaoIniciadaNoMeioDaCadeiaSobeAteOCompetente() {
        assertEquals("Médico Auditor", enfermeiro.autorizar(new Solicitacao(cirurgia)));
        assertEquals("Diretor Clínico", enfermeiro.autorizar(new Solicitacao(internacao)));
    }

    // ---------- estrutura da cadeia ----------

    @Test
    void getAuditorSuperiorRetornaOProximoElo() {
        assertSame(enfermeiro, atendente.getAuditorSuperior());
        assertSame(medico, enfermeiro.getAuditorSuperior());
        assertSame(diretor, medico.getAuditorSuperior());
    }

    @Test
    void cadeiaPodeSerAlteradaEmTempoDeExecucao() {
        Auditor isolado = new AuditorAtendente(null);
        Solicitacao pedidoCirurgia = new Solicitacao(cirurgia);

        assertEquals("Sem autorização", isolado.autorizar(pedidoCirurgia));

        isolado.setAuditorSuperior(medico);
        assertEquals("Médico Auditor", isolado.autorizar(pedidoCirurgia));
    }

    @Test
    void auditorNaoPodeSerSuperiorDeSiMesmo() {
        assertThrows(IllegalArgumentException.class, () -> atendente.setAuditorSuperior(atendente));
    }

    @Test
    void cadeiaNaoAceitaCiclos() {
        // diretor -> atendente fecharia o ciclo atendente -> ... -> diretor -> atendente
        assertThrows(IllegalArgumentException.class, () -> diretor.setAuditorSuperior(atendente));
        assertNull(diretor.getAuditorSuperior());
    }

    @Test
    void primeiroAuditorCompetenteDaCadeiaVence() {
        Auditor geral = new AuditorGeral(atendente); // aceita consulta e exame, vem antes do Atendente
        assertEquals("Auditor Geral", geral.autorizar(new Solicitacao(consulta)));
        assertEquals("Auditor Geral", geral.autorizar(new Solicitacao(exame)));
        assertEquals("Médico Auditor", geral.autorizar(new Solicitacao(cirurgia)));
    }

    // ---------- entradas inválidas ----------

    @Test
    void solicitacaoNulaLancaExcecao() {
        assertThrows(NullPointerException.class, () -> atendente.autorizar(null));
    }

    /** Auditor de teste que atende mais de um tipo de procedimento. */
    private static class AuditorGeral extends Auditor {
        AuditorGeral(Auditor superior) {
            procedimentosAutorizados.add(TipoProcedimentoConsulta.getTipoProcedimentoConsulta());
            procedimentosAutorizados.add(TipoProcedimentoExame.getTipoProcedimentoExame());
            setAuditorSuperior(superior);
        }

        @Override
        public String getDescricaoCargo() {
            return "Auditor Geral";
        }
    }
}
