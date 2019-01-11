package br.com.cronos.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Administrador
 *
 */
@Entity
@Table(name = "tab_administrador")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Administrador extends Pessoa implements Serializable {

	public Administrador() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@NotNull(message = "O campo função não pode ser nulo")
	@Column(nullable = false)
	private String funcao;

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

}
