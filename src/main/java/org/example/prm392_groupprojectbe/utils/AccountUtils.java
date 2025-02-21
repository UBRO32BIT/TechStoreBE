package org.example.prm392_groupprojectbe.utils;

import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountUtils {
    private AccountUtils() {}

    public static Account getCurrentAccount(){
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof Account) {
            return (Account) object;
        } else {
            return null;
        }
    }

    public static boolean isAdminRole(Account account) {
        return account != null && account.getRole().equals(AccountRoleEnum.ADMIN);
    }
}
