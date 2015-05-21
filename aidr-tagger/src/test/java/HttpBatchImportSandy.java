import java.net.*;
import java.io.*;
import java.util.*;

import qa.qcri.aidr.common.code.Configurator;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Test class that reads labeled training data from a file and sends each item
 * to the AIDR pipeline.
 * 
 * To use with the Sandy dataset, configure attributes and labels in a
 * deployment and modify the labels and attribute IDs in processSandyData() to
 * match the configuration.
 * 
 * @author jrogstadius
 */
public class HttpBatchImportSandy {

	static String host = "localhost";
	static int port;

	static {
		Configurator testConfigurator = TaggerConfigurator.getInstance();
		testConfigurator.initProperties(TaggerConfigurator.configLoadFileName,
				TaggerConfigurationProperty.values());
		port = Integer.parseInt(testConfigurator
				.getProperty(TaggerConfigurationProperty.HTTP_INPUT_PORT));
	}

	static Socket socket;
	static BufferedReader serverIn;
	static PrintWriter serverOut;
	static int messageID = 0;

	public static void main(String[] args) throws Exception {
		System.out.println("ClientTest");
		sendMessages();
	}

	public static void sendMessages() throws Exception {
		// Create socket connection
		socket = new Socket(host, port);
		serverOut = new PrintWriter(socket.getOutputStream(), true);
		serverIn = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		// processTweetsFromFile(5, "..\\1k_random_tweets.txt");
		processSandyData();

		socket.close();
	}

	public static void getUserInput() {

		Scanner scanner = new Scanner(System.in);
		String line = "";
		while (!(line = scanner.nextLine()).equals("exit")) {
			serverOut.println(line);
		}

		scanner.close();
	}

	// Reads tweets from a file in which each line is one json-formatted tweet
	public static void processJsonTweetsFromFile(String crisisCode,
			int crisisID, String filename) throws Exception {
		Scanner scanner = new Scanner(System.in);

		BufferedReader br;
		br = new BufferedReader(new FileReader(filename));
		String tweet = br.readLine();
		while (tweet != null) {
			sendTweetToServer(crisisCode, crisisID, tweet, null);
			scanner.nextLine();
			tweet = br.readLine();
		}
		br.close();
		scanner.close();
	}

