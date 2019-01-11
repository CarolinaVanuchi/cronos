package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.core.env.SystemEnvironmentPropertySource;

import br.com.cronos.controle.UsuarioSessaoMB;
import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.dao.DAOMovimentacaoAluno;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.Curso;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.modelo.Movimentacao;
import br.com.cronos.modelo.ProfessorCurso;
import br.com.cronos.modelo.Turma;

public class RecuperarRelacoesProfessor {
	private UsuarioSessaoMB usuarioSessao;
	private List<ProfessorCurso> professorCursos;
	private List<Turma> turmas;
	private List<Curso> cursos;
	private List<GrupoTurma> grupoTurmas;
	private List<AtividadeTurma> atividadeTurmas;
	private List<Certificado> certificados;
	private DAOGenerico dao;

	public RecuperarRelacoesProfessor() {
		usuarioSessao = new UsuarioSessaoMB();
		professorCursos = new ArrayList<>();
		turmas = new ArrayList<>();
		cursos = new ArrayList<>();
		grupoTurmas = new ArrayList<>();
		certificados = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();
		dao = new DAOGenerico();
	}

	public List<Turma> recuperarTurmasProfessor() {
		try {
			professorCursos = new ArrayList<>();
			professorCursos = dao.listar(ProfessorCurso.class,
					" professor = " + usuarioSessao.recuperarProfessor().getId());
			for (ProfessorCurso pc : professorCursos) {
				turmas.addAll(dao.listar(Turma.class, " curso = " + pc.getCurso().getId()));
			}

		} catch (Exception e) {
			System.err.println("Erro recuperarTurmasProfessor ");
			e.printStackTrace();
		}
		return turmas;
	}

	public List<GrupoTurma> recuperarGrupoTurmasProfessor() {
		try {
			recuperarTurmasProfessor();
			for (Turma t : turmas) {
				grupoTurmas.addAll(dao.listar(GrupoTurma.class, " turma = " + t.getId()));
			}
		} catch (Exception e) {
			System.err.println("Erro recuperarGrupoTurmasProfessor ");
			e.printStackTrace();
		}
		return grupoTurmas;
	}

	public List<AtividadeTurma> recuperarAtividadeTurmasProfessor() {
		try {
			recuperarTurmasProfessor();
			for (Turma t : turmas) {
				atividadeTurmas.addAll(dao.listar(AtividadeTurma.class, " turma = " + t.getId()));
			}
		} catch (Exception e) {
			System.err.println("Erro recuperarAtividadeTurmasProfessor ");
			e.printStackTrace();
		}
		return atividadeTurmas;
	}

	public List<Curso> recuperarCursosProfessor() {
		try {
			professorCursos = new ArrayList<>();
			professorCursos = dao.listar(ProfessorCurso.class,
					" professor = " + usuarioSessao.recuperarProfessor().getId());
			for (ProfessorCurso pc : professorCursos) {
				cursos.addAll(dao.listar(Curso.class, " id = " + pc.getCurso().getId()));
			}

		} catch (Exception e) {
			System.err.println("Erro recuperarCursosProfessor ");
			e.printStackTrace();
		}
		return cursos;
	}

	public List<Certificado> recuperarCertificados(Integer situacao) {
		try {
			recuperarTurmasProfessor();
			for (Turma t : turmas) {
				certificados.addAll(dao.listar(Certificado.class,
						" situacao = " + situacao + " and atividadeTurma.turma = " + t.getId()));
			}
		} catch (Exception e) {
			System.err.println("Erro recuperarCertificados ");
			e.printStackTrace();
		}
		return certificados;
	}

