package br.com.cronos.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Secretaria
 *
 */
@Entity
@Table(name = "tab_secretaria")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Secretaria extends Pessoa implements Serializable {

	public Secretaria() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@NotNull(message = "O campo chefe não pode ser nulo")
	@Column(nullable = false)
	private Boolean chefe;

	private String siape;

	public Boolean getChefe() {
		return chefe;
	}

	public void setChefe(Boolean chefe) {
		this.chefe = chefe;
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}
}
