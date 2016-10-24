package exercise;

import java.io.StringReader;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Data {
	private final static String URI1 = "http://openapi.seoul.go.kr:8088/";
	private final static String URI2 = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode";
	private final static String KEY1 = "55676f65736b616a3332506e587358";
	private final static String KEY2 = "GF8avkJReYCw3MnjTdJYY27N%2F2WO1NRGv8PA5CG24eXn25olSgsZyk9%2BBLYwbrU%2FbATXPGtrbjibc5AM06HEbw%3D%3D";
	public static void main(String[] args) {
		Scanner sc =new Scanner(System.in);

        System.out.print("Enter a month of use(e.g 201501):\t");
        String useMonth = sc.nextLine();
		Data data = new Data();
		data.getData(useMonth);

	}

	private void getData(String useMonth) {
		try {
			String uri = URI1 + KEY1 + "/xml/CardSubwayTime/1/5/"+useMonth+"/";
			CloseableHttpClient httpclient = HttpClients.custom().build();
			HttpUriRequest getData = RequestBuilder.get().setUri(uri)
					// .addParameter("ServiceKey", KEY2)
					// .addParameter("MobileOS", "AND")
					// .addParameter("MobileApp", "appTesting")
					.build();
			CloseableHttpResponse response = httpclient.execute(getData);
			try {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(response.getEntity());
				System.out.println(result);

				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					InputSource is = new InputSource();
					is.setCharacterStream(new StringReader(result));

					Document doc = db.parse(is);
					NodeList nodes = doc.getElementsByTagName("row");


					for (int i = 0; i < nodes.getLength(); i++) {
						Element element = (Element) nodes.item(i);
						NodeList[] data = new NodeList[43];
						data[0] = element.getElementsByTagName("SUB_STA_NM");
						data[1] = element.getElementsByTagName("FOUR_RIDE_NUM");
						data[2] = element.getElementsByTagName("FOUR_ALIGHT_NUM");
						data[3] = element.getElementsByTagName("FIVE_RIDE_NUM");
						data[4] = element.getElementsByTagName("FIVE_ALIGHT_NUM");
						data[5] = element.getElementsByTagName("SIX_RIDE_NUM");
						data[6] = element.getElementsByTagName("SIX_ALIGHT_NUM");
						data[7] = element.getElementsByTagName("SEVEN_RIDE_NUM");
						data[8] = element.getElementsByTagName("SEVEN_ALIGHT_NUM");
						data[9] = element.getElementsByTagName("EIGHT_RIDE_NUM");
						data[10] = element.getElementsByTagName("EIGHT_ALIGHT_NUM");
						data[11] = element.getElementsByTagName("NINE_RIDE_NUM");
						data[12] = element.getElementsByTagName("NINE_ALIGHT_NUM");
						data[13] = element.getElementsByTagName("TEN_RIDE_NUM");
						data[14] = element.getElementsByTagName("TEN_ALIGHT_NUM");
						data[15] = element.getElementsByTagName("ELEVEN_RIDE_NUM");
						data[16] = element.getElementsByTagName("ELEVEN_ALIGHT_NUM");
						data[17] = element.getElementsByTagName("TWELVE_RIDE_NUM");
						data[18] = element.getElementsByTagName("TWELVE_ALIGHT_NUM");
						data[19] = element.getElementsByTagName("THIRTEEN_RIDE_NUM");
						data[20] = element.getElementsByTagName("THIRTEEN_ALIGHT_NUM");
						data[21] = element.getElementsByTagName("FOURTEEN_RIDE_NUM");
						data[22] = element.getElementsByTagName("FOURTEEN_ALIGHT_NUM");
						data[23] = element.getElementsByTagName("FIFTEEN_RIDE_NUM");
						data[24] = element.getElementsByTagName("FIFTEEN_ALIGHT_NUM");
						data[25] = element.getElementsByTagName("SIXTEEN_RIDE_NUM");
						data[26] = element.getElementsByTagName("SIXTEEN_ALIGHT_NUM");
						data[27] = element.getElementsByTagName("SEVENTEEN_RIDE_NUM");
						data[28] = element.getElementsByTagName("SEVENTEEN_ALIGHT_NUM");
						data[29] = element.getElementsByTagName("EIGHTEEN_RIDE_NUM");
						data[30] = element.getElementsByTagName("EIGHTEEN_ALIGHT_NUM");
						data[31] = element.getElementsByTagName("NINETEEN_RIDE_NUM");
						data[32] = element.getElementsByTagName("NINETEEN_ALIGHT_NUM");
						data[33] = element.getElementsByTagName("TWENTY_RIDE_NUM");
						data[34] = element.getElementsByTagName("TWENTY_ALIGHT_NUM");
						data[35] = element.getElementsByTagName("TWENTY_ONE_RIDE_NUM");
						data[36] = element.getElementsByTagName("TWENTY_ONE_ALIGHT_NUM");
						data[37] = element.getElementsByTagName("TWENTY_TWO_RIDE_NUM");
						data[38] = element.getElementsByTagName("TWENTY_TWO_ALIGHT_NUM");
						data[39] = element.getElementsByTagName("TWENTY_THREE_RIDE_NUM");
						data[40] = element.getElementsByTagName("TWENTY_THREE_ALIGHT_NUM");
						data[41] = element.getElementsByTagName("MIDNIGHT_RIDE_NUM");
						data[42] = element.getElementsByTagName("MIDNIGHT_ALIGHT_NUM");
						Element station = (Element) data[0].item(0);
						System.out.println("역명: " + station.getTextContent());
						for (int j = 1; j < data.length; j++) {
							if (data[j] == null) {
								System.out.print("");
							} else {
								Element number = (Element) data[j].item(0);
								if (j % 2 == 1) {
									System.out.println((j + 7)/2 + "시 승차인원: " + number.getTextContent()+"명");
								} else {
									System.out.println((j + 6)/2 + "시 하차인원: " + number.getTextContent()+"명");
								}

							}

						}

						System.out.println("---------------------------------");

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} finally {
				response.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
