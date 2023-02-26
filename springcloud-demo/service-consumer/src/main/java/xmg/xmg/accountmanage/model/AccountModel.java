package xmg.xmg.accountmanage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountModel {
    private Integer id;
    private String username;
    private String phone;
}
