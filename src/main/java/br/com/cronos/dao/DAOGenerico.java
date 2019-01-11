package br.com.cronos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DAOGenerico {
	private static EntityManager entityManager;

	public DAOGenerico() {
		entityManager = ConexaoBanco.getConexao().getEm();
	}

	public void inserir(Object objeto) {
		try {
			if (!entityManager.getTransaction().isActive())
				entityManager.getTransaction().begin();
			entityManager.persist(objeto);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public void alterar(Object objeto) {
		try {
			if (!entityManager.getTransaction().isActive())
				entityManager.getTransaction().begin();
			entityManager.merge(objeto);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public List listaComStatus(Class classe) {
		Query query = null;
		try {
			query = entityManager.createQuery("from " + classe.getSimpleName() + " where status is true order by id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List listar(Class classe) {
		Query query = null;
		try {
			query = entityManager.createQuery("from " + classe.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public List listar(Class classe, String condicao) {
		Query query = null;
		try {
			query = entityManager
					.createQuery("from " + classe.getSimpleName() + " where status is true and " + condicao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	public Object buscarPorId(Class classe, Long id) {
		Object retornando = null;
		try {
			retornando = entityManager.find(classe, id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retornando;
	}

	public void updateSenha(String senha, String email) {
		String sql = "";
		try {
			entityManager.getTransaction().begin();
			sql = ("update Pessoa set senha = '" + senha + "' where usuario like '" + email + "'");
			int update = entityManager.createQuery(sql).executeUpdate();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}

	}

	public void update(String alteracao) {
		String sql = "";
		try {
			entityManager.getTransaction().begin();
			sql = ("update " + alteracao);
			int update = entityManager.createQuery(sql).executeUpdate();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}
}
