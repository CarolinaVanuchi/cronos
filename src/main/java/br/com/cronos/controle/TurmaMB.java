package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Curso;
import br.com.cronos.modelo.Turma;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.PermiteInativar;
import br.com.cronos.util.RecuperarRelacoesProfessor;

@SessionScoped
@ManagedBean
public class TurmaMB {

	private DAOGenerico dao;
	private Turma turma;
	private List<Turma> turmas;
	private List<Curso> cursos;
	private List<Turma> turmasProfessor;
	private List<Curso> cursosProfessor;
	private RecuperarRelacoesProfessor relacoesProfessor;
	private PermiteInativar permiteInativar;

	public TurmaMB() {
		dao = new DAOGenerico();
		turma = new Turma();
		turmas = new ArrayList<>();
		cursos = new ArrayList<>();
		turmasProfessor = new ArrayList<>();
		cursosProfessor = new ArrayList<>();
		criarNovaTurma();
		preencherListaTurma();
		preencherListaTurmaProfessor();
	}

	public void salvar() {
		if (validarDataCursoComTurma().getDataAbertura().after(turma.getDataInicioTurma())) {
			ExibirMensagem.exibirMensagem(Mensagem.DATA_TURMA);
		} else {
			try {
				if (turma.getId() == null) {
					turma.setDataCadastro(new Date());
					turma.setStatus(true);
					dao.inserir(turma);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					dao.alterar(turma);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				}
				FecharDialog.fecharDialogTurma();
				preencherListaTurma();
				preencherListaTurmaProfessor();

			} catch (Exception e) {
				ExibirMensagem.exibirMensagem(Mensagem.ERRO);
				FecharDialog.fecharDialogTurma();
			}
			criarNovaTurma();
		}
	}

	public void inativar(Turma turma) {
		permiteInativar = new PermiteInativar();
		try {
			if (permiteInativar.verificarTurmaComAlunoTurma(turma.getId())) {
				if (permiteInativar.verificarTurmaComAtividadeTurma(turma.getId())) {
					if (permiteInativar.verificarTurmaComGrupoTurma(turma.getId())) {
						turma.setStatus(false);
						dao.alterar(turma);
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						preencherListaTurma();
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.TURMA_COM_GRUPO_TURMA);
					}
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.TURMA_COM_ATIVIDADE_TURMA);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.TURMA_COM_ALUNO_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovaTurma() {
		turma = new Turma();
	}

	public void preencherListaTurma() {
		turmas = dao.listaComStatus(Turma.class);
	}

	public void preencherListaCurso() {
		cursos = dao.listaComStatus(Curso.class);
	}

	public void preencherListaTurmaProfessor() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		turmasProfessor = relacoesProfessor.recuperarTurmasProfessor();
	}

	public void preencherListaCursoProfessor() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		cursosProfessor = relacoesProfessor.recuperarCursosProfessor();
	}

	public Curso validarDataCursoComTurma() {
		Curso curso = new Curso();
		curso = (Curso) dao.listar(Curso.class, " id = " + turma.getCurso().getId()).get(0);
		return curso;
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

	public List<Curso> completarCursosProfessor(String str) {
		preencherListaCursoProfessor();
		List<Curso> cursosSelecionados = new ArrayList<>();
		for (Curso cur : cursosProfessor) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				cursosSelecionados.add(cur);
			}
		}
		return cursosSelecionados;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public List<Turma> getTurmasProfessor() {
		return turmasProfessor;
	}

	public void setTurmasProfessor(List<Turma> turmasProfessor) {
		this.turmasProfessor = turmasProfessor;
	}
}
