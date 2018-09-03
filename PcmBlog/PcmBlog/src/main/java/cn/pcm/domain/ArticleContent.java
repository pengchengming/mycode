package cn.pcm.domain;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "T_ARTICLE_CONTENT")
public class ArticleContent {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	private Blob content;

	@OneToOne
	@JoinColumn(name = "article_id")
	private ArticleDetail articleDeatil;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public ArticleDetail getArticleDeatil() {
		return articleDeatil;
	}

	public void setArticleDeatil(ArticleDetail articleDeatil) {
		this.articleDeatil = articleDeatil;
	}

}
