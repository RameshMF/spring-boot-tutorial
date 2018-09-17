package net.guides.springboot2.springboot2externalizingconfproperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="jdbc")
public class DataSourceConfig
{
	@NotNull
	private String driver;
	@NotNull
	private String url;
	@NotNull
	private String username;
	@Min(1) @Max(5)
	private String password;
	
	@Override
	public String toString()
	{
		return "DataSourceConfig [driver=" + driver + ", url=" + url + ", username=" + username + "]";
	}
	public String getDriver()
	{
		return driver;
	}
	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}

}
