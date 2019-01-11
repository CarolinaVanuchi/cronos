package br.com.cronos.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.internal.EntityManagerImpl;

public class ConexaoBanco {
	private static ConexaoBanco conexao;
	private EntityManager em;

	public ConexaoBanco() {
		em = Persistence.createEntityManagerFactory("cronosPU").createEntityManager();
	}

	public synchronized static ConexaoBanco getConexao() {
		if (conexao == null) {
			conexao = new ConexaoBanco();
		}
		return conexao;
	}

	public EntityManager getEm() {
		return em;
	}

	public Connection getConnection() {
		EntityManagerImpl factory = (EntityManagerImpl) em;
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) factory.getSession().getSessionFactory();
		try {
			return sessionFactoryImpl.getConnectionProvider().getConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ConexaoBanco.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
