package rs.edu.raf.si.bank2.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.edu.raf.si.bank2.client.models.mongodb.enums.BalanceStatus;
import rs.edu.raf.si.bank2.client.models.mongodb.enums.BalanceType;
import rs.edu.raf.si.bank2.client.models.mongodb.enums.BussinessAccountType;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PoslovniRacunDto {

//      private String id;
//      private String registrationNumber;
      private String ownerId;
//      private Double balance;
//      private Double availableBalance;
      private Long assignedAgentId;
      private String currency;
//      private BalanceStatus balanceStatus;
      private BussinessAccountType bussinessAccountType;

}
