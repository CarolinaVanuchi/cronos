package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Professor;
import br.com.cronos.util.RecuperarRelacoesProfessor;

@ViewScoped
@ManagedBean
public class AlunoTurmaMB {

	private List<AlunoTurma> alunoTurmaSecretaria;
	private List<AlunoTurma> alunoTurmaAluno;
	private List<AlunoTurma> alunoTurmaProfessor;
	private UsuarioSessaoMB usuarioSessao;
	private Aluno aluno;
	private DAOGenerico dao;

	public AlunoTurmaMB() {
		alunoTurmaSecretaria = new ArrayList<>();
		alunoTurmaAluno = new ArrayList<>();
		usuarioSessao = new UsuarioSessaoMB();
		dao = new DAOGenerico();
	}

	public void preencherAlunoTurmasSecretaria() {
		alunoTurmaSecretaria = dao.listaComStatus(AlunoTurma.class);
	}

	public void preencherAlunoTurmasAluno() {
		aluno = new Aluno();
		aluno = (Aluno) usuarioSessao.recuperarAluno();
		alunoTurmaAluno = dao.listar(AlunoTurma.class, " aluno = " + aluno.getId());
	}

	public void preencherAlunoTurmasProfessor() {
		RecuperarRelacoesProfessor recuperarRelacoesProfessor = new RecuperarRelacoesProfessor();
		alunoTurmaProfessor = recuperarRelacoesProfessor.recuperarTodosAlunosTurmasAtivas();
	}

	public List<AlunoTurma> getAlunoTurmaSecretaria() {
		return alunoTurmaSecretaria;
	}

	public void setAlunoTurmaSecretaria(List<AlunoTurma> alunoTurmaSecretaria) {
		this.alunoTurmaSecretaria = alunoTurmaSecretaria;
	}

	public List<AlunoTurma> getAlunoTurmaAluno() {
		return alunoTurmaAluno;
	}

	public void setAlunoTurmaAluno(List<AlunoTurma> alunoTurmaAluno) {
		this.alunoTurmaAluno = alunoTurmaAluno;
	}

	public List<AlunoTurma> getAlunoTurmaProfessor() {
		return alunoTurmaProfessor;
	}

	public void setAlunoTurmaProfessor(List<AlunoTurma> alunoTurmaProfessor) {
		this.alunoTurmaProfessor = alunoTurmaProfessor;
	}
}
