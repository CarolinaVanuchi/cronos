package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOGrupoCertificado;
import br.com.cronos.modelo.Atividade;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.Grupo;
import br.com.cronos.modelo.GrupoTurma;

public class ValidaTopoGrupo {

	private DAOGrupoCertificado daoGrupoCertificado;
	private Grupo grupo;
	private GrupoTurma grupoTurma;
	private List<Atividade> atividadesParaGrupo;
	private List<Atividade> atividades;
	private List<AtividadeTurma> atividadesTurmas;
	private List<Certificado> certificados;
	private Double somaCertificado;

	public ValidaTopoGrupo() {
		daoGrupoCertificado = new DAOGrupoCertificado();
		grupo = new Grupo();
		grupoTurma = new GrupoTurma();
		atividades = new ArrayList<>();
		atividadesParaGrupo = new ArrayList<>();
		atividadesTurmas = new ArrayList<>();
		certificados = new ArrayList<>();
		somaCertificado = 0.0;
	}

	public Boolean calcularTotalGrupo(Certificado certificado) {
		try {
			atividadesParaGrupo = daoGrupoCertificado
					.recuperarGrupoAtividade(certificado.getAtividadeTurma().getAtividade().getId());
			grupo = atividadesParaGrupo.get(0).getGrupo();

			grupoTurma = (GrupoTurma) daoGrupoCertificado
					.recuperarGrupoTurma(certificado.getAtividadeTurma().getTurma().getId(), grupo.getId()).get(0);

			atividades = daoGrupoCertificado.recuperarAtividade(grupoTurma.getGrupo().getId());

			for (Atividade a : atividades) {
				atividadesTurmas.addAll(daoGrupoCertificado.recuperarAtividadeTurmas(a.getId(),
						certificado.getAtividadeTurma().getTurma().getId()));
			}
			for (AtividadeTurma a : atividadesTurmas) {
				certificados
						.addAll(daoGrupoCertificado.recuperarCertificados(certificado.getAluno().getId(), a.getId()));
			}

			for (Certificado c : certificados) {
				somaCertificado = somaCertificado + c.getHoraComputada();
			}

			if (somaCertificado > grupoTurma.getMaximoHoras()) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro calcularTotalGrupo");
			e.printStackTrace();
		}
		return true;
	}

}
