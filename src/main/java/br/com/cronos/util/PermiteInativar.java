package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Atividade;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.modelo.ProfessorCurso;
import br.com.cronos.modelo.Turma;

public class PermiteInativar {
	private DAOGenerico dao;

	public Boolean verificarCursoComTurma(Long id) {
		dao = new DAOGenerico();
		List<Turma> turmas = new ArrayList<>();
		try {
			turmas = dao.listar(Turma.class, " curso.id = " + id + " and status is true ");
			if (turmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarCursoComTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarCursoComProfessorCurso(Long id) {
		dao = new DAOGenerico();
		List<ProfessorCurso> professorCursos = new ArrayList<>();
		try {
			professorCursos = dao.listar(ProfessorCurso.class, " curso.id = " + id + " and status is true ");
			if (professorCursos.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarCursoComProfessorCurso ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarTurmaComAtividadeTurma(Long id) {
		dao = new DAOGenerico();
		List<AtividadeTurma> atividadesTurmas = new ArrayList<>();
		try {
			atividadesTurmas = dao.listar(AtividadeTurma.class, " turma.id = " + id + " and status is true ");
			if (atividadesTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarTurmaComAtividadeTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarTurmaComGrupoTurma(Long id) {
		dao = new DAOGenerico();
		List<GrupoTurma> gruposTurmas = new ArrayList<>();
		try {
			gruposTurmas = dao.listar(GrupoTurma.class, " turma.id = " + id + " and status is true ");
			if (gruposTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarTurmaComGrupoTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarTurmaComAlunoTurma(Long id) {
		dao = new DAOGenerico();
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		try {
			alunosTurmas = dao.listar(AlunoTurma.class, " turma.id = " + id + " and aluno.status is true ");
			if (alunosTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarTurmaComAlunoTurma");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarGrupoComGrupoTurma(Long id) {
		dao = new DAOGenerico();
		List<GrupoTurma> gruposTurmas = new ArrayList<>();
		try {
			gruposTurmas = dao.listar(GrupoTurma.class, " grupo.id = " + id + " and status is true ");
			if (gruposTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarGrupoComGrupoTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarGrupoComAtividade(Long id) {
		dao = new DAOGenerico();
		List<Atividade> atividades = new ArrayList<>();
		try {
			atividades = dao.listar(Atividade.class, " grupo.id = " + id + " and status is true ");
			if (atividades.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarGrupoComAtividade ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarAtividadeComAtividadeTurma(Long id) {
		dao = new DAOGenerico();
		List<AtividadeTurma> atividadesTurmas = new ArrayList<>();
		try {
			atividadesTurmas = dao.listar(AtividadeTurma.class, " atividade.id = " + id + " and status is true ");
			if (atividadesTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarAtividadeComAtividadeTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarAtividadeTurmaComCertificado(Long id) {
		dao = new DAOGenerico();
		List<Certificado> certificados = new ArrayList<>();
		try {
			certificados = dao.listar(Certificado.class, " atividadeTurma.id = " + id + " and status is true ");
			if (certificados.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarAtividadeTurmaComCertificado ");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean verificarGrupoTurmaComAtividadeTurma(GrupoTurma grupoTurma) {
		dao = new DAOGenerico();
		List<AtividadeTurma> atividadesTurmas = new ArrayList<>();
		try {
			atividadesTurmas = dao.listar(AtividadeTurma.class, " atividade.grupo.id = " + grupoTurma.getGrupo().getId() + 
					" and turma.id = "+grupoTurma.getTurma().getId() + " and status is true ");
			if (atividadesTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarGrupoTurmaComAtividadeTurma");
			e.printStackTrace();
		}
		return false;
	}
}
