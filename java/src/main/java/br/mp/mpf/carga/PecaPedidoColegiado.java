package br.mp.mpf.carga;

import java.util.Date;

public class PecaPedidoColegiado {

	private Long id;
	private Long idDocumentoPrincipal;
	private Long membroResponsavel;
	private Date dataCadastro;
	private Long integra;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdDocumentoPrincipal() {
		return idDocumentoPrincipal;
	}

	public void setIdDocumentoPrincipal(Long idDocumentoPrincipal) {
		this.idDocumentoPrincipal = idDocumentoPrincipal;
	}

	public Long getMembroResponsavel() {
		return membroResponsavel;
	}

	public void setMembroResponsavel(Long membroResposavel) {
		this.membroResponsavel = membroResposavel;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Long getIntegra() {
		return integra;
	}

	public void setIntegra(Long integra) {
		this.integra = integra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idDocumentoPrincipal == null) ? 0 : idDocumentoPrincipal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PecaPedidoColegiado other = (PecaPedidoColegiado) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (idDocumentoPrincipal == null) {
			if (other.idDocumentoPrincipal != null) {
				return false;
			}
		} else if (!idDocumentoPrincipal.equals(other.idDocumentoPrincipal)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PecaPedidoColegiado [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (idDocumentoPrincipal != null) {
			builder.append("idDocumentoPrincipal=");
			builder.append(idDocumentoPrincipal);
			builder.append(", ");
		}
		if (membroResponsavel != null) {
			builder.append("membroResponsavel=");
			builder.append(membroResponsavel);
			builder.append(", ");
		}
		if (dataCadastro != null) {
			builder.append("dataCadastro=");
			builder.append(dataCadastro);
			builder.append(", ");
		}
		if (integra != null) {
			builder.append("integra=");
			builder.append(integra);
		}
		builder.append("]");
		return builder.toString();
	}

}
