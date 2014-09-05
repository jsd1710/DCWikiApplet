package Search;
import java.io.IOException;
import java.util.ArrayList;

import Articles.ArticlePage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class SearchResultsPage 
{
	private static ArrayList<SearchResult> searchResultsParsed; //A list of SearchResult objects for simple access after parsing.
	private static String searchResults; //Original Json code for search results.
	
	public SearchResultsPage(String unparsedData)
	{
		searchResultsParsed = new ArrayList<SearchResult>();
		searchResults = unparsedData;
		parseResults();
	}
	
	public static void parseResults()
	{
		JsonParser parser = new JsonParser();
		JsonElement searchPageElement = parser.parse(searchResults); //Turns the JSON string into a JSON Element (Like a class).
		JsonObject searchPageObject = searchPageElement.getAsJsonObject();
		
		JsonArray items = searchPageObject.getAsJsonArray("items");
		for (int i = 0; i < items.size(); i++)
		{
			SearchResult temp = new SearchResult(items.get(i));
			searchResultsParsed.add(temp);
		}
	}
	
	public static void printResults()
	{
		for (int i = 0; i < searchResultsParsed.size(); i++)
		{
			System.out.println( (i+1) + ": " + searchResultsParsed.get(i).returnSearchResult() );
		}
		System.out.println("");
	}
	
	public ArticlePage selectSearchResult(int selectedResult) throws IOException
	{
		ArticlePage selectedPage = new ArticlePage(searchResultsParsed.get(selectedResult).getId());
		System.out.println(selectedPage.returnArticle() + "\n" + "------------------------------------------------------------------------------------");
		
		return selectedPage;
	}
}


