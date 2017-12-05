package com.example.alucardc.idlegame;

/**
 * Created by AlucardC on 2017/12/6.
 */

public class Player {
    public MyPlayerStatus[] playerStatuses;
    public MyPlayerInventory[] playerInventory;
    public MyPlayerMobsCollection[] playerMobsCollection;

}

class MyPlayerStatus
{
    public String playerID;
    public int playerMaxHP;
    public int playerCurrentHP;
    public int playerATK;
}

class MyPlayerInventory
{
    public int
        i100101,i100102,i100103,
        i100201,i100202,i100203,
        i100301,i100302,i100303,
        i110101,i110102,
        i200101,i200102,i200103,
        i200201,i200202,i200203,
        i200301,i200302,i200303,
        i210101,i210102,
        i910101,i910102;
}

class MyPlayerMobsCollection
{

}
