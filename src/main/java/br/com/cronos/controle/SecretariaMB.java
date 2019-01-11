package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Secretaria;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@SessionScoped
@ManagedBean
public class SecretariaMB {

	private Secretaria secretaria;
	private List<Secretaria> secretarias;
	private DAOGenerico dao;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	public SecretariaMB() {
		criarNovoObjetoSecretaria();
		secretarias = new ArrayList<>();
		dao = new DAOGenerico();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		preencherListaSecretaria();
	}

	public void salvar() {
		try {
			if (secretaria.getId() == null) {
				if (validacoesGerirUsuarios.buscarSiape(secretaria)) {
					ExibirMensagem.exibirMensagem(Mensagem.SIAPE);
				} else if (validacoesGerirUsuarios.buscarUsuarios(secretaria)) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else {
					if (secretaria.getSenha().isEmpty()) {
						secretaria.setSenha("123");
					}
					secretaria.setStatus(true);
					secretaria.setDataCadastro(new Date());
					secretaria.setPerfil("SECRETARIA");
					secretaria.setSenha(CriptografiaSenha.criptografar(secretaria.getSenha()));
					dao.inserir(secretaria);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					criarNovoObjetoSecretaria();
					FecharDialog.fecharDialogSecretaria();
					preencherListaSecretaria();

				}
			} else {
				if ((validacoesGerirUsuarios.buscarSiape(secretaria))
						&& (validacoesGerirUsuarios.buscarSiapeAlterar(secretaria))) {
					ExibirMensagem.exibirMensagem(Mensagem.SIAPE);
				} else if ((validacoesGerirUsuarios.buscarUsuarios(secretaria))
						&& (validacoesGerirUsuarios.buscarUsuarioAlterar(secretaria))) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else {
					if (secretaria.getSenha().isEmpty()) {
						secretaria.setSenha("123");
					}
					secretaria.setSenha(CriptografiaSenha.criptografar(secretaria.getSenha()));
					dao.alterar(secretaria);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					criarNovoObjetoSecretaria();
					FecharDialog.fecharDialogSecretaria();
					preencherListaSecretaria();
				}
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(Secretaria secretaria) {
		try {
			secretaria.setStatus(false);
			dao.alterar(secretaria);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			preencherListaSecretaria();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoObjetoSecretaria();
	}

	public void criarNovoObjetoSecretaria() {
		secretaria = new Secretaria();
	}

	public void preencherListaSecretaria() {
		secretarias = dao.listaComStatus(Secretaria.class);
	}

	public Secretaria getSecretaria() {
		return secretaria;
	}

	public void setSecretaria(Secretaria secretaria) {
		this.secretaria = secretaria;
	}

	public List<Secretaria> getSecretarias() {
		return secretarias;
	}

	public void setSecretarias(List<Secretaria> secretarias) {
		this.secretarias = secretarias;
	}
}
