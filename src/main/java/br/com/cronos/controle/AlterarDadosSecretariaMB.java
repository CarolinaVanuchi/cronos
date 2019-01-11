package br.com.cronos.controle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Secretaria;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@SessionScoped
@ManagedBean
public class AlterarDadosSecretariaMB {

	private Secretaria secretaria;
	private DAOGenerico dao;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	private UsuarioSessaoMB usuarioSessao;
	private String senha;
	private String senha2;

	public AlterarDadosSecretariaMB() {
		secretaria = new Secretaria();
		dao = new DAOGenerico();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		usuarioSessao = new UsuarioSessaoMB();
		senha = "";
		senha2 = "";
		preencherSecretaria();
	}

	public void alterarSecretaria() {
		try {
			if ((validacoesGerirUsuarios.buscarUsuarios(secretaria))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(secretaria))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				if (senha != "") {
					secretaria.setSenha(senha);
					secretaria.setSenha(CriptografiaSenha.criptografar(secretaria.getSenha()));
				}
				dao.alterar(secretaria);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherSecretaria();

			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarSecretaria");
			e.printStackTrace();
		}
	}

	public void preencherSecretaria() {
		secretaria = (Secretaria) usuarioSessao.recuperarSecretaria();
	}

	public Secretaria getSecretaria() {
		return secretaria;
	}

	public void setSecretaria(Secretaria secretaria) {
		this.secretaria = secretaria;
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
