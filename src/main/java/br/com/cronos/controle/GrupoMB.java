package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Grupo;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.PermiteInativar;

@SessionScoped
@ManagedBean
public class GrupoMB {
	private Grupo grupo;
	private List<Grupo> grupos;
	private DAOGenerico dao;
	private PermiteInativar permiteInativar;

	public GrupoMB() {
		dao = new DAOGenerico();
		criarNovoObjetoGrupo();
		grupos = new ArrayList<>();
		preencherListaGrupo();
	}

	public void salvar() {
		try {
			if (grupo.getId() == null) {
				grupo.setStatus(true);
				grupo.setDataCadastro(new Date());
				dao.inserir(grupo);
				FecharDialog.fecharDialogGrupo();
				criarNovoObjetoGrupo();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaGrupo();
			} else {
				dao.alterar(grupo);
				FecharDialog.fecharDialogGrupo();
				criarNovoObjetoGrupo();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaGrupo();
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(Grupo grupo) {
		permiteInativar = new PermiteInativar();
		try {
			if (permiteInativar.verificarGrupoComGrupoTurma(grupo.getId())) {
				if (permiteInativar.verificarGrupoComAtividade(grupo.getId())) {
					grupo.setStatus(false);
					dao.alterar(grupo);
					preencherListaGrupo();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.GRUPO_COM_ATIVIDADE);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.GRUPO_COM_GRUPO_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovoObjetoGrupo() {
		grupo = new Grupo();
	}

	public void preencherListaGrupo() {
		grupos = dao.listaComStatus(Grupo.class);
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

}
