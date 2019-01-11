package br.com.cronos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import br.com.cronos.modelo.ProfessorCurso;

public class DAOUsuario {
	private static EntityManager entityManager;

	public List<ProfessorCurso> buscarCursosProfessor(String condicao) {
		Query q = null;
		List<ProfessorCurso> lista = new ArrayList<ProfessorCurso>();
		try {
			entityManager = ConexaoBanco.getConexao().getEm();
			q = entityManager.createQuery(
					"from ProfessorCurso where " + condicao + " and curso.status is true and status is true");
			lista = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	public List retornarLogado(Class classe, String usuario) {
		Query q = null;
		try {
			entityManager = ConexaoBanco.getConexao().getEm();
			q = entityManager.createQuery(
					"from " + classe.getSimpleName() + " where status = true and usuario = '" + usuario + "'");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return q.getResultList();
	}

}
