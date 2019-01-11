package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.dao.DAOUsuario;
import br.com.cronos.modelo.Curso;
import br.com.cronos.modelo.Professor;
import br.com.cronos.modelo.ProfessorCurso;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@SessionScoped
@ManagedBean
public class ProfessorMB {
	private Professor professor;
	private ProfessorCurso professorCurso;
	private List<Professor> professores;
	private DAOGenerico dao;
	private Curso curso;
	private List<Curso> cursos;
	private List<Curso> cursosSelecionados;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	private DAOUsuario daoUsuarios;

	public ProfessorMB() {
		dao = new DAOGenerico();
		professor = new Professor();
		professorCurso = new ProfessorCurso();
		professores = new ArrayList<>();
		cursos = new ArrayList<>();
		cursosSelecionados = new ArrayList<>();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		daoUsuarios = new DAOUsuario();
		curso = new Curso();
		preencherListaProfessor();
	}

	public void salvar() {
		try {
			if (professor.getId() == null) {
				if (validacoesGerirUsuarios.buscarUsuarios(professor)) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else if (validacoesGerirUsuarios.buscarSiape(professor)) {
					ExibirMensagem.exibirMensagem(Mensagem.SIAPE);
				} else {
					if (professor.getSenha().isEmpty()) {
						professor.setSenha("123");
					}
					professor.setStatus(true);
					professor.setDataCadastro(new Date());
					professor.setPerfil("PROFESSOR");
					professor.setSenha(CriptografiaSenha.criptografar(professor.getSenha()));
					dao.inserir(professor);
					for (Curso c : cursosSelecionados) {
						professorCurso.setStatus(true);
						professorCurso.setProfessor(professor);
						professorCurso.setCurso(c);
						dao.inserir(professorCurso);
						professorCurso = new ProfessorCurso();
					}
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogProfessor();
					preencherListaProfessor();
				}
			} else {
				if ((validacoesGerirUsuarios.buscarUsuarios(professor))
						&& (validacoesGerirUsuarios.buscarUsuarioAlterar(professor))) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else if ((validacoesGerirUsuarios.buscarSiape(professor))
						&& (validacoesGerirUsuarios.buscarSiapeAlterar(professor))) {
					ExibirMensagem.exibirMensagem(Mensagem.SIAPE);
				} else {
					if (professor.getSenha().isEmpty()) {
						professor.setSenha("123");
					}
					professor.setSenha(CriptografiaSenha.criptografar(professor.getSenha()));
					dao.alterar(professor);					
					for (Curso c : cursosSelecionados) {
						professorCurso.setStatus(true);
						professorCurso.setProfessor(professor);
						professorCurso.setCurso(c);
						if (validarRelacionarCursos(professorCurso)) {
							dao.inserir(professorCurso);
						}
						professorCurso = new ProfessorCurso();
					}
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogProfessor();
					preencherListaProfessor();
				}
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(Professor professor) {
		try {
			professor.setStatus(false);
			dao.alterar(professor);
			preencherListaProfessor();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovoObjeto() {
		professor = new Professor();
		cursosSelecionados = new ArrayList<>();
		preencherListaCurso();
	}

	public void preencherListaProfessor() {
		professores = dao.listaComStatus(Professor.class);
	}

	public void preencherListaCurso() {
		cursos = dao.listaComStatus(Curso.class);
	}

	public void abrirDetalhes(Professor professor) {
		this.professor = professor;
		preencherCursoProfessor(professor);
	}

	public void removerCurso(Curso curso) {
		ProfessorCurso pc = new ProfessorCurso();
		pc = (ProfessorCurso) dao
				.listar(ProfessorCurso.class,
						"curso = " + curso.getId() + " and professor = " + professor.getId() + " and status = true")
				.get(0);
		if (pc != null) {
			pc.setStatus(false);
			dao.alterar(pc);
			preencherCursoProfessor(professor);

		}
	}

	public void preencherCursoProfessor(Professor pro) {
		this.professor = pro;
		preencherListaCurso();
		cursosSelecionados.clear();
		for (ProfessorCurso c : daoUsuarios.buscarCursosProfessor(" professor = " + professor.getId())) {
			cursosSelecionados.add(c.getCurso());
		}

	}

	private Boolean validarRelacionarCursos(ProfessorCurso professorCurso) {
		List<ProfessorCurso> professorCursos = new ArrayList<>();
		try {
			professorCursos = dao.listar(ProfessorCurso.class, " curso = " + professorCurso.getCurso().getId()
					+ " and professor = " + professorCurso.getProfessor().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (professorCursos.isEmpty())
			return true;
		return false;
	}

	public List<Curso> completarCursos(String str) {
		preencherListaCurso();
		List<Curso> cursosSelecionados = new ArrayList<>();
		for (Curso cur : cursos) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				cursosSelecionados.add(cur);
			}
		}
		return cursosSelecionados;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public ProfessorCurso getProfessorCurso() {
		return professorCurso;
	}

	public void setProfessorCurso(ProfessorCurso professorCurso) {
		this.professorCurso = professorCurso;
	}

	public List<Professor> getProfessores() {
		return professores;
	}

	public void setProfessores(List<Professor> professores) {
		this.professores = professores;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public List<Curso> getCursosSelecionados() {
		return cursosSelecionados;
	}

	public void setCursosSelecionados(List<Curso> cursosSelecionados) {
		this.cursosSelecionados = cursosSelecionados;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
}
