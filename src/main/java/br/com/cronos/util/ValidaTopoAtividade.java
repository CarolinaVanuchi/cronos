package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOFiltros;
import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.Certificado;

public class ValidaTopoAtividade {
	private DAOFiltros daoFiltros;
	private List<Certificado> certificados;
	private Double somaCertificado;

	public ValidaTopoAtividade() {
		daoFiltros = new DAOFiltros();
		certificados = new ArrayList<>();
		somaCertificado = 0.0;
	}

	public Boolean calcularTotalAtividade(Certificado certificado) {
		try {
			certificados = daoFiltros.certificadosAlunosComAtividade(certificado.getAluno().getId(), 3,
					certificado.getAtividadeTurma().getId());

			for (Certificado c : certificados) {
				somaCertificado = somaCertificado + c.getHoraComputada();
			}

			if ((certificado.getAtividadeTurma().getHoraUnica())
					&& (certificado.getAtividadeTurma().getQuantidadeHoraUnica() != null)) {
				if (somaCertificado > certificado.getAtividadeTurma().getQuantidadeHoraUnica()) {
					return false;
				}
			} else if (certificado.getAtividadeTurma().getMaximoHoras() != null){
				if (somaCertificado >= certificado.getAtividadeTurma().getMaximoHoras()) {
					return false;
				}
			}
		} catch (Exception e) {
			System.err.println("Erro calcularTotalAtividade");
			e.printStackTrace();
		}
		return true;

	}
}
