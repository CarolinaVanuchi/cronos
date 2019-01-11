package br.com.cronos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Certificado;

public class DAOFiltros {
	private static EntityManager entityManager;

	public DAOFiltros() {
		entityManager = ConexaoBanco.getConexao().getEm();
	}

	public List certificadosAlunos(Long idAluno, int sit) {
		Query query = null;
		try {
			query = entityManager.createQuery(
					"from Certificado a where a.status is true and a.aluno = " + idAluno + " and a.situacao = " + sit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List certificadosAlunosComAtividade(Long idAluno, int sit, Long idAtividadeTurma) {
		Query query = null;
		try {
			query = entityManager.createQuery(
					"from Certificado a where a.status is true and a.aluno = " + idAluno + " and a.situacao = " + sit
							+ " and atividadeTurma = " + idAtividadeTurma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List atividadesTurmaAluno(Long idTurma) {
		Query query = null;
		try {
			query = entityManager.createQuery("from AtividadeTurma a where a.status is true and a.turma = " + idTurma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List gruposTurmaAluno(Long idTurma) {
		Query query = null;
		try {
			query = entityManager.createQuery("from GrupoTurma a where a.status is true and a.turma = " + idTurma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List buscarTurmaAluno(Long idAluno) {
		Query query = null;
		try {
			query = entityManager.createQuery("from AlunoTurma a where "
					+ " a.dataMudanca = (select max(b.dataMudanca) from AlunoTurma b where b.aluno = " + idAluno + ") "
					+ " and a.aluno = " + idAluno + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}
	
	public List buscarAlunoTurma(Long idTurma) {
		Query query = null;
		try {
			query = entityManager.createQuery("from AlunoTurma a where "
					+ " a.dataMudanca = (select max(b.dataMudanca) from AlunoTurma b where b.turma = " + idTurma + ") "
					+ " and a.turma = " + idTurma + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}
}
