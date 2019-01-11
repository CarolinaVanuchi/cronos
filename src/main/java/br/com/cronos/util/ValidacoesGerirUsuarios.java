package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.dao.DAOUsuario;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.Pessoa;
import br.com.cronos.modelo.Professor;
import br.com.cronos.modelo.Secretaria;

public class ValidacoesGerirUsuarios {
	private DAOGenerico dao;

	public ValidacoesGerirUsuarios() {
		dao = new DAOGenerico();
	}

	public Boolean buscarUsuarios(Pessoa pessoa) {
		List<Pessoa> pessoas = new ArrayList<>();
		try {
			pessoas = dao.listar(Pessoa.class, " usuario = '" + pessoa.getUsuario() + "'");
			if (pessoas.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarUsuarios");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean buscarUsuarioAlterar(Pessoa pessoa) {
		List<Pessoa> pessoas = new ArrayList<>();
		pessoas = dao.listar(Pessoa.class,
				" usuario = '" + pessoa.getUsuario() + "' and id = '" + pessoa.getId() + "'");
		if (pessoas.size() > 0) {
			return false;
		}
		return true;
	}

	public Boolean buscarSiape(Professor professor) {
		List<Professor> professores = new ArrayList<>();
		List<Secretaria> secretarias = new ArrayList<>();
		try {
			professores = dao.listar(Professor.class, " siape = '" + professor.getSiape() + "'");
			secretarias = dao.listar(Secretaria.class, " siape = '" + professor.getSiape() + "'");
			if (!professores.isEmpty() || !secretarias.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarSiape ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean buscarSiapeAlterar(Professor professor) {
		List<Professor> professores = new ArrayList<>();
		try {
			professores = dao.listar(Professor.class,
					" siape = '" + professor.getSiape() + "' and id = '" + professor.getId() + "'");
			if (professores.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarSiapeAlterar ");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean buscarRA(Aluno aluno) {
		List<Aluno> alunos = new ArrayList<>();
		try {
			alunos = dao.listar(Aluno.class, " ra = '" + aluno.getRa() + "'");
			if (alunos.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarRA");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean buscarRaAlterar(Aluno aluno) {
		List<Aluno> alunos = new ArrayList<>();
		try {
			alunos = dao.listar(Aluno.class, " ra = '" + aluno.getRa() + "' and id = '" + aluno.getId() + "'");
			if (alunos.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarRaAlterar");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean buscarSiape(Secretaria secretaria) {
		List<Secretaria> secretarias = new ArrayList<>();
		List<Professor> professores = new ArrayList<>();
		try {
			secretarias = dao.listar(Secretaria.class, " siape = '" + secretaria.getSiape() + "'");
			professores = dao.listar(Professor.class, " siape = '" + secretaria.getSiape() + "'");
			if ((!secretarias.isEmpty()) || (!professores.isEmpty())) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarSiape Secretaria");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean buscarSiapeAlterar(Secretaria secretaria) {
		List<Secretaria> secretarias = new ArrayList<>();
		try {
			secretarias = dao.listar(Secretaria.class,
					" siape = '" + secretaria.getSiape() + "' and id = '" + secretaria.getId() + "'");
			if (secretarias.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarSiapeAlterar Secretaria");
			e.printStackTrace();
		}
		return true;
	}

}
