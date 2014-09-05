package Search;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SearchResult
{
	private String id;
	private String title;
	private String url;
	private String ns;
	private String quality;
	
	public SearchResult(JsonElement articleInfoInput)
	{
		JsonObject articleInfoObject = articleInfoInput.getAsJsonObject();
		id = articleInfoObject.get("id").toString();
		title = articleInfoObject.get("title").toString();
		url = articleInfoObject.get("url").toString();
		ns = articleInfoObject.get("ns").toString();
		quality = articleInfoObject.get("quality").toString();
	}	
	
	public String returnSearchResult()
	{
		return (title + " " + url);
	}
	
	public String getId() 
	{
		return id;
	}
	public void setId(String id) 
	{
		this.id = id;
	}

	public String getTitle() 
	{
		return title;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getUrl() 
	{
		return url;
	}
	public void setUrl(String url) 
	{
		this.url = url;
	}

	public String getNs() 
	{
		return ns;
	}
	public void setNs(String ns) 
	{
		this.ns = ns;
	}

	public String getQuality() 
	{
		return quality;
	}
	public void setQuality(String quality) 
	{
		this.quality = quality;
	}

}
