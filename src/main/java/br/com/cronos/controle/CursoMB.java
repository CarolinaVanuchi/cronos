package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Curso;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.PermiteInativar;

@SessionScoped
@ManagedBean
public class CursoMB {

	private DAOGenerico dao;
	private Curso curso;
	private List<Curso> cursos;
	private Date dataFechamento;
	private PermiteInativar permiteInativar;

	public CursoMB() {
		dao = new DAOGenerico();
		cursos = new ArrayList<>();
		criarNovoCurso();
		preencherListaCurso();
	}

	public void salvar() {
		try {
			if (curso.getId() == null) {
				curso.setStatus(true);
				curso.setDataCadastro(new Date());
				dao.inserir(curso);
				criarNovoCurso();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaCurso();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				dao.alterar(curso);
				criarNovoCurso();
				preencherListaCurso();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		FecharDialog.fecharDialogCurso();
	}

	public void inativar(Curso curso) {
		permiteInativar = new PermiteInativar();
		try {
			if (permiteInativar.verificarCursoComTurma(curso.getId())) {
				if (permiteInativar.verificarCursoComProfessorCurso(curso.getId())) {
					curso.setStatus(false);
					dao.alterar(curso);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					preencherListaCurso();
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.CURSO_COM_PROFESSOR_CURSO);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.CURSO_COM_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void preencherListaCurso() {
		cursos = dao.listaComStatus(Curso.class);
	}

	public void criarNovoCurso() {
		curso = new Curso();
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
}
