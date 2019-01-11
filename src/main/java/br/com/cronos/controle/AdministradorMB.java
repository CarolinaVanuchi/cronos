package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Administrador;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@SessionScoped
@ManagedBean
public class AdministradorMB {
	private Administrador administrador;
	private List<Administrador> administradores;
	private DAOGenerico dao;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	public AdministradorMB() {
		dao = new DAOGenerico();
		criarNovoObjetoAdministrador();
		administradores = new ArrayList<>();
		preencherListaAdministrador();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
	}

	public void salvar() {
		try {
			if (administrador.getId() == null) {
				if (validacoesGerirUsuarios.buscarUsuarios(administrador)) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else {
					if (administrador.getSenha().isEmpty()) {
						administrador.setSenha("123");
					}
					administrador.setStatus(true);
					administrador.setDataCadastro(new Date());
					administrador.setPerfil("ADMINISTRADOR");
					administrador.setSenha(CriptografiaSenha.criptografar(administrador.getSenha()));
					dao.inserir(administrador);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					criarNovoObjetoAdministrador();
					FecharDialog.fecharDialogAdministrador();
					preencherListaAdministrador();
				}
			} else {
				if ((validacoesGerirUsuarios.buscarUsuarios(administrador))
						&& (validacoesGerirUsuarios.buscarUsuarios(administrador))) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else {
					if (administrador.getSenha().isEmpty()) {
						administrador.setSenha("123");
					}
					administrador.setSenha(CriptografiaSenha.criptografar(administrador.getSenha()));
					dao.alterar(administrador);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					criarNovoObjetoAdministrador();
					FecharDialog.fecharDialogAdministrador();
					preencherListaAdministrador();
				}
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro salvar AdministradorMB");
			e.printStackTrace();
		}
	}

	public void inativar(Administrador administrador) {
		try {
			administrador.setStatus(false);
			dao.alterar(administrador);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			preencherListaAdministrador();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovoObjetoAdministrador() {
		administrador = new Administrador();
	}

	private void preencherListaAdministrador() {
		try {
			administradores = dao.listaComStatus(Administrador.class);
		} catch (Exception e) {
			System.err.println("Erro preencherListaAdministrador");
			e.printStackTrace();
		}
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}

	public List<Administrador> getAdministradores() {
		return administradores;
	}

	public void setAdministradores(List<Administrador> administradores) {
		this.administradores = administradores;
	}
}
