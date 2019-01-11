package br.com.cronos.controle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Administrador;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@ViewScoped
@ManagedBean
public class AlterarDadosAdministradorMB {

	private Administrador administrador;
	private DAOGenerico dao;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	private UsuarioSessaoMB usuarioSessao;
	private String senha;
	private String senha2;

	public AlterarDadosAdministradorMB() {
		administrador = new Administrador();
		dao = new DAOGenerico();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		usuarioSessao = new UsuarioSessaoMB();
		senha = "";
		senha2 = "";
		preencherAdministrador();
	}

	public void alterarAdministrador() {
		try {
			if ((validacoesGerirUsuarios.buscarUsuarios(administrador))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(administrador))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				if (senha != "") {
					administrador.setSenha(senha);
					administrador.setSenha(CriptografiaSenha.criptografar(administrador.getSenha()));
				}

				dao.alterar(administrador);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherAdministrador();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarAdministrador");
			e.printStackTrace();
		}
	}

	public void preencherAdministrador() {
		try {
			usuarioSessao = new UsuarioSessaoMB();
			administrador = (Administrador) usuarioSessao.recuperarAdmisnitrador();
		} catch (Exception e) {
			System.err.println("preencherAdministrador");
			e.printStackTrace();
		}
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}
}
