import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import Search.SearchResultsPage;
import Articles.ArticlePage;

public class DCWikiaApplication 
{
	static boolean programDone; // Used to determine when to exit program.
	static SearchResultsPage searchResultsPage; //A container for any search result pages that may be generated.
	static ArticlePage articlePage; //A container for any article pages that may be generated.
	static BufferedReader br; //A buffered reader for console input.
	
	public static void main(String[] args) throws IOException
	{
		br = new BufferedReader(new InputStreamReader(System.in)); //Initializes the br to accept user-input.
		programDone = false; //The program is now ready to run.
		
		System.out.println("Welcome to the home page! \n");
		generateSearchResults();
		
		while (!programDone)
		{ //Until the user chooses, the program will allow you to navigate basic options.			
			System.out.println("Please select one of the following options: ");
			System.out.println("1) Show search results.");
			System.out.println("2) Begin new search.");
			System.out.println("3) Exit program. \n");
			
			System.out.print("Answer: ");
			String input = br.readLine();
			System.out.println("");
			
			switch (input) //TODO: Add features to switch cases.
			{
			case "1": //Show the search results.
				SearchResultsPage.printResults();
				System.out.println("What would you like to do?");
				System.out.println("1) Select an Article.");
				
				System.out.print("Answer: ");
				String searchChoice = br.readLine();
				System.out.println("");
				
				switch (searchChoice) //TODO: Add more options from here.
				{
				case "1": //Select an Article for viewing.
					System.out.print("Please choose the article number you wish to view: ");
					int selectedResult = Integer.parseInt(br.readLine());
					System.out.println("");
					
					articlePage = searchResultsPage.selectSearchResult(selectedResult - 1);
					articlePage.printIssuesList();
					break;
					
				}
				
				break;
			case "2": //Begin a new search.
				generateSearchResults();
				break;
			case "3": //Exit the program.
				programDone = true;
				break;
			default: //None of the above. //TODO: Create and exception handler for more intuitive handling.
				System.out.println("You have entered and invalid input!");
			}
		}
		br.close();
	}
	
	/**
	 * Asks the user to enter the terms they wish to search.
	 * @throws IOException
	 */
	public static void generateSearchResults() throws IOException
	{
		System.out.print("Please enter the name of the article you wish to search: ");
		String input = br.readLine();
		System.out.println("");
		
		//TODO: Format input String to accept any text and turn it into URL friendly query format (e.g. earth+2, justice_league, justice+AND+LEAGUE+and+dark,
		//TODO: (Justice+AND+League)+OR+(Earth+AND+(2+OR+3)), etc.)
		
		searchResultsPage = new SearchResultsPage(returnURLPlaintext(input));
	}
	
	/**
	 * Takes in a search term, and concatonates it inside of a URL, then downloads the contents of the page, which is returned as a String.
	 * @param searchInput
	 * @return
	 * @throws IOException
	 */
	public static String returnURLPlaintext(String searchInput) throws IOException
	{
		URL searchResults = new URL("http://dc.wikia.com/api/v1/Search/List?query=" + searchInput + "&limit=25&minArticleQuality=10&namespaces=0%2C14"); //TODO: Modularize parameters such as limit and minArticleQuality. Make them attributes of searchResultsPage.java.
		ReadableByteChannel rbc = Channels.newChannel(searchResults.openStream()); //Reads the contents found at this URL.
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream("searchResults.html"); //Creates datastore for website contents for optimal access times.
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE); //Transfers data from rbc to the output file.
		FileReader fr = new FileReader("searchResults.html"); //Reads the contents of the file into a Stream.
		BufferedReader tempbr = new BufferedReader(fr); //Takes fs Stream and turns it into an input through BufferedReader.
		
		String searchContent = "";
		String temp;
		while ((temp = tempbr.readLine()) != null) //Writes website contents to local variable during building of String.
		{
			searchContent += temp;
		}
		
		tempbr.close();
		return searchContent; //Returns to contents of website as String.
	}
}
