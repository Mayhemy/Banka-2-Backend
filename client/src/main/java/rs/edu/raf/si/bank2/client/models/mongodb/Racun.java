package rs.edu.raf.si.bank2.client.models.mongodb;

import org.springframework.data.annotation.Id;
import rs.edu.raf.si.bank2.client.models.mongodb.enums.BalanceStatus;
import rs.edu.raf.si.bank2.client.models.mongodb.enums.BalanceType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class Racun {

    public Racun(String registrationNumber, String ownerId, Double balance, Double availableBalance, Long assignedAgentId, String creationDate,
                 String expirationDate, String currency, BalanceStatus balanceStatus, BalanceType balanceType, Integer interestRatePercentage, Double accountMaintenance) {
        this.registrationNumber = registrationNumber;
        this.ownerId = ownerId;
        this.balance = balance;
        this.availableBalance = availableBalance;
        this.assignedAgentId = assignedAgentId;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.currency = currency;
        this.balanceStatus = balanceStatus;
        this.balanceType = balanceType;
        this.interestRatePercentage = interestRatePercentage;
        this.accountMaintenance = accountMaintenance;
    }

    public Racun() {
    }


    private String registrationNumber;
    private String ownerId;
    private Double balance;
    private Double availableBalance; //ovo je toliko glupo .i.
    private Long assignedAgentId;
    private String creationDate;
    private String expirationDate;
    private String currency;
    private BalanceStatus balanceStatus; //enum active / not active
    private BalanceType balanceType; //enum //licni, stedni ...
    private Integer interestRatePercentage; //1% - kamatna stopa
    private Double accountMaintenance;
}
