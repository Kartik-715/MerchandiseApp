package com.example.merchandiseapp.Prevalent;

import android.content.Intent;

public class Prevalent_Intent
{
    static public void setIntent(Intent intent)
    {
        intent.putExtra("name", Prevalent.currentOrderType);
        intent.putExtra("address", Prevalent.currentAddress);
        intent.putExtra("contact", Prevalent.currentPhone);
        intent.putExtra("email", Prevalent.currentEmail);
        intent.putExtra("gender", Prevalent.currentGender);
        intent.putExtra("wallet", Prevalent.currentWalletMoney);
    }
}