	public static void processSandyData() throws Exception {
		/*
		 * * SANDY DATASET COLUMNS 0 _unit_id, 1 _golden, 2 _unit_state, 3
		 * _trusted_judgments, 4 _last_judgment_at, 5
		 * the_author_of_the_tweet_seems_to_be_an_eye_witness_of_the_event, 6
		 * the_author_of_the_tweet_seems_to_be_an_eye_witness_of_the_event
		 * :confidence, 7 type_of_message, 8 type_of_message:confidence, 9 nil,
		 * 10
		 * the_author_of_the_tweet_seems_to_be_an_eye_witness_of_the_event_gold,
		 * 11 tweet, 12 tweet_no, 13 tweet_no_rt, 14 type_of_message_gold, 15
		 * user
		 * 
		 * * SANDY DATASET example TUPLE 0 238841781, 1 false, 2 finalized, 3 4,
		 * 4 1/2/2013 13:37:11, 5 , 6 , 7
		 * "Informative: offers/gives donations of money, goods, or free services"
		 * , 8 0.2689, 9 , 10 , 11 important --&gt; @JebBush suggests federal
		 * gov't not crucial to storm recovery http://t.co/pVsV6qoS #haction
		 * #2012 #sandy, 12 11899, 13 important --&gt; @JebBush suggests federal
		 * gov't not crucial to storm recovery http://t.co/pVsV6qoS #haction
		 * #2012 #sandy, 14 , 15 danholler
		 * 
		 * * ATTRIBUTE DEFINITIONS 20 Eyewitness null, eyewitness 23 Informative
		 * null, informative 24 Casualties null, casualties 25 Caution or advice
		 * null, advice 27 Reaction null, reaction 28 Damage null, damage 29
		 * Media source null, mediasource 30 Donation null, offer, request 31
		 * Photo or video null, photo 32 People missing null, missing
		 * 
		 * * SANDY DATASET LABELS Can not judge (not in English, too short,
		 * etc.) Informative: casualties (people injured or dead) Informative:
		 * caution or advice Informative: celebrities or authorities react to
		 * the event or visit the area Informative: damage (building, road,
		 * lines, etc.) Informative: information source with extensive coverage
		 * (radio, tv, website, etc.) Informative: offers/gives donations of
		 * money, goods, or free services Informative: other Informative: other
		 * type of photos/videos (not in the above classes) Informative: people
		 * missing, or lost people found Informative: requests donations of
		 * money, goods, or free services Not informative: personal only Not
		 * informative: unrelated to the disaster
		 */

		String crisisCode = "sandy_hurricane_test";
		int crisisID = 5;
		String filename = "C:\\projects\\aidr\\develop\\predict\\Sandy-Labeled.csv";

		// Attribute IDs
		int eyewitnessID = 20;
		int informativeID = 23;
		int casualtiesID = 24;
		int adviceID = 25;
		int reactionID = 27;
		int damageID = 28;
		int mediaID = 29;
		int donationID = 30;
		int photoID = 31;
		int missingID = 32;

		// Labels used in the Sandy dataset
		String casualtiesStr = "casualties (people injured or dead)";
		String adviceStr = "caution or advice";
		String reactionStr = "celebrities or authorities react to the event or visit the area";
		String damageStr = "damage (building, road, lines, etc.)";
		String mediaStr = "information source with extensive coverage (radio, tv, website, etc.)";
		String donationStr1 = "offers/gives donations of money, goods, or free services";
		String donationStr2 = "requests donations of money, goods, or free services";
		String photoStr = "other type of photos/videos (not in the above classes)";
		String missingStr = "people missing, or lost people found";

		CSVReader reader = new CSVReader(new FileReader(filename));
		String[] line;
		reader.readNext();
		while ((line = reader.readNext()) != null) {
			String tweetText = line[11];
			long userID = 0;

			String tweet = "{text:\"" + tweetText.replaceAll("\"", "'")
					+ "\", user: {id:" + userID + "}}";

			ArrayList<NominalLabelBC> labels = new ArrayList<>();

			// Match labels in the Sandy dataset to attribute IDs/labels
			// labels.add(new NominalLabelBC(0, eyewitnessID,
			// line[5].equals("true") ? "eyewitness" : "null", 1));
			// String typeStr = line[14].equals("")
			// && Double.parseDouble(line[8]) > 0.5 ? line[7] : line[14];
			// labels.add(new NominalLabelBC(0, informativeID, typeStr
			// .contains("Informative") ? "informative" : "null", 1));
			// labels.add(new NominalLabelBC(0, casualtiesID, typeStr
			// .contains(casualtiesStr) ? "casualties" : "null", 1));
			// labels.add(new NominalLabelBC(0, adviceID, typeStr
			// .contains(adviceStr) ? "advice" : "null", 1));
			// labels.add(new NominalLabelBC(0, reactionID, typeStr
			// .contains(reactionStr) ? "reaction" : "null", 1));
			// labels.add(new NominalLabelBC(0, damageID, typeStr
			// .contains(damageStr) ? "damage" : "null", 1));
			// labels.add(new NominalLabelBC(0, mediaID,
			// typeStr.contains(mediaStr) ? "mediasource" : "null", 1));
			// labels.add(new NominalLabelBC(0, donationID, typeStr
			// .contains(donationStr1) ? "offer" : (typeStr
			// .contains(donationStr2) ? "request" : "null"), 1));
			// labels.add(new NominalLabelBC(0, photoID,
			// typeStr.contains(photoStr) ? "photo" : "null", 1));
			// labels.add(new NominalLabelBC(0, missingID, typeStr
			// .contains(missingStr) ? "missing" : "null", 1));
			for (NominalLabelBC l : labels)
				l.setHumanLabel(true);

			sendTweetToServer(crisisCode, crisisID, tweet, labels);

			if (messageID % 150 == 149)
				Thread.sleep(20000);
			else
				Thread.sleep(10);
		}
		reader.close();
	}

	static void sendTweetToServer(String crisisCode, int crisisID,
			String jsonTweet, List<NominalLabelBC> labels) {
		String labelsStr = "";
		if (labels != null && labels.size() > 0) {
			labelsStr = "nominal_labels: [";
			for (int i = 0; i < labels.size(); i++) {
				if (i > 0)
					labelsStr += ",";
				labelsStr += DocumentJSONConverter.getLabelJson(crisisID,
						labels.get(i));
			}
			labelsStr += "]";
		}
		String info = "{ \"crisis_code\": \"" + crisisCode
				+ "\", doctype:\"twitter\", " + labelsStr + " }";
		jsonTweet = jsonTweet.substring(0, jsonTweet.length() - 1)
				+ ", \"aidr\": " + info + "}";
		System.out.println(jsonTweet);
		serverOut.println(jsonTweet);
	}

	public static void getServerOutput() {

		String line;
		try {
			while ((line = serverIn.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
