package Articles;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ArticlePage 
{
	String id; //Field that contains the ID of a DC Wikia page.
	String title; //Field that contains the title/volume title of a DC Wikia page.
	ArrayList<String> issueNameList; //Lists the names of each issue belonging to this page. //TODO: Make into a dictionary that contains the name as key and ID/URL as value.
	
	JsonObject articleContentJson; //The actual Json page as a manipulatable Object.
	
	String articleJson; //The original Json code from the article.
	
	public ArticlePage(String idInput) throws IOException
	{
		id = idInput;
		importJsonContent();
		parseArticleJsonData();
	}
	
	public String returnArticle()
	{
		return ("Title of Volume: " + title + " | DCWikia ID: " + id);
	}
	public void printIssuesList()
	{
		for (int i = 0; i < issueNameList.size(); i++)
		{
			System.out.println(issueNameList.get(i));
		}
		System.out.println("");
	}
	
	/**
	 * Downloads the Json code from the API website based on the article ID of this object.
	 * @throws IOException
	 */
	private void importJsonContent() throws IOException
	{
		URL articleURL = new URL("http://dc.wikia.com/api/v1/Articles/AsSimpleJson?id=" + id);
		ReadableByteChannel rbc = Channels.newChannel(articleURL.openStream()); //Reads the contents found at this URL.
		FileOutputStream fos = new FileOutputStream("articleJson.html"); //Creates datastore for website contents for optimal access times.
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE); //Transfers data from rbc to the output file.
		FileReader fr = new FileReader("articleJson.html"); //Reads the contents of the file into a Stream.
		BufferedReader br = new BufferedReader(fr); //Takes fs Stream and turns it into an input through BufferedReader.
		
		String articleContent = "";
		String temp;
		while ((temp = br.readLine()) != null) //Writes website contents to local variable during building of String.
		{
			articleContent += temp;
		}
		
		articleJson = articleContent;
		
		fos.close();
		br.close();
	}
	
	
	private void parseArticleJsonData()
	{
		JsonParser parser = new JsonParser(); //Used to convert Strings into Json Objects.
		JsonElement articleElement = parser.parse(articleJson); //Converts String into the most generic Json type.
		JsonObject articleObject = articleElement.getAsJsonObject(); //Converts generic Json type into an Object.
		
		
		JsonArray sectionsArray = articleObject.get("sections").getAsJsonArray(); //Finds the "sections" section, and converts it into an array.
		title = sectionsArray.get(0).getAsJsonObject().get("title").getAsString(); //Inside the first element, the title for this article can always be found.
		
		//TODO: Replace this line below with an intelligent Json Object finder that finds the MOST APPROPRIATE "elements" JsonArray, which contains issues found in this volume set.
		//TODO: Perhaps use a for-loop on sectionsArray and use if statements to confirm position and population of "elements".
		JsonArray issuesArray = sectionsArray.get(1).getAsJsonObject().get("content").getAsJsonArray().get(0).getAsJsonObject().get("elements").getAsJsonArray(); 
		
		//THE ONLY TESTED AND PROVEN SEARCH THAT WORKS WITH THIS METHOD IS "earth+2". IF YOU WANT TO SEE HOW NORMAL BEHAVIOR WORKS, SEARCH "earth+2" AND SELECT OPTION 3, WHICH IS EARTH 2 VOL. 1.
		
		issueNameList = new ArrayList<String>();
		for (int i = 0; i < issuesArray.size(); i++) //Adds found issue names into the issueNameList.
		{
			issueNameList.add(issuesArray.get(i).getAsJsonObject().get("text").getAsString());
		}
		
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
	
}
