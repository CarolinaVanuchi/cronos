package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Atividade;
import br.com.cronos.modelo.Grupo;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.PermiteInativar;

@SessionScoped
@ManagedBean
public class AtividadeMB {

	private Atividade atividade;
	private List<Atividade> atividades;
	private List<Grupo> grupos;
	private DAOGenerico dao;
	private PermiteInativar permiteInativar;

	public AtividadeMB() {
		dao = new DAOGenerico();
		criarNovoObjetoAtividade();
		atividades = new ArrayList<>();
		grupos = new ArrayList<>();
		preencherListaAtividade();

	}

	public void salvar() {
		try {
			if (atividade.getId() == null) {
				atividade.setDataCadastro(new Date());
				atividade.setStatus(true);
				dao.inserir(atividade);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogAtividade();
				criarNovoObjetoAtividade();
				preencherListaAtividade();
			} else {
				dao.alterar(atividade);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogAtividade();
				criarNovoObjetoAtividade();
				preencherListaAtividade();
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(Atividade atividade) {
		permiteInativar = new PermiteInativar();
		try {
			if (permiteInativar.verificarAtividadeComAtividadeTurma(atividade.getId())) {
				atividade.setStatus(false);
				dao.alterar(atividade);
				preencherListaAtividade();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.ATIVIDADE_COM_ATIVIDADE_TURMA);
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovoObjetoAtividade() {
		atividade = new Atividade();

	}

	public void preencherListaAtividade() {
		atividades = dao.listaComStatus(Atividade.class);
	}

	public void preencherListaGrupo() {
		grupos = dao.listaComStatus(Grupo.class);
	}

	public List<Grupo> completarGrupo(String str) {
		preencherListaGrupo();
		List<Grupo> gruposSelecionados = new ArrayList<>();
		for (Grupo gru : grupos) {
			if (gru.getDescricao().toLowerCase().startsWith(str)) {
				gruposSelecionados.add(gru);
			}
		}
		return gruposSelecionados;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public List<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

}
