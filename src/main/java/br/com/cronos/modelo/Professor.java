package br.com.cronos.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Professor
 *
 */
@Entity
@Table(name = "tab_professor")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Professor extends Pessoa implements Serializable {

	public Professor() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@NotNull(message = "O campo siape não pode ser nulo")
	@Column(nullable = false, unique = true)
	private String siape;

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

}
