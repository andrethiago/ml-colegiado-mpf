package br.mp.mpf.carga;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class ArquivoIntegra {

	private String nomeArquivo;
	private Blob conteudo;
	private byte[] bytesConteudo;

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Blob getConteudo() {
		return conteudo;
	}

	public void setConteudo(Blob conteudo) throws SQLException, IOException {
		this.conteudo = conteudo;
		this.bytesConteudo = IOUtils.toByteArray(conteudo.getBinaryStream());
	}

	public byte[] getBytesConteudo() {
		return bytesConteudo;
	}

	public String getExtensao() {
		return FilenameUtils.getExtension(nomeArquivo);
	}

}
