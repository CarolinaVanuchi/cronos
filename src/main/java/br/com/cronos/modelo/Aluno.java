package br.com.cronos.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Aluno
 *
 */
@Entity
@Table(name = "tab_aluno")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Aluno extends Pessoa implements Serializable {

	public Aluno() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@NotNull(message = "O campo RA não pode ser nulo")
	@Column(nullable = false, unique = true)
	private String ra;

	private String situacao;

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

}
