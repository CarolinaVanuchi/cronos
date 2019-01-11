package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cronos.dao.DAOFiltros;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.GrupoTurma;

@ViewScoped
@ManagedBean
public class ListaGrupoTurmaAlunoMB {

	private List<GrupoTurma> gruposTurmas;
	private DAOFiltros daoFiltros;
	private UsuarioSessaoMB usuarioSessao;
	private AlunoTurma alunoTurma;
	private GrupoTurma grupoTurma;
	
	public ListaGrupoTurmaAlunoMB() {
		daoFiltros = new DAOFiltros();
		alunoTurma = new AlunoTurma();
		gruposTurmas = new ArrayList<>();
		usuarioSessao = new UsuarioSessaoMB();
		grupoTurma = new GrupoTurma();
		preencherGruposTurmas();
	}
	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}

	private void preencherGruposTurmas() {
		gruposTurmas = daoFiltros.gruposTurmaAluno(recuperarTurmaAluno());		
	}
	public List<GrupoTurma> getGruposTurmas() {
		return gruposTurmas;
	}
	public void setGruposTurmas(List<GrupoTurma> gruposTurmas) {
		this.gruposTurmas = gruposTurmas;
	}
	public GrupoTurma getGrupoTurma() {
		return grupoTurma;
	}
	public void setGrupoTurma(GrupoTurma grupoTurma) {
		this.grupoTurma = grupoTurma;
	}
}