	public List<Movimentacao> recuperarAlunoMovimentacaoAtivo() {
		List<Movimentacao> alunosAtivos = new ArrayList<>();
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		List<Movimentacao> alunosAtivosProfessor = new ArrayList<>();
		DAOMovimentacaoAluno daoMovimentacaoAluno = new DAOMovimentacaoAluno();
		List<AlunoTurma> alunosTurmasTurma = new ArrayList<>();
		turmas = new ArrayList<>();
		recuperarTurmasProfessor();
		try {
			alunosAtivos = daoMovimentacaoAluno.buscarAtivo();
			alunosTurmas = daoMovimentacaoAluno.listarMaioresAlunoTurma();

			for (int m = 0; m <= alunosTurmas.size() - 1; m++) {
				for (int v = 0; v <= turmas.size() - 1; v++) {
					if (alunosTurmas.get(m).getTurma().getId() == turmas.get(v).getId()) {
						alunosTurmasTurma.add(alunosTurmas.get(m));
					}
				}
			}

			for (int c = 0; c <= alunosAtivos.size() - 1; c++) {
				for (int s = 0; s <= alunosTurmasTurma.size() - 1; s++) {
					if (alunosAtivos.get(c).getAluno().getId() == alunosTurmasTurma.get(s).getAluno().getId()) {
						alunosAtivosProfessor.add(alunosAtivos.get(c));
					}
				}
			}

		} catch (

		Exception e) {
			System.err.println("Erro recuperarAlunoMovimentacaoAtivo ");
			e.printStackTrace();
		}

		return alunosAtivosProfessor;
	}

	public List<Movimentacao> recuperarAlunoMovimentacaoTrancados() {
		List<Movimentacao> alunosTrancados = new ArrayList<>();
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		DAOMovimentacaoAluno daoMovimentacaoAluno = new DAOMovimentacaoAluno();
		List<AlunoTurma> alunosTurmasTurma = new ArrayList<>();
		List<Movimentacao> alunosTrancadosProfessor = new ArrayList<>();
		turmas = new ArrayList<>();
		recuperarTurmasProfessor();

		try {
			alunosTrancados = daoMovimentacaoAluno.buscarTrancado();

			alunosTurmas = daoMovimentacaoAluno.listarMaioresAlunoTurma();

			for (int m = 0; m <= alunosTurmas.size() - 1; m++) {
				for (int v = 0; v <= turmas.size() - 1; v++) {
					if (alunosTurmas.get(m).getTurma().getId() == turmas.get(v).getId()) {
						alunosTurmasTurma.add(alunosTurmas.get(m));
					}
				}
			}

			for (int c = 0; c <= alunosTrancados.size() - 1; c++) {
				for (int s = 0; s <= alunosTurmasTurma.size() - 1; s++) {
					if (alunosTrancados.get(c).getAluno().getId() == alunosTurmasTurma.get(s).getAluno().getId()) {
						alunosTrancadosProfessor.add(alunosTrancados.get(c));
					}
				}
			}

		} catch (Exception e) {
			System.err.println("Erro recuperarAlunoMovimentacaoTrancados");
			e.printStackTrace();
		}

		return alunosTrancadosProfessor;
	}

	public List<AlunoTurma> recuperarTodosAlunosTurmasAtivas() {
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		recuperarAtividadeTurmasProfessor();
		try {
			for (Turma t : turmas) {
				alunosTurmas.addAll(dao.listar(AlunoTurma.class, " turma = " + t.getId()));
			}
		} catch (Exception e) {
			System.err.println("Erro recuperarTodosAlunosTurmasAtivas");
			e.printStackTrace();
		}
		return alunosTurmas;
	}

	public List<Movimentacao> recuperarTodasMovimentacoesAtivas() {
		List<Movimentacao> todosAlunos = new ArrayList<>();
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		DAOMovimentacaoAluno daoMovimentacaoAluno = new DAOMovimentacaoAluno();
		List<AlunoTurma> alunosTurmasTurma = new ArrayList<>();
		List<Movimentacao> alunos = new ArrayList<>();
		turmas = new ArrayList<>();
		recuperarTurmasProfessor();

		try {
			todosAlunos = dao.listaComStatus(Movimentacao.class);

			alunosTurmas = dao.listaComStatus(AlunoTurma.class);

			for (int m = 0; m <= alunosTurmas.size() - 1; m++) {
				for (int v = 0; v <= turmas.size() - 1; v++) {
					if (alunosTurmas.get(m).getTurma().getId() == turmas.get(v).getId()) {
						alunosTurmasTurma.add(alunosTurmas.get(m));
					}
				}
			}

			for (int c = 0; c <= todosAlunos.size() - 1; c++) {
				for (int s = 0; s <= alunosTurmasTurma.size() - 1; s++) {
					if (todosAlunos.get(c).getAluno().getId() == alunosTurmasTurma.get(s).getAluno().getId()) {
						alunos.add(todosAlunos.get(c));
					}
				}
			}

		} catch (Exception e) {
			System.err.println("Erro recuperarTodasMovimentacoesAtivas");
			e.printStackTrace();
		}

		return alunos;
	}
}
