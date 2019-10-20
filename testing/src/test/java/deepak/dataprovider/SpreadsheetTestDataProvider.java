package deepak.dataprovider;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SpreadsheetTestDataProvider {

	public SpreadsheetTestDataProvider() {
		// TODO Auto-generated constructor stub
	}

	@DataProvider
	public Object[][] getData(ITestContext ctx) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> param = ctx.getCurrentXmlTest().getAllParameters();
		try {
			List<List<Object>> values = getSpreadsheetData(param.get("spreadsheetId"), "Moke!A2:F");
			int index=0;
			Object returnValue[][] = new Object[values.size()][1];
			for (int i = 0; i < values.size(); i++) {
				List<Object> row =values.get(i);
				Map<String,Object> testData =mapper.readValue(row.get(2).toString(), Map.class);
				testData.put("id", row.get(0));
				testData.put("testDescription", row.get(1));
				testData.put("expected",row.get(3));
				testData.put("dateCreated",row.get(4));
				testData.put("writtenBy",row.get(5));
				returnValue[index][0] = testData;
				index++;
			}
			return returnValue;

		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;//new Object[][] { { "Deepak" }, { "Ankit" } };
	}

	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "./src/main/resources/credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new FileReader(CREDENTIALS_FILE_PATH));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	/**
	 * https://docs.google.com/spreadsheets/d/spreadsheetId/edit
	 * range: name of the sheet and range
	 */
	public static List<List<Object>> getSpreadsheetData(String spreadsheetId, String range)
			throws IOException, GeneralSecurityException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		List<List<Object>> values = response.getValues();
		System.out.println(values.toString());
		return values;
	}
}
