package piastrellista.mailgun;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import piastrellista.entities.User;

@Component
// This class is responsible for sending emails using the Mailgun service
public class MailgunSender {

    // ATTRIBUTES
    private String apiKey;
    private String domainName;

    @Value("${mailgun.email}")
    private String email;

    // Constructor
    public MailgunSender(@Value("${mailgun.apikey}") String apiKey, @Value("${mailgun.domainname}") String domainName){
        this.apiKey = apiKey;
        this.domainName = domainName;
    }

    // This method sends a registration email to a user
    public void sendRegistrationEmail (User user){
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/"+ this.domainName + "/messages")
                    .basicAuth("api", this.apiKey)
                    .queryString("from", this.email)
                    .queryString("to", user.getEmail())
                    .queryString("subject", "Registration Complete!")
                    .queryString("text", "Congratulations " + user.getEmail() + " for registering!")
                    .asJson();
            System.out.println(response.getBody());
        } catch (Exception ex){
            System.out.println("An error occurred while sending the email: " + ex.getMessage());
        }

    }
}
