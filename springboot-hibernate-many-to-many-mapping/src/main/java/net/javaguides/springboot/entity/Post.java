package net.javaguides.springboot.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Post domain model
 * @author javaguides.net
 *
 */
@Entity
@Table(name = "posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Column(name = "title")
    private String title;
	
	@Column(name = "description")
    private String description;
	
	@Column(name = "content")
    private String content;
	
	@Column(name = "posted_at")
    private Date postedAt = new Date();
	
	@Column(name = "last_updated_at")
    private Date lastUpdatedAt = new Date();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "post_tags",
		joinColumns = { @JoinColumn(name = "post_id")},
		inverseJoinColumns = { @JoinColumn (name = "tag_id")})
	private Set<Tag> tags = new HashSet<>();
    
    public Post() {
    	
    }
    
	public Post(String title, String description, String content) {
		super();
		this.title = title;
		this.description = description;
		this.content = content;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getPostedAt() {
		return postedAt;
	}
	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}
	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}
	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

}
