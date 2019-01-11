package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cronos.dao.DAOFiltros;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.AtividadeTurma;

@ViewScoped
@ManagedBean
public class ListaAtividadesTurmaAlunosMB {
	private List<AtividadeTurma> atividadesTurmas;
	private DAOFiltros daoFiltros;
	private UsuarioSessaoMB usuarioSessao;
	private AlunoTurma alunoTurma;
	private AtividadeTurma atividadeTurma;

	public ListaAtividadesTurmaAlunosMB() {
		daoFiltros = new DAOFiltros();
		alunoTurma = new AlunoTurma();
		atividadesTurmas = new ArrayList<>();
		usuarioSessao = new UsuarioSessaoMB();
		atividadeTurma = new AtividadeTurma();
		preencherAtividadesTurmas();
	}

	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}

	public void preencherAtividadesTurmas() {
		atividadesTurmas = daoFiltros.atividadesTurmaAluno(recuperarTurmaAluno());
	}

	public List<AtividadeTurma> getAtividadesTurmas() {
		return atividadesTurmas;
	}

	public void setAtividadesTurmas(List<AtividadeTurma> atividadesTurmas) {
		this.atividadesTurmas = atividadesTurmas;
	}

	public AtividadeTurma getAtividadeTurma() {
		return atividadeTurma;
	}

	public void setAtividadeTurma(AtividadeTurma atividadeTurma) {
		this.atividadeTurma = atividadeTurma;
	}
}
