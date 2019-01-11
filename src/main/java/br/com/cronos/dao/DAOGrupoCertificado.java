package br.com.cronos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DAOGrupoCertificado {

	private static EntityManager entityManager;

	public DAOGrupoCertificado() {
		entityManager = ConexaoBanco.getConexao().getEm();
	}

	public List recuperarGrupoAtividade(Long id) {
		Query query = null;
		try {
			query = entityManager.createQuery("from Atividade e where e.status is true and e = " + id);
		} catch (Exception e) {
			System.err.println("Erro recuperarGrupo");
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List recuperarGrupoTurma(Long idTurma, Long idGrupo) {
		Query query = null;
		try {
			query = entityManager.createQuery(
					"from GrupoTurma a where a.status is true and a.turma = " + idTurma + " and a.grupo = " + idGrupo);
		} catch (Exception e) {
			System.err.println("Erro recuperarGrupoTurma");
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List recuperarAtividade(Long idGrupo) {
		Query query = null;
		try {
			query = entityManager.createQuery("from Atividade b where b.status is true and b.grupo = " + idGrupo);
		} catch (Exception e) {
			System.err.println("Erro recuperarAtividade");
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List recuperarAtividadeTurmas(Long idAtividade, Long idTurma) {
		Query query = null;
		try {
			query = entityManager.createQuery("from AtividadeTurma c where c.status is true and c.atividade = "
					+ idAtividade + " and c.turma = " + idTurma);
		} catch (Exception e) {
			System.err.println("Erro recuperarAtividadeTurmas");
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List recuperarCertificados(Long idAluno, Long idAtividadeTurma) {
		Query query = null;
		try {
			query = entityManager.createQuery("from Certificado d where d.status is true and d.aluno = " + idAluno
					+ " and d.atividadeTurma = " + idAtividadeTurma + " and d.situacao = 3");
		} catch (Exception e) {
			System.err.println("Erro recuperarCertificados");
			e.printStackTrace();
		}
		return query.getResultList();
	}
}
