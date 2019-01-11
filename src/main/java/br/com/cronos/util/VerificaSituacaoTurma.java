package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOFiltros;
import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.modelo.Turma;

public class VerificaSituacaoTurma {
	private DAOGenerico dao;
	private List<GrupoTurma> grupoTurmas;
	private List<Certificado> certificados;
	private List<AlunoTurma> alunosTurmas;
	private DAOFiltros daoFiltros;

	public VerificaSituacaoTurma() {
		dao = new DAOGenerico();
		grupoTurmas = new ArrayList<>();
		alunosTurmas = new ArrayList<>();
		certificados = new ArrayList<>();
		daoFiltros = new DAOFiltros();
	}

	public void recuperarGrupoTurma(Turma turma) {
		grupoTurmas = dao.listar(GrupoTurma.class, " turma = " + turma.getId());
	}

	public void recuperarAlunoTurmas(Turma turma) {
		alunosTurmas = daoFiltros.buscarAlunoTurma(turma.getId());
	}

	public void recuperarCertificados(Turma turma) {
		recuperarAlunoTurmas(turma);
		for (AlunoTurma a : alunosTurmas) {
			certificados
					.addAll(dao.listar(Certificado.class, " aluno = " + a.getAluno().getId() + " and situacao = 3"));
			calcularSituacao(turma, a);
			certificados = new ArrayList<>();
		}
	}

	public void calcularSituacao(Turma turma, AlunoTurma alunoTurma) {
		recuperarGrupoTurma(turma);
		List<Boolean> situacoes = new ArrayList<>();
		Boolean situacaoAux = true;
		Double soma = 0.0;
		for (GrupoTurma g : grupoTurmas) {
			for (Certificado c : certificados) {
				if (c.getIdGrupoTurma().equals(g.getId())) {
					if (c.getHoraComputada() != null) {
						soma = c.getHoraComputada() + soma;
					}
				}
			}
			if (soma >= g.getMaximoHoras() && soma >= g.getMinimoHoras()) {
				situacoes.add(true);
			} else {
				situacoes.add(false);
			}
			soma = 0.0;
		}
		for (int i = 0; i <= situacoes.size() - 1; i++) {
			if (situacoes.get(i) == false) {
				situacaoAux = false;
			}
		}

		alterarSituacao(alunoTurma, situacaoAux);
	}

	public void alterarSituacao(AlunoTurma alunoTurma, Boolean situacao) {
		if (situacao) {
			dao.update(" Aluno set situacao = 'Completou' where id = " + alunoTurma.getAluno().getId());
		} else {
			dao.update(" Aluno set situacao = 'Não Completou' where id = " + alunoTurma.getAluno().getId());
		}
	}
}
