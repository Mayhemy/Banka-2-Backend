package rs.edu.raf.si.bank2.otc.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.edu.raf.si.bank2.otc.dto.CommunicationDto;
import rs.edu.raf.si.bank2.otc.dto.ReserveDto;
import rs.edu.raf.si.bank2.otc.dto.TransactionElementDto;
import rs.edu.raf.si.bank2.otc.models.mongodb.ContractElements;
import rs.edu.raf.si.bank2.otc.models.mongodb.TransactionElements;
import rs.edu.raf.si.bank2.otc.utils.JwtUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ReservedService {

    private final JwtUtil jwtUtil;
    ObjectMapper mapper = new ObjectMapper();

    @Value("${services.main.host}")
    private String usersServiceHost;

    @Autowired
    public ReservedService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    public CommunicationDto sendReservation(TransactionElementDto teDto){
        String reserveJson;
        String url = "";

        if (teDto.getBuyOrSell() == ContractElements.BUY){
            url = "/reserveMoney";//u ovom slucaju mariaDbId je null
            teDto.setMariaDbId(138L);//todo HARD CODE NA USD
        }
        else if (teDto.getBuyOrSell() == ContractElements.SELL){
            switch (teDto.getTransactionElement()){
                case STOCK -> url = "/reserveStock";
                case OPTION -> url = "/reserveOption";
                case FUTURE -> url = "/reserveFuture";
            }
        }

        try {
            reserveJson = mapper.writeValueAsString(new ReserveDto(teDto.getUserId(), teDto.getMariaDbId(), teDto.getAmount()));
        } catch (JsonProcessingException e) { throw new RuntimeException(e); }

        System.out.println("RESERVE - " + reserveJson);

        return sendReservePost(url, reserveJson);
    }

    private CommunicationDto sendReservePost(String urlExtension, String postObjectBody){
        System.err.println("POSALI SMO SEND RESERVE");

        String token = jwtUtil.generateToken("anesic3119rn+banka2backend+admin@raf.rs");
        String []hostPort = usersServiceHost.split(":");
        BufferedReader reader;
        StringBuilder response = new StringBuilder();
        String line;

        try {
            URL url = new URL("http", hostPort[0], Integer.parseInt(hostPort[1]), "/api/reserve" + urlExtension);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(postObjectBody);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response: " + response.toString());
            connection.disconnect();
            reader.close();
            return new CommunicationDto(responseCode, response.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
